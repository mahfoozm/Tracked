package com.tracked.event.task;

import com.tracked.cache.EventCache;
import com.tracked.kafka.config.TrackedKafkaGroupId;
import com.tracked.kafka.config.TrackedKafkaTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnProperty(name = "tracked.task-event-store.enabled", havingValue = "true")
public class TaskEventStore {

    private static final Logger logger = LoggerFactory.getLogger(TaskEventStore.class);

    private final Map<Integer, TaskEvent> taskDatabase = new EventCache<>(1000);

    @KafkaListener(topics = TrackedKafkaTopic.TASK_TOPIC, groupId = TrackedKafkaGroupId.TASK_GROUP, containerFactory = "taskKafkaListenerContainerFactory")
    public void listen(ConsumerRecord<Integer, TaskEvent> record, Acknowledgment ack) {
        logger.info("Received task event: {}", record.value().getId());
        this.taskDatabase.put(record.key(), record.value());

        // Do not ack!
        // We don't want the consumer's offset to update because we always want
        // to replay events from the beginning.
    }

    public TaskEvent getTaskEvent(Integer taskId) {
        return taskDatabase.get(taskId);
    }

    public Map<Integer, TaskEvent> getAllTaskEvents() {
        return taskDatabase;
    }

}
