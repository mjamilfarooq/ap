package com.afterpay.excercise.frauddetection.detection;

import com.afterpay.excercise.frauddetection.model.IFraudPublisher;
import com.afterpay.excercise.frauddetection.model.Record;
import com.afterpay.excercise.frauddetection.model.Transactions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class FraudDetectorService {

    private static Logger logger = LoggerFactory.getLogger(FraudDetectorService.class);

    @Value("${afterpay.fraud-detection.threshold}")
    Double fraudDetectionThreshold;

    @Autowired
    private IFraudPublisher fraudPublisher;


    @JmsListener(destination = "${afterpay.fraud-detection-queue}", containerFactory = "jmsFraudDetectionContainerFactory")
    public void listen(Transactions transactions) {

        ArrayList<Record> records = transactions.getTransactions();

        Double sum = 0.0;

        for (Record record : records) {
            sum += record.getAmount();
        }

        if (sum > fraudDetectionThreshold)
        {
            logger.warn("fraud detected" + transactions.getCardNumber());
            fraudPublisher.publish(transactions);
        }
    }

}
