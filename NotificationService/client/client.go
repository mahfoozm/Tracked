// Package client provides a client manager to handle WebSocket connections.
// It allows adding new clients, broadcasting messages, and closing all
// connections, all with synchronization mechanisms for thread safety.
package client

import (
	"log"
	"sync"

	"github.com/gorilla/websocket"
)

// ClientManager manages active WebSocket connections.
type ClientManager struct {
	// Clients stores active WebSocket connections. This is a mapping from
	// connections to booleans instead of a list because the add and delete
	// functionality is O(1), whereas with a list it is O(n).
	Clients map[*websocket.Conn]bool
	// Mutex to protect access to the Clients mapping. This mutex should be
	// locked whenever reading from or writing to Clients.
	ClientMutex sync.Mutex
}

// NewClientManager creates and returns a new instance of ClientManager.
func NewClientManager() *ClientManager {
	return &ClientManager{Clients: make(map[*websocket.Conn]bool)}
}

// AddClient registers a new WebSocket connection (conn) in the client manager.
func (cm *ClientManager) AddClient(conn *websocket.Conn) {
	cm.ClientMutex.Lock()
	defer cm.ClientMutex.Unlock()

	cm.Clients[conn] = true
	log.Printf("New client connection: %s\n", conn.RemoteAddr())
}

// CloseAllConnections closes and removes all active WebSocket connections from
// the Clients mapping.
func (cm *ClientManager) CloseAllConnections() {
	cm.ClientMutex.Lock()
	defer cm.ClientMutex.Unlock()

	for conn := range cm.Clients {
		conn.Close()             // Close the connection
		delete(cm.Clients, conn) // Delete the connection from the Clients mapping

		log.Printf("Removed client connection: %s\n", conn.RemoteAddr())
	}
}

// BroadcastMessage sends a message to all connected WebSocket clients. If a
// client fails to receive the message, its connection is closed and it is
// removed from the Clients mapping.
func (cm *ClientManager) BroadcastMessage(message string) {
	cm.ClientMutex.Lock()
	defer cm.ClientMutex.Unlock()

	// Send the message to each connection in the client mapping
	for conn := range cm.Clients {
		// Send the message to the connection
		err := conn.WriteMessage(websocket.TextMessage, []byte(message))

		// If there was an error sending the websocket message, close
		// the client's connection and remove it from the client mapping
		if err != nil {
			log.Printf("Error sending to websocket: %v. Removing client %s", err, conn.RemoteAddr())
			conn.Close()
			delete(cm.Clients, conn)
		}
	}
}
