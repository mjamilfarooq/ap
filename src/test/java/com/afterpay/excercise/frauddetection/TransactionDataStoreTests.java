package com.afterpay.excercise.frauddetection;

import com.afterpay.excercise.frauddetection.datastore.InMemoryTransactionDataStore;
import com.afterpay.excercise.frauddetection.model.ITransactionDataStore;
import com.afterpay.excercise.frauddetection.model.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;



@RunWith(SpringRunner.class)
public class TransactionDataStoreTests {

    private ITransactionDataStore transactionDataStore;

    @Before
    public void prepare()
    {
        transactionDataStore = new InMemoryTransactionDataStore(24);
    }


    @Test
    public void testAdditionOfExpiredTransaction()
    {
        //"10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.00"

        String cardNumber = "10d7ce2f43e35fa57d1bbf8b1e2";
        transactionDataStore.add(new Transaction(cardNumber, LocalDateTime.now().minusHours(25), 10.00));
        transactionDataStore.add(new Transaction(cardNumber, LocalDateTime.now().minusHours(24).minusSeconds(60), 10.00));
        transactionDataStore.add(new Transaction(cardNumber, LocalDateTime.now().minusHours(23), 10.00));
        transactionDataStore.add(new Transaction(cardNumber, LocalDateTime.now().minusHours(22), 10.00));
        transactionDataStore.add(new Transaction(cardNumber, LocalDateTime.now().minusHours(21), 10.00));

        Assert.assertEquals(3, transactionDataStore.fetchAll(cardNumber).size());

    }

    @Test
    public void testExpiredValueRetention() throws Exception
    {
        String cardNumber = "10d7ce2f43e35fa57d1bbf8b1e2";
        transactionDataStore.add(new Transaction(cardNumber, LocalDateTime.now().minusHours(24).plusSeconds(1), 10.00));
        Assert.assertEquals(1, transactionDataStore.fetchAll(cardNumber).size());
        Thread.sleep(1000);
        Assert.assertEquals(1, transactionDataStore.fetchAll(cardNumber).size());
    }

    @Test
    public void testAdditionOfNewNotExpired() throws Exception
    {
        String cardNumber = "10d7ce2f43e35fa57d1bbf8b1e2";

        //soon to be expired transaction
        transactionDataStore.add(new Transaction(cardNumber, LocalDateTime.now().minusHours(24).plusSeconds(1), 10.00));
        transactionDataStore.add(new Transaction(cardNumber, LocalDateTime.now().minusHours(24).plusSeconds(2), 10.00));
        transactionDataStore.add(new Transaction(cardNumber, LocalDateTime.now().minusHours(24).plusSeconds(3), 10.00));
        transactionDataStore.add(new Transaction(cardNumber, LocalDateTime.now().minusHours(24).plusSeconds(4), 10.00));
        Assert.assertEquals(4, transactionDataStore.fetchAll(cardNumber).size());
        Thread.sleep(5000);

        //not expired transaction
        transactionDataStore.add(new Transaction(cardNumber, LocalDateTime.now(), 10.00));
        Assert.assertEquals(1, transactionDataStore.fetchAll(cardNumber).size());
    }

}
