package com.afterpay.excercise.frauddetection.jms;

import com.afterpay.excercise.frauddetection.model.Transactions;
import com.afterpay.excercise.frauddetection.model.IFraudPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsFraudPublisher implements IFraudPublisher {

    private static Logger logger = LoggerFactory.getLogger(JmsFraudPublisher.class);

    @Autowired
    @Qualifier("jmsFraudNotificationTemplate")
    private JmsTemplate jmsFraudNotificationTemplate;



    public void publish(Transactions transactionsRecord)
    {
        logger.info("publishing fraud notification " + transactionsRecord.toString());
        jmsFraudNotificationTemplate.convertAndSend(transactionsRecord);
    }
}
