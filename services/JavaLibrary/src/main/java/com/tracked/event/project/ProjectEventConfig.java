package com.tracked.event.project;

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
@ConditionalOnProperty(name = "tracked.project-event-store.enabled", havingValue = "true")
public class ProjectEventConfig {

    private static final Logger logger = LoggerFactory.getLogger(ProjectEventConfig.class);

    @Value("${tracked.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private CommonKafkaConfigs<Integer, ProjectEvent> commonKafkaConfigs = null;

    private CommonKafkaConfigs<Integer, ProjectEvent> commonKafkaConfigs() {
        if (this.commonKafkaConfigs == null) {
            this.commonKafkaConfigs = new CommonKafkaConfigs<>(
                this.bootstrapServers,
                TrackedKafkaGroupId.PROJECT_GROUP
            );
        }
        return this.commonKafkaConfigs;
    }

    @Bean
    public ConsumerFactory<Integer, ProjectEvent> consumerFactory() {
        logger.info("Creating project event consumer factory");
        return this.commonKafkaConfigs().consumerFactory();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, ProjectEvent> kafkaListenerContainerFactory() {
        logger.info("Creating project event listener container factory");
        return this.commonKafkaConfigs().kafkaListenerContainerFactory();
    }

    @Bean
    public ProducerFactory<Integer, ProjectEvent> producerFactory() {
        logger.info("Creating project event producer factory");
        return this.commonKafkaConfigs().producerFactory();
    }

    @Bean
    public KafkaTemplate<Integer, ProjectEvent> kafkaTemplate() {
        logger.info("Creating project event kafka template");
        return this.commonKafkaConfigs().kafkaTemplate();
    }

}
