package com.afterpay.excercise.frauddetection.jms;

import com.afterpay.excercise.frauddetection.exceptions.InvalidTransactionFormatException;
import com.afterpay.excercise.frauddetection.model.ITransactionParser;
import com.afterpay.excercise.frauddetection.model.Transaction;
import com.afterpay.excercise.frauddetection.parser.TransactionParserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component
public class JmsTransactionMessageConverter implements MessageConverter {

    private static Logger logger = LoggerFactory.getLogger(JmsTransactionMessageConverter.class);


    @Autowired
    ITransactionParser transactionParser;

    @Override
    public Message toMessage(Object o, Session session) throws JMSException, MessageConversionException {
        Transaction transaction = (Transaction) o;
        TextMessage message = session.createTextMessage();
        message.setText(transaction.toStringFormat());
        return message;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        TextMessage received = (TextMessage)message;
        Transaction transaction = null;
        try
        {
            transaction = transactionParser.parse(received.getText());
        }
        catch(InvalidTransactionFormatException ex)
        {
            logger.warn(ex.getMessage());
        }

        return transaction;
    }
}
