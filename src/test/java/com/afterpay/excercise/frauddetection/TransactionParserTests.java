package com.afterpay.excercise.frauddetection;

import com.afterpay.excercise.frauddetection.exceptions.InvalidTransactionFormatException;
import com.afterpay.excercise.frauddetection.model.Transaction;
import com.afterpay.excercise.frauddetection.parser.TransactionParserImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
public class TransactionParserTests {

    @Test
    public void testTransactionStringFormat() throws InvalidTransactionFormatException
    {
        TransactionParserImpl transactionParser = new TransactionParserImpl();
        String transStr = "10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.00";
        Transaction transaction = transactionParser.parse(transStr);

        Assert.assertEquals(transStr, transaction.toStringFormat());
    }

    @Test
    public void testValidParse() throws InvalidTransactionFormatException
    {
        TransactionParserImpl transactionParser = new TransactionParserImpl();
        Transaction transaction = transactionParser.parse("10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.00");

        Assert.assertNotNull("transaction should be not null", transaction);
        Assert.assertEquals("credit card number not matching", "10d7ce2f43e35fa57d1bbf8b1e2", transaction.getCredit_card());
        Assert.assertEquals("transaction amount parsed incorrectly", 10.00, transaction.getAmount(), 0.0001);

        LocalDateTime dateTime = transaction.getTime();

        //verifying date
        Assert.assertEquals("year is not correct", 2014, dateTime.getYear());
        Assert.assertEquals("month is not correct", 04, dateTime.getMonthValue());
        Assert.assertEquals("date of month is not correct", 29, dateTime.getDayOfMonth());

        //verifying time
        Assert.assertEquals(13, dateTime.getHour());
        Assert.assertEquals(15, dateTime.getMinute());
        Assert.assertEquals(54, dateTime.getSecond());
    }

    @Test(expected = InvalidTransactionFormatException.class)
    public void testEmptyCardNumber() throws InvalidTransactionFormatException
    {
        TransactionParserImpl transactionParser = new TransactionParserImpl();
        Transaction transaction = transactionParser.parse(", 2014-04-29T13:15:54, 10.00");
    }

    @Test(expected = InvalidTransactionFormatException.class)
    public void testSpaceFormat() throws InvalidTransactionFormatException
    {
        TransactionParserImpl transactionParser = new TransactionParserImpl();
        Transaction transaction = transactionParser.parse("10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54,10.00");
    }

    @Test(expected = InvalidTransactionFormatException.class)
    public void testCardNumberLength() throws InvalidTransactionFormatException
    {
        TransactionParserImpl transactionParser = new TransactionParserImpl();
        Transaction transaction = transactionParser.parse("10d7ce2f43e35fa57d1bbf8b1e, 2014-04-29T13:15:54, 10.00");
    }

    @Test(expected = InvalidTransactionFormatException.class)
    public void testNegativeAmount() throws InvalidTransactionFormatException
    {
        TransactionParserImpl transactionParser = new TransactionParserImpl();
        Transaction transaction = transactionParser.parse("10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, -10.00");
    }

    @Test(expected = InvalidTransactionFormatException.class)
    public void testInvalidTimeFormat() throws InvalidTransactionFormatException
    {
        TransactionParserImpl transactionParser = new TransactionParserImpl();
        Transaction transaction = transactionParser.parse("10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:, 10.00");
    }

}
