package com.tracked.event.project;


import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.tracked.cache.EventCache;
import com.tracked.kafka.config.TrackedKafkaGroupId;
import com.tracked.kafka.config.TrackedKafkaTopic;

@Component
@ConditionalOnProperty(name = "tracked.project-event-store.enabled", havingValue = "true")
public class ProjectEventStore {

    private static final Logger logger = LoggerFactory.getLogger(ProjectEventStore.class);

    private final Map<Integer, ProjectEvent> projectDatabase = new EventCache<>(1000);

    @KafkaListener(
        topics = TrackedKafkaTopic.PROJECT_TOPIC,
        groupId = "${tracked.project-event-store.group-id:" + TrackedKafkaGroupId.PROJECT_GROUP + "}",
        containerFactory = "projectKafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<Integer, ProjectEvent> record, Acknowledgment ack) {
        logger.info("Received project event: {}", record.value().getId());
        this.projectDatabase.put(record.key(), record.value());

        // Do not ack!
        // We don't want the consumer's offset to update because we always want
        // to replay events from the beginning.
    }

    public ProjectEvent getProjectEvent(Integer projectId) {
        return projectDatabase.get(projectId);
    }

    public Map<Integer, ProjectEvent> getAllProjectEvents() {
        return projectDatabase;
    }

}
