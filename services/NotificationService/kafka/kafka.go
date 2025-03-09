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
}

// StartKafkaConsumer starts the Kafka consumer that listens for messages on the
// specified Kafka topic and broadcasts them to WebSocket clients. It shuts down
// gracefully when a signal is received on the shutdown channel (kc.Shutdown).
func (kc *KafkaConsumer) StartKafkaConsumer(wg *sync.WaitGroup) {
	// Mark this goroutine as done when it finishes
	defer wg.Done()

	// Configure consumer
	config := sarama.NewConfig()
	config.Consumer.Return.Errors = true

	// Create a Kafka consumer
	client, err := sarama.NewConsumer([]string{broker}, config)
	if err != nil {
		log.Fatalf("Error creating consumer: %v", err)
	}

	// Close the Kafka consumer after this function returns
	defer client.Close()

	// Start consuming from Kafka partition
	partitionConsumer, err := client.ConsumePartition(topic, 0, sarama.OffsetNewest)
	if err != nil {
		log.Fatalf("Error creating partition consumer: %v", err)
	}

	// Close the Kafka partition consumer after this function returns
	defer partitionConsumer.Close()

	log.Println("Kafka Consumer started. Waiting for messages...")

	// Wait for either an event from Kafka or a shutdown signal
	for {
		select {
		// Kafka event
		case msg := <-partitionConsumer.Messages():
			message := string(msg.Value)
			log.Printf("Received message: %s\n", message)

			// Parse the Kafka message
			var kafkaMsg KafkaTestMessage
			err := json.Unmarshal(msg.Value, &kafkaMsg)
			if err != nil {
				log.Printf("Error unmarshalling Kafka message: %v", err)
				continue
			}

			// Send message to connections
			kc.Cm.BroadcastMessage(message)

		// Shutdown signal
		case <-kc.Shutdown:
			log.Println("Shutting down Kafka consumer...")
			return
		}
	}
}
