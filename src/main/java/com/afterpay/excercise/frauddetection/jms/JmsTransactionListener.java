package com.afterpay.excercise.frauddetection.jms;

import com.afterpay.excercise.frauddetection.model.ITransactionDataStore;
import com.afterpay.excercise.frauddetection.model.ITransactionParser;
import com.afterpay.excercise.frauddetection.model.ITransactionListener;
import com.afterpay.excercise.frauddetection.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class JmsTransactionListener implements ITransactionListener {

    private static Logger logger = LoggerFactory.getLogger(JmsTransactionListener.class);

    @Autowired
    private ITransactionParser transactionParser;

    @Autowired
    private ITransactionDataStore transactionHistoryMap;

    @JmsListener(destination = "${afterpay.transaction-queue}", containerFactory = "jmsTransactionContainerFactory")
    public void listen(Transaction transaction)
    {
            logger.trace("new transaction " + transaction.toString());
            transactionHistoryMap.add(transaction);
    }

}
