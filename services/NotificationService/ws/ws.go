// Package ws provides WebSocket handling for the notification-service.
// It includes an HTTP server that upgrades connections to WebSockets, manages
// connected clients, and gracefully shuts down on request.
package ws

import (
	"log"
	"net/http"
	"notification-service/client"
	"sync"

	"github.com/gorilla/websocket"
)

var (
	// upgrader is a WebSocket upgrader that allows all origins (CORS policy).
	upgrader = websocket.Upgrader{
		CheckOrigin: func(r *http.Request) bool {
			// Allow all origins (CORS policy)
			return true
		},
	}
)

// WebSocketHandler manages WebSocket connections and server shutdowns.
type WebSocketHandler struct {
	Cm       *client.ClientManager // Cm handles active WebSocket clients.
	Shutdown chan struct{}         // Shutdown channel to signal termination.
}

// HandleWebSocket upgrades an HTTP connection to a WebSocket connection and
// registers it in the ClientManager. This should be used with the http package
// like this: http.HandleFunc("/ws", wsh.HandleWebSocket)
func (wsh *WebSocketHandler) HandleWebSocket(w http.ResponseWriter, r *http.Request) {
	// Upgrade HTTP connection to websocket connection
	conn, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Println("Error upgrading to websocket:", err)
		return
	}

	// Add the connection to the connection manager
	wsh.Cm.AddClient(conn)

	log.Printf("New websocket connection: %s\n", conn.RemoteAddr())
}

// listenForShutdown waits for a shutdown signal (which will come from the
// wsh.Shutdown channel), then tells that client manager (wsh.Cm) to close all
// WebSocket connections, then shuts down the WebSocket server.
func (wsh *WebSocketHandler) listenForShutdown(wg *sync.WaitGroup, server *http.Server) {
	// Mark this goroutine as done when it finishes
	defer wg.Done()

	// Block until the shutdown signal is received
	<-wsh.Shutdown

	// Close all connections in the connection manager
	wsh.Cm.CloseAllConnections()

	log.Println("Stopping websocket server")

	// Shutdown websocket server
	err := server.Close()
	if err != nil {
		log.Fatalf("Websocket server shutdown failed: %v", err)
	}
}

// StartWebsocketServer starts an HTTP server that listens for WebSocket
// connections and handles shutdown gracefully.
func (wsh *WebSocketHandler) StartWebsocketServer(wg *sync.WaitGroup) {
	// Mark this goroutine as done when it finishes
	defer wg.Done()

	// Create new HTTP server that handles websocket connections
	server := &http.Server{Addr: ":8080"}

	// Listen for shutdown signals in a goroutine
	wg.Add(1)
	go wsh.listenForShutdown(wg, server)

	// Handler for websocket connections
	http.HandleFunc("/ws", wsh.HandleWebSocket)

	log.Println("Listening for WebSocket connections at ws://localhost:8080/ws ...")

	// Start the server
	err := server.ListenAndServe()
	if err != nil && err != http.ErrServerClosed {
		log.Fatalf("HTTP Server Error: %v", err)
	}
}
