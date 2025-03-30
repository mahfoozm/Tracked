// Package client provides a client manager to handle WebSocket connections.
// It allows adding new clients, broadcasting messages, and closing all
// connections, all with synchronization mechanisms for thread safety.
package client

import (
	"log"
	"sync"

	"notification-service/utils"

	"github.com/gorilla/websocket"
)

// type CustomDate struct {
// 	time.Time
// }

type TaskEvent struct {
	ID             int    `json:"id"`
	Name           string `json:"name"`
	ProjectID      int    `json:"projectId"`
	CreatorUserID  int    `json:"creatorUserId"`
	AssigneeUserID int    `json:"assigneeUserId"`
	Status         string `json:"status"`
	// StartDate      *CustomDate `json:"startDate"`
	// EndDate        *CustomDate `json:"endDate"`
	// CreatedAt      *CustomDate `json:"createdAt"`
	// UpdatedAt      *CustomDate `json:"updatedAt"`
}

type ProjectEvent struct {
	ID   int    `json:"id"`
	Name string `json:"name"`
	// CreatedAt *CustomDate `json:"createdAt"`
	// UpdatedAt *CustomDate `json:"updatedAt"`
	UserIds []int `json:"userIds"`
}

type UserEvent struct {
	ID       int    `json:"id"`
	FullName string `json:"fullName"`
	Email    string `json:"email"`
	// CreatedAt *CustomDate `json:"createdAt"`
	// UpdatedAt *CustomDate `json:"updatedAt"`
}

type ValidKafkaMessageTypes interface {
	UserEvent | ProjectEvent | TaskEvent
}

// ClientManager manages active WebSocket connections.
type ClientManager struct {
	// Clients maps user IDs to WebSocket connections
	Clients map[int]*websocket.Conn

	// Mutex to protect access to the Clients mapping. This mutex should be
	// locked whenever reading from or writing to Clients.
	ClientMutex sync.Mutex

	UserEventStore map[int]*UserEvent

	ProjectEventStore map[int]*ProjectEvent

	TaskEventStore map[int]*TaskEvent
}

// NewClientManager creates and returns a new instance of ClientManager.
func NewClientManager() *ClientManager {
	return &ClientManager{
		Clients:           make(map[int]*websocket.Conn),
		UserEventStore:    make(map[int]*UserEvent),
		ProjectEventStore: make(map[int]*ProjectEvent),
		TaskEventStore:    make(map[int]*TaskEvent),
	}
}

// AddClient registers a new WebSocket connection (conn) in the client manager.
func (cm *ClientManager) AddClient(userId int, conn *websocket.Conn) {
	cm.ClientMutex.Lock()
	defer cm.ClientMutex.Unlock()

	cm.Clients[userId] = conn
	log.Printf("New client connection: %s for user: %d\n", conn.RemoteAddr(), userId)
}

// CloseAllConnections closes and removes all active WebSocket connections from
// the Clients mapping.
func (cm *ClientManager) CloseAllConnections() {
	cm.ClientMutex.Lock()
	defer cm.ClientMutex.Unlock()

	for email, conn := range cm.Clients {
		conn.Close()              // Close the connection
		delete(cm.Clients, email) // Delete the connection from the Clients mapping

		log.Printf("Removed client connection: %s\n", conn.RemoteAddr())
	}
}

func (cm *ClientManager) UpdateUserEventStore(user_event *UserEvent) {
	log.Printf("Adding user event %d to event store", user_event.ID)
	cm.UserEventStore[user_event.ID] = user_event
}

func (cm *ClientManager) UpdateProjectEventStore(project_event *ProjectEvent) {
	log.Printf("Adding project event %d to event store", project_event.ID)
	cm.ProjectEventStore[project_event.ID] = project_event
}

func (cm *ClientManager) UpdateTaskEventStore(task_event *TaskEvent) {
	log.Printf("Adding task event %d to event store", task_event.ID)
	cm.TaskEventStore[task_event.ID] = task_event
}

func (cm *ClientManager) BroadcastTaskMessage(task_event *TaskEvent, message *string) {
	log.Printf("Broadcasting task event %d\n", task_event.ID)

	project_id := task_event.ProjectID
	project_event := cm.ProjectEventStore[project_id]
	if project_event == nil {
		log.Printf("Project with id %d not found", project_id)
		return
	}

	user_ids := project_event.UserIds
	if user_ids == nil {
		log.Printf("Project event with id %d does not have a list of user ids", project_id)
		return
	}

	for client_user_id := range cm.Clients {
		log.Println(user_ids)
		log.Println(client_user_id)
		if utils.Contains(user_ids, client_user_id) {
			log.Printf("Sending task event to user %d", client_user_id)
			conn := cm.Clients[client_user_id]

			err := conn.WriteMessage(websocket.TextMessage, []byte(*message))
			if err != nil {
				log.Printf("Error sending to websocket: %v. Removing client %s", err, conn.RemoteAddr())
				conn.Close()
				delete(cm.Clients, client_user_id)
			}
		}
	}
}
