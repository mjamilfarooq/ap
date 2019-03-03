package com.afterpay.excercise.frauddetection.parser;

import com.afterpay.excercise.frauddetection.exceptions.InvalidTransactionFormatException;
import com.afterpay.excercise.frauddetection.model.ITransactionParser;
import com.afterpay.excercise.frauddetection.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class TransactionParserImpl implements ITransactionParser {

    private static Logger logger = LoggerFactory.getLogger(TransactionParserImpl.class);

    private final String cardNumberPattern =  "([a-zA-Z0-9]{27})";
    private final String dateTimePattern = "([0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2})";
    private final String amountPattern = "([0-9]+.[0-9]{2})";

    private final String transactionRegex =
            "^" + cardNumberPattern + ", "
            + dateTimePattern + ", "
            + amountPattern + "$";

    private final Pattern transactionPattern = Pattern.compile(transactionRegex);

    public Transaction parse(String transStr) throws InvalidTransactionFormatException
    {
        Transaction transaction = null;

        try
        {

            Matcher matcher = transactionPattern.matcher(transStr);
            if (!matcher.matches() || matcher.groupCount() != 3 )
                throw new InvalidTransactionFormatException(transStr);



            String cardNumber = matcher.group(1);
            LocalDateTime dateTime = LocalDateTime.parse(matcher.group(2));
            double amount = Double.parseDouble(matcher.group(3));

            transaction = new Transaction(
                    cardNumber,
                    dateTime,
                    amount
            );
        }
        catch(Exception ex)
        {
            logger.warn(ex.getMessage());
            throw new InvalidTransactionFormatException(ex.getLocalizedMessage() + " " + transStr);
        }

        return transaction;
    }
}
