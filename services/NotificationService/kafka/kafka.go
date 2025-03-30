// Package kafka provides a Kafka consumer for handling incoming messages from
// Kafka and broadcasting them to connected WebSocket clients.
package kafka

import (
	"encoding/json"
	"log"
	"notification-service/client"
	"sync"

	"github.com/IBM/sarama"
)

const (
	broker = "localhost:9092" // Kafka broker address.
)

// KafkaConsumer manages the consumption of messages from Kafka and broadcasting
// them to WebSocket clients.
type KafkaConsumer[T client.ValidKafkaMessageTypes] struct {
	Cm       *client.ClientManager // ClientManager to broadcast messages to WebSocket clients.
	Shutdown chan struct{}         // Channel to signal shutdown of the Kafka consumer.
}

func (kc *KafkaConsumer[T]) StartKafkaConsumer(wg *sync.WaitGroup, topic string) {
	defer wg.Done()
	config := sarama.NewConfig()
	config.Consumer.Return.Errors = true

	kafka_client, err := sarama.NewConsumer([]string{broker}, config)
	if err != nil {
		log.Fatalf("Error creating %s Kafka consumer: %v", topic, err)
	}

	defer kafka_client.Close()

	partitionConsumer, err := kafka_client.ConsumePartition(topic, 0, sarama.OffsetOldest)
	if err != nil {
		log.Fatalf("Error creating %s partition consumer: %v", topic, err)
	}

	defer partitionConsumer.Close()

	log.Printf("%s Kafka consumer started. Waiting for messages...\n", topic)

	for {
		select {
		case msg := <-partitionConsumer.Messages():
			message := string(msg.Value)
			log.Printf("%s Kafka consumer received message: %s\n", topic, message)

			var kafkaMsg T
			err := json.Unmarshal(msg.Value, &kafkaMsg)
			if err != nil {
				log.Printf("%s Kafka consumer failed to unmarshal message: %v. Message: %s", topic, err, message)
				continue
			}

			switch v := any(kafkaMsg).(type) {
			case client.UserEvent:
				user_event := client.UserEvent(v)
				kc.Cm.UpdateUserEventStore(&user_event)
			case client.ProjectEvent:
				project_event := client.ProjectEvent(v)
				kc.Cm.UpdateProjectEventStore(&project_event)
			case client.TaskEvent:
				task_event := client.TaskEvent(v)
				kc.Cm.UpdateTaskEventStore(&task_event)
				kc.Cm.BroadcastTaskMessage(&task_event, &message)
			default:
				log.Printf("Unknown event type: %T", v)
			}

		case <-kc.Shutdown:
			log.Printf("Shutting down %s Kafka consumer...\n", topic)
			return
		}
	}
}
