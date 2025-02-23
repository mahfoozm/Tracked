package main

import (
	"log"
	"net/http"
	"notification-service/kafka"
	"notification-service/ws"
	"os"
	"os/signal"
	"sync"
)

func main() {
	var wg sync.WaitGroup

	// Create new HTTP server that handles websocket connections
	server := &http.Server{Addr: ":8080"}
	http.HandleFunc("/ws", ws.HandleWebSocket)

	// Make a channel that holds OS signals and send interrupt (Ctrl+C) signals
	// to that channel
	stop_channel := make(chan os.Signal, 1)
	signal.Notify(stop_channel, os.Interrupt)

	// Start the Kafka consumer in a goroutine
	wg.Add(1)
	go kafka.StartKafkaConsumer(&wg, &ws.ClientsMutex, &ws.Clients, ws.Shutdown)

	// Start the websocket server in a goroutine
	wg.Add(1)
	go ws.StartWebsocketServer(server, &wg)

	// Block until an interrupt is received in the stop channel
	<-stop_channel

	log.Println("Shutting down websocket server...")

	// Close the shutdown channel, which will send a message to all goroutines
	// listening on the channel
	close(ws.Shutdown)

	// Close all websocket connections
	ws.ClientsMutex.Lock()
	for conn := range ws.Clients {
		conn.Close()
	}
	ws.ClientsMutex.Unlock()

	// Shutdown websocket server
	err := server.Close()
	if err != nil {
		log.Fatalf("Websocket server shutdown failed: %v", err)
	}

	// Wait for all goroutines to finish
	wg.Wait()

	log.Println("Websocket server shutdown")
}
