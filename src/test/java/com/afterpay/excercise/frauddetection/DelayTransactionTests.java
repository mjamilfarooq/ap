package com.afterpay.excercise.frauddetection;

import com.afterpay.excercise.frauddetection.datastore.DelayTransaction;
import com.afterpay.excercise.frauddetection.model.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.LocalDateTime;


@RunWith(SpringRunner.class)
public class DelayTransactionTests {



    @Test
    public void testExpiration()
    {
        String cardNumber = "10d7ce2f43e35fa57d1bbf8b1e2";
        Transaction expiredTransaction = new Transaction(cardNumber, LocalDateTime.now().minusHours(24).minusSeconds(1), 10.00);
        DelayTransaction delayTransaction = new DelayTransaction(expiredTransaction, 24);
        Assert.assertTrue("transaction has not expired correctly", delayTransaction.isExpired());
    }

    @Test
    public void testNotExpiration()
    {
        String cardNumber = "10d7ce2f43e35fa57d1bbf8b1e2";
        Transaction transaction = new Transaction(cardNumber, LocalDateTime.now(), 10.00);
        DelayTransaction delayTransaction = new DelayTransaction(transaction, 24);

        Assert.assertFalse("transaction has not expired ", delayTransaction.isExpired());
    }

}
