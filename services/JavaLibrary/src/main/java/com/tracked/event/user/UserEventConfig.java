package com.tracked.event.user;

import com.tracked.event.common.CommonKafkaConfigs;
import com.tracked.kafka.config.TrackedKafkaGroupId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@ConditionalOnProperty(name = "tracked.user-event-store.enabled", havingValue = "true")
public class UserEventConfig {

    private static final Logger logger = LoggerFactory.getLogger(UserEventConfig.class);

    @Value("${tracked.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private CommonKafkaConfigs<Integer, UserEvent> commonKafkaConfigs = null;

    private CommonKafkaConfigs<Integer, UserEvent> commonKafkaConfigs() {
        if (this.commonKafkaConfigs == null) {
            this.commonKafkaConfigs = new CommonKafkaConfigs<>(
                this.bootstrapServers,
                TrackedKafkaGroupId.USER_GROUP
            );
        }
        return this.commonKafkaConfigs;
    }

    @Bean("userKafkaConsumerFactory")
    public ConsumerFactory<Integer, UserEvent> consumerFactory() {
        logger.info("Creating user event consumer factory");
        return this.commonKafkaConfigs().consumerFactory();
    }

    @Bean("userKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<Integer, UserEvent> kafkaListenerContainerFactory() {
        logger.info("Creating user event listener container factory");
        return this.commonKafkaConfigs().kafkaListenerContainerFactory();
    }

    @Bean("userKafkaProducerFactory")
    public ProducerFactory<Integer, UserEvent> producerFactory() {
        logger.info("Creating user event producer factory");
        return this.commonKafkaConfigs().producerFactory();
    }

    @Bean("userKafkaTemplate")
    public KafkaTemplate<Integer, UserEvent> kafkaTemplate() {
        logger.info("Creating user event kafka template");
        return this.commonKafkaConfigs().kafkaTemplate();
    }

}
