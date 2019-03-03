package com.afterpay.excercise.frauddetection;

import com.afterpay.excercise.frauddetection.model.Transaction;
import com.afterpay.excercise.frauddetection.model.Transactions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FraudDetectionApplicationTests {
	private int testCases = 0;

	String cardNumber1 = "10d7ce2f43e35fa57d1bbf8b1e2";
	String cardNumber2 = "20d7ce2f43e35fa57d1bbf8b1e2";
	String cardNumber3 = "30d7ce2f43e35fa57d1bbf8b1e2";
	String cardNumber4 = "40d7ce2f43e35fa57d1bbf8b1e2";

	private final double threshold = 0.0000001;

	@Autowired @Qualifier(value = "jmsTransactionPubishTemplate")
	private JmsTemplate jmsTransactionPubishTemplate;

	@JmsListener(destination = "${afterpay.fraud-notification-topic}"
			, containerFactory = "jmsFraudNotificationContainerFactory")
	private void listen(Transactions transactions)
	{
		if (transactions.getCardNumber().equals(cardNumber1))
		{
			Assert.assertEquals(3, transactions.getTransactions().get(0).getAmount(), threshold);
			Assert.assertEquals(4, transactions.getTransactions().get(1).getAmount(), threshold);
			Assert.assertEquals(14, transactions.getTransactions().get(2).getAmount(), threshold);
			testCases++;
		}
		else if (transactions.getCardNumber().equals(cardNumber2))
		{
			Assert.assertEquals(10, transactions.getTransactions().get(0).getAmount(), threshold);
			Assert.assertEquals(10, transactions.getTransactions().get(1).getAmount(), threshold);
			Assert.assertEquals(1, transactions.getTransactions().get(2).getAmount(), threshold);
			testCases++;
		}
		else if (transactions.getCardNumber().equals(cardNumber3))
		{
			Assert.assertEquals(21, transactions.getTransactions().get(0).getAmount(), threshold);
			testCases++;
		}
		else if (transactions.getCardNumber().equals(cardNumber4))
		{
			System.out.println("Must not execute this part");
			testCases++;
			Assert.assertTrue(false);
		}
		else Assert.assertTrue(false);
	}



	@Test
	public void testIntegration() throws Exception
	{


		LocalDateTime start = LocalDateTime.now().minusHours(24);

		//transactions on cardNumber1
		jmsTransactionPubishTemplate.convertAndSend(new Transaction(cardNumber1,
				start,
				10.00));
		jmsTransactionPubishTemplate.convertAndSend(new Transaction(cardNumber1,
				start.plusMinutes(2),
				3.00));
		jmsTransactionPubishTemplate.convertAndSend(new Transaction(cardNumber1,
				start.plusMinutes(3),
				4.00));
		jmsTransactionPubishTemplate.convertAndSend(new Transaction(cardNumber1,
				start.plusMinutes(4),
				14.00));

		//transactions on cardNumber2
		jmsTransactionPubishTemplate.convertAndSend(new Transaction(cardNumber2,
				start.plusHours(1),
				10.00));
		jmsTransactionPubishTemplate.convertAndSend(new Transaction(cardNumber2,
				start.plusHours(2),
				10.00));
		jmsTransactionPubishTemplate.convertAndSend(new Transaction(cardNumber2,
				start.plusHours(3),
				1.00));


		//transactions on cardNumber3
		jmsTransactionPubishTemplate.convertAndSend(new Transaction(cardNumber3,
				start,
				25.00));
		jmsTransactionPubishTemplate.convertAndSend(new Transaction(cardNumber3,
				LocalDateTime.now(),
				21.00));

		//transactions on cardNumber4 all good transactions
		jmsTransactionPubishTemplate.convertAndSend(new Transaction(cardNumber4,
				start.plusMinutes(5),
				5.00));
		jmsTransactionPubishTemplate.convertAndSend(new Transaction(cardNumber4,
				start.plusHours(5),
				5.00));
		jmsTransactionPubishTemplate.convertAndSend(new Transaction(cardNumber4,
				LocalDateTime.now().minusHours(5),
				5.00));
		jmsTransactionPubishTemplate.convertAndSend(new Transaction(cardNumber4,
				LocalDateTime.now().minusMinutes(5),
				5.00));

		//To fail this test uncomment statement below
//		jmsTransactionPubishTemplate.convertAndSend(new Transaction(cardNumber4,
//				LocalDateTime.now().minusMinutes(2),
//				1.00));


		Thread.sleep(10000);


		Assert.assertEquals(3, testCases);
	}

}

