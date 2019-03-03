

1- class FraudDetectionApplicationTests contains Integration Tests against which the code is tested. It publishes
   transactions on transactions.queue and listens to fraud.notification topic for any fraud published by the service.

2- Application is divided into three main parts.

    2-1 ITransactionListener listens for transaction and implemented using Jms/ActiveMq and listens at
        transaction.queue. which then maintains ITransactionDataStore. which is implemented as in memory datastore and
        mark transaction with configurable TTL (time to live).

    2-2 InMemoryTransactionDataStore also publishes entire transactions against card-number to fraud-detection.queue on
        arrival of new transactions. and purge expired transactions.

    2-3 FraudDetectionService implements the summation logic and also expect configurable fraud threshold amount. After
        finding transactions to exceed threshold it also publishes the information on fraud.notification topic.

3- Unit test cases is implemented to verify DataStore implementation and expiration logic, and parsing of transactions.