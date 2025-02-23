package ws

import (
	"log"
	"net/http"
	"sync"

	"github.com/gorilla/websocket"
)

var (
	// Websocket upgrader.
	upgrader = websocket.Upgrader{
		CheckOrigin: func(r *http.Request) bool {
			// Allow all origins (CORS policy)
			return true
		},
	}

	// Shutdown channel for shutting down Kafka consumers and websocket
	// connections.
	Shutdown = make(chan struct{})

	// Mapping of websocket clients to booleans. The reason this is a mapping to
	// boolean values instead of a list is because it simplifies adding and
	// deleting connections. (If this were a list, we would need to append and
	// delete elements (O(n)) instead of simply adding and removing from the
	// mapping (O(1)).)
	Clients = make(map[*websocket.Conn]bool)

	// Mutex for synchronizing updates to clients mapping.
	ClientsMutex sync.Mutex
)

func HandleWebSocket(w http.ResponseWriter, r *http.Request) {
	// Upgrade HTTP connection to websocket connection
	conn, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Println("Error upgrading to websocket:", err)
		return
	}

	// Close the websocket connection when this function returns
	defer conn.Close()

	// Add the connection to the clients mapping
	ClientsMutex.Lock()
	Clients[conn] = true
	ClientsMutex.Unlock()

	log.Printf("New websocket connection: %s\n", conn.RemoteAddr())

	// Block until shutdown signal is received
	<-Shutdown

	log.Printf("Closing websocket connection: %s\n", conn.RemoteAddr())

	// Remove connection from clients mapping
	ClientsMutex.Lock()
	delete(Clients, conn)
	ClientsMutex.Unlock()
}

func StartWebsocketServer(server *http.Server, wg *sync.WaitGroup) {
	// Mark this goroutine as done when it finishes
	defer wg.Done()

	log.Println("Listening for WebSocket connections at ws://localhost:8080/ws ...")

	err := server.ListenAndServe()
	if err != nil && err != http.ErrServerClosed {
		log.Fatalf("HTTP Server Error: %v", err)
	}
}

// func main() {
// 	var wg sync.WaitGroup

// 	// Create new HTTP server that handles websocket connections
// 	server := &http.Server{Addr: ":8080"}
// 	http.HandleFunc("/ws", handleWebSocket)

// 	// Make a channel that holds OS signals and send interrupt (Ctrl+C) signals
// 	// to that channel
// 	stop_channel := make(chan os.Signal, 1)
// 	signal.Notify(stop_channel, os.Interrupt)

// 	// Start the Kafka consumer in a goroutine
// 	wg.Add(1)
// 	go kafka.StartKafkaConsumer(&wg, &clientsMutex, &clients, shutdown)

// 	// Start the websocket server in a goroutine
// 	wg.Add(1)
// 	go startWebsocketServer(server, &wg)

// 	// Block until an interrupt is received in the stop channel
// 	<-stop_channel

// 	log.Println("Shutting down websocket server...")

// 	// Close the shutdown channel, which will send a message to all goroutines
// 	// listening on the channel
// 	close(shutdown)

// 	// Close all websocket connections
// 	clientsMutex.Lock()
// 	for conn := range clients {
// 		conn.Close()
// 	}
// 	clientsMutex.Unlock()

// 	// Shutdown websocket server
// 	err := server.Close()
// 	if err != nil {
// 		log.Fatalf("Websocket server shutdown failed: %v", err)
// 	}

// 	// Wait for all goroutines to finish
// 	wg.Wait()

// 	log.Println("Websocket server shutdown")
// }
