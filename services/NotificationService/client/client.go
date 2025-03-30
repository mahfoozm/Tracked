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
    // Clients maps WebSocket connections to user IDs
	Clients map[*websocket.Conn]string
	// Mutex to protect access to the Clients mapping. This mutex should be
	// locked whenever reading from or writing to Clients.
	ClientMutex sync.Mutex
}

// NewClientManager creates and returns a new instance of ClientManager.
func NewClientManager() *ClientManager {
    return &ClientManager{Clients: make(map[*websocket.Conn]string)}
}

// AddClient registers a new WebSocket connection (conn) in the client manager.
func (cm *ClientManager) AddClient(conn *websocket.Conn, userId string) {
    cm.ClientMutex.Lock()
    defer cm.ClientMutex.Unlock()

    cm.Clients[conn] = userId
    log.Printf("New client connection: %s for user: %s\n", conn.RemoteAddr(), userId)
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

// BroadcastUserMessage sends a message to specific user's WebSocket connections.
// If a client fails to receive the message, its connection is closed and removed.
func (cm *ClientManager) BroadcastUserMessage(userId string, message string) {
    cm.ClientMutex.Lock()
    defer cm.ClientMutex.Unlock()

    // Send the message to each connection matching the userId
    for conn, connUserId := range cm.Clients {
        // Only send to connections belonging to the specified user
        if connUserId == userId {
            err := conn.WriteMessage(websocket.TextMessage, []byte(message))

            if err != nil {
                log.Printf("Error sending to websocket: %v. Removing client %s", err, conn.RemoteAddr())
                conn.Close()
                delete(cm.Clients, conn)
            }
        }
    }
}
