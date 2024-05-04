package com.sekolah.user.configuration;

import com.sekolah.user.event.IncomingMessageEvent;
import com.sekolah.user.event.OutgoingMessageEvent;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    // Kafka Producer Configuration
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // Kafka Producer Factory
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.29.102.233:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // Add any other producer properties here

        return new DefaultKafkaProducerFactory<>(props);
    }

    // Kafka Consumer Configuration
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    // Kafka Consumer Factory
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.29.102.233:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "sekolah");
        // Add any other consumer properties here

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new StringDeserializer());
    }

    // Kafka Admin Configuration
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> adminConfigs = new HashMap<>();
        adminConfigs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "172.29.102.233:9092");

        return new KafkaAdmin(adminConfigs);
    }

    // UserRequestProducer Bean
    @Bean
    public OutgoingMessageEvent outgoingMessageProducer() {
        return new OutgoingMessageEvent();
    }

    // UserRequestConsumer Bean
    @Bean
    public IncomingMessageEvent incomingMessageConsumer() {
        return new IncomingMessageEvent();
    }
}
