package com.afterpay.excercise.frauddetection.jms;


import com.afterpay.excercise.frauddetection.exceptions.InvalidTransactionFormatException;
import com.afterpay.excercise.frauddetection.parser.TransactionParserImpl;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.*;

@Configuration
public class JMSConfiguration {

    @Value("${activemq.broker-url}")
    private String brokerUrl;

    @Value("${afterpay.transaction-queue}")
    private String transactionQueue;

    @Value("${afterpay.fraud-notification-topic}")
    private String fraudNotificationTopic;

    @Value("${afterpay.fraud-detection-queue}")
    private String fraudDetectionQueue;

    @Bean
    public Topic fraudNotificationTopic()
    {
        return new ActiveMQTopic(fraudNotificationTopic);
    }

    @Bean
    public Queue transactionQueue()
    {
        return new ActiveMQQueue(transactionQueue);
    }

    @Bean
    public Queue fraudDetectionQueue()
    {
        return new ActiveMQQueue(fraudDetectionQueue);
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory()
    {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(brokerUrl);
        return factory;
    }


    private MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }


    @Bean
    public JmsTemplate jmsTransactionPubishTemplate(
            @Qualifier("jmsTransactionMessageConverter")
                    MessageConverter jmsTransactionMessageConverter)
    {
        JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory());
        jmsTemplate.setDefaultDestinationName(transactionQueue);
        jmsTemplate.setMessageConverter(jmsTransactionMessageConverter);
        return jmsTemplate;
    }



    @Bean
    public JmsTemplate jmsFraudNotificationTemplate()
    {
        JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory());
        jmsTemplate.setDefaultDestinationName(fraudNotificationTopic);
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        return jmsTemplate;
    }


    @Bean
    public JmsTemplate jmsFraudDetectionQueueTemplate()
    {
        JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory());
        jmsTemplate.setDefaultDestinationName(fraudDetectionQueue);
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        return jmsTemplate;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsTransactionContainerFactory(ConnectionFactory connectionFactory,
                     @Qualifier("jmsTransactionMessageConverter") MessageConverter jmsTransactionMessageConverter)
    {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setMessageConverter(jmsTransactionMessageConverter);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsFraudDetectionContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsFraudNotificationContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
}
