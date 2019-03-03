package com.afterpay.excercise.frauddetection.datastore;

import com.afterpay.excercise.frauddetection.model.Record;
import com.afterpay.excercise.frauddetection.model.Transactions;
import com.afterpay.excercise.frauddetection.model.ITransactionDataStore;
import com.afterpay.excercise.frauddetection.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class InMemoryTransactionDataStore implements ITransactionDataStore {

    private static Logger logger = LoggerFactory.getLogger(InMemoryTransactionDataStore.class);

    @Value("${afterpay.fraud-detection.ttl-hr}")
    int timeToLiveInHrs;            //delay

    @Autowired
    @Qualifier("jmsFraudDetectionQueueTemplate")
    private JmsTemplate jmsFraudDetectionQueueTemplate;

    private HashMap<String, Queue<DelayTransaction>> historyMap = new HashMap<>();

    public InMemoryTransactionDataStore() {}

    public InMemoryTransactionDataStore(int timeToLiveInHrs)
    {
        this.timeToLiveInHrs = timeToLiveInHrs;
    }

    public void add(Transaction transaction)
    {
        final String card_number = transaction.getCredit_card();
        Queue<DelayTransaction> history = historyMap.get(card_number);
        DelayTransaction delayTransaction = new DelayTransaction(transaction, timeToLiveInHrs);
        if (delayTransaction.isExpired())
        {
            logger.trace("transaction is already expired " + transaction.toString());
            return;
        }

        if (history==null)
        {
            Queue<DelayTransaction> queue = new LinkedList<>();
            queue.add(delayTransaction);
            historyMap.put(card_number, queue);
            history = historyMap.get(card_number);
        }
        else
        {
            //delete all transaction that is expired
            purgeExpired(history);
            history.add(delayTransaction);
        }

        if (history!=null)
        {
            logger.trace("publishing transactions on fraud-detection-queue for " + card_number);
            publish(card_number, history);
        }
        else logger.warn("nothing to publisher for new transaction " + card_number);
    }

    public Collection<Record> fetchAll(String cardNumber)
    {
        ArrayList<Record> transactions = new ArrayList<>();
        Queue<DelayTransaction> history = historyMap.get(cardNumber);

        if (history==null)
        {
            return transactions;
        }

        for (DelayTransaction trans: history)
        {
            Transaction temp = trans.getTransaction();
            logger.trace("adding transactioin to recordset " + temp.toString());
            transactions.add(new Record(temp.getTime(), temp.getAmount()));
        }

        return transactions;
    }

    private void publish(String cardNumber, Queue<DelayTransaction> history)
    {
        ArrayList<Record> transactions = new ArrayList<>();
        for (DelayTransaction trans: history)
        {
            Transaction temp = trans.getTransaction();
            logger.trace("adding transactioin to recordset " + temp.toString());
            transactions.add(new Record(temp.getTime(), temp.getAmount()));
        }

        if (jmsFraudDetectionQueueTemplate!=null)
        {
            jmsFraudDetectionQueueTemplate.convertAndSend(new Transactions(
                    cardNumber,
                    transactions
            ));
        }
        else
        {
            logger.warn("fraud detection publisher not registered!!");
        }
    }

    private void purgeExpired(Queue<DelayTransaction> history)
    {
        while (true)
        {
            DelayTransaction delayTransaction = history.peek();
            if (delayTransaction==null)
                break;
            if (delayTransaction.isExpired())
            {
                DelayTransaction deletedTransaction = history.poll();
                logger.debug("transaction deleted " + deletedTransaction.getTransaction().toString());
            }
            else break;
        }
    }

}
