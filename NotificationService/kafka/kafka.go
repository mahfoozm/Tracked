package kafka

import (
	"log"
	"sync"

	"github.com/IBM/sarama"
	"github.com/gorilla/websocket"
)

const (
	broker = "localhost:9092"
	topic  = "test_topic"
)

func sendMessageToClients(message *string, clientsMutex *sync.Mutex, clients *map[*websocket.Conn]bool) {
	clientsMutex.Lock()

	// Send the message to each connection in the client mapping
	for conn := range *clients {
		// Send the message to the connection
		err := conn.WriteMessage(websocket.TextMessage, []byte(*message))

		// If there was an error sending the websocket message, close
		// the connection and remove it from the client mapping
		if err != nil {
			log.Println("Error sending to websocket:", err)
			conn.Close()
			delete(*clients, conn)
		}
	}

	clientsMutex.Unlock()
}

func StartKafkaConsumer(wg *sync.WaitGroup, clientsMutex *sync.Mutex, clients *map[*websocket.Conn]bool, shutdown chan struct{}) {
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

			sendMessageToClients(&message, clientsMutex, clients)

		// Shutdown signal
		case <-shutdown:
			log.Println("Shutting down Kafka consumer...")
			return
		}
	}
}
