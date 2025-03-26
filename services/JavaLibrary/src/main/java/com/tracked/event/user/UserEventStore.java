package com.tracked.event.user;

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
@ConditionalOnProperty(name = "tracked.user-event-store.enabled", havingValue = "true")
public class UserEventStore {

    private static final Logger logger = LoggerFactory.getLogger(UserEventStore.class);

    private final Map<Integer, UserEvent> userDatabase = new EventCache<>(1000);

    @KafkaListener(topics = TrackedKafkaTopic.USER_TOPIC, groupId = TrackedKafkaGroupId.USER_GROUP)
    public void listen(ConsumerRecord<Integer, UserEvent> record, Acknowledgment ack) {
        logger.info("Received user event: {}", record.value().getEmail());
        this.userDatabase.put(record.key(), record.value());

        // Do not ack!
        // We don't want the consumer's offset to update because we always want
        // to replay events from the beginning.
    }

    public UserEvent getUserEvent(Integer userId) {
        return userDatabase.get(userId);
    }

    public Map<Integer, UserEvent> getAllUserEvents() {
        return userDatabase;
    }

}
