// Package main initializes and runs the WebSocket server and Kafka consumer.
// It listens for OS interrupt signals to gracefully shut down all running
// services.
package main

import (
	"log"
	"notification-service/client"
	"notification-service/kafka"
	"notification-service/ws"
	"os"
	"os/signal"
	"sync"
	"syscall"
	"time"
)

const (
	USER_TOPIC    = "user-topic"
	PROJECT_TOPIC = "project-topic"
	TASK_TOPIC    = "task-topic"
)

// shutdown is a shared channel used to signal all running services to stop.
var shutdown = make(chan struct{})

func main() {
	log.Println("Starting up")

	// WaitGroup to track and wait for goroutines to finish.
	var wg sync.WaitGroup

	// Consumer
	var wsh *ws.WebSocketHandler
	var user_kafka_consumer *kafka.KafkaConsumer[client.UserEvent]
	var project_kafka_consumer *kafka.KafkaConsumer[client.ProjectEvent]
	var task_kafka_consumer *kafka.KafkaConsumer[client.TaskEvent]

	cm := client.NewClientManager()

	wsh = &ws.WebSocketHandler{Cm: cm, Shutdown: shutdown}
	// Consumer
	user_kafka_consumer = &kafka.KafkaConsumer[client.UserEvent]{Cm: cm, Shutdown: shutdown}
	project_kafka_consumer = &kafka.KafkaConsumer[client.ProjectEvent]{Cm: cm, Shutdown: shutdown}
	task_kafka_consumer = &kafka.KafkaConsumer[client.TaskEvent]{Cm: cm, Shutdown: shutdown}

	// Make a channel that holds OS signals and send interrupt (Ctrl+C) signals
	// to that channel
	stop_channel := make(chan os.Signal, 1)
	signal.Notify(stop_channel, os.Interrupt, syscall.SIGTERM)

	// Start all Kafka consumers
	wg.Add(1)
	go user_kafka_consumer.StartKafkaConsumer(&wg, USER_TOPIC)
	wg.Add(1)
	go project_kafka_consumer.StartKafkaConsumer(&wg, PROJECT_TOPIC)
	wg.Add(1)
	go task_kafka_consumer.StartKafkaConsumer(&wg, TASK_TOPIC)

	// Start the websocket server in a goroutine
	wg.Add(1)
	go wsh.StartWebsocketServer(&wg)

	// Block until an interrupt is received in the stop channel
	<-stop_channel

	log.Println("Shutting down...")

	// Close the shutdown channel, which will send a message to all goroutines
	// listening on the channel
	close(shutdown)

	// Wait for all goroutines to finish
	wg.Wait()

	time.Sleep(time.Millisecond * 1000)

	log.Println("Shutdown complete")
}
