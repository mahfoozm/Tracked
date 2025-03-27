package com.tracked.event.task;

import com.tracked.event.common.CommonKafkaConfigs;
import com.tracked.kafka.config.TrackedKafkaGroupId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

@Configuration
@ConditionalOnProperty(name = "tracked.task-event-store.enabled", havingValue = "true")
public class TaskEventConfig {

    private static final Logger logger = LoggerFactory.getLogger(TaskEventConfig.class);

    @Value("${tracked.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private CommonKafkaConfigs<Integer, TaskEvent> commonKafkaConfigs = null;

    private CommonKafkaConfigs<Integer, TaskEvent> commonKafkaConfigs() {
        if (this.commonKafkaConfigs == null) {
            this.commonKafkaConfigs = new CommonKafkaConfigs<>(
                this.bootstrapServers,
                TrackedKafkaGroupId.TASK_GROUP
            );
        }
        return this.commonKafkaConfigs;
    }

    @Bean("taskKafkaConsumerFactory")
    public ConsumerFactory<Integer, TaskEvent> consumerFactory() {
        logger.info("Creating task event consumer factory");
        return this.commonKafkaConfigs().consumerFactory();
    }

    @Bean("taskKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<Integer, TaskEvent> kafkaListenerContainerFactory() {
        logger.info("Creating task event listener container factory");
        return this.commonKafkaConfigs().kafkaListenerContainerFactory();
    }

    @Bean("taskKafkaProducerFactory")
    public ProducerFactory<Integer, TaskEvent> producerFactory() {
        logger.info("Creating task event producer factory");
        return this.commonKafkaConfigs().producerFactory();
    }

    @Bean("taskKafkaTemplate")
    public KafkaTemplate<Integer, TaskEvent> kafkaTemplate() {
        logger.info("Creating task event kafka template");
        return this.commonKafkaConfigs().kafkaTemplate();
    }

}
