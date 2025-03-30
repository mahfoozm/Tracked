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
	topic  = "test_topic"     // Kafka topic to consume messages from.
)

// KafkaConsumer manages the consumption of messages from Kafka and broadcasting
// them to WebSocket clients.
type KafkaConsumer struct {
	Cm       *client.ClientManager // ClientManager to broadcast messages to WebSocket clients.
	Shutdown chan struct{}         // Channel to signal shutdown of the Kafka consumer.
}

// KafkaTestMessage is the structure of Kafka messages in the "test_topic" topic.
type KafkaTestMessage struct {
	ID        int     `json:"id"`
	Message   string  `json:"content"`
	Timestamp float64 `json:"timestamp"`
	UserID   string     `json:"userID"`
}

// StartKafkaConsumer starts the Kafka consumer that listens for messages on the
// specified Kafka topic and broadcasts them to WebSocket clients. It shuts down
// gracefully when a signal is received on the shutdown channel (kc.Shutdown).
func (kc *KafkaConsumer) StartKafkaConsumer(wg *sync.WaitGroup) {
    defer wg.Done()

    config := sarama.NewConfig()
    config.Consumer.Return.Errors = true

    client, err := sarama.NewConsumer([]string{broker}, config)
    if err != nil {
        log.Fatalf("Error creating consumer: %v", err)
    }

    defer client.Close()

    partitionConsumer, err := client.ConsumePartition(topic, 0, sarama.OffsetNewest)
    if err != nil {
        log.Fatalf("Error creating partition consumer: %v", err)
    }

    defer partitionConsumer.Close()

    log.Println("Kafka Consumer started. Waiting for messages...")

    for {
        select {
        case msg := <-partitionConsumer.Messages():
            message := string(msg.Value)
            log.Printf("Received message: %s\n", message)

            var kafkaMsg KafkaTestMessage
            err := json.Unmarshal(msg.Value, &kafkaMsg)
            if err != nil {
                log.Printf("Error unmarshalling Kafka message: %v", err)
                continue
            }

            if kafkaMsg.UserID != "" {
                log.Printf("Sending notification to user: %s\n", kafkaMsg.UserID)
                kc.Cm.BroadcastUserMessage(kafkaMsg.UserID, message)
            } else {
                // If no user ID, broadcast to all (system message)
                log.Println("Broadcasting system notification to all users")
                kc.Cm.BroadcastMessage(message)
            }

        case <-kc.Shutdown:
            log.Println("Shutting down Kafka consumer...")
            return
        }
    }
}
