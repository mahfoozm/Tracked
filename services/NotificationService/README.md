# Notification Service

## Overview

The **Notification Service** is a WebSocket-based system that listens for messages from Kafka and broadcasts them to connected clients. It consists of three main components:

- **WebSocket Server**: Manages client connections and broadcasts messages.

- **Kafka Consumer**: Listens for messages from Kafka and forwards them to the WebSocket server.

- **Client Manager**: Keeps track of connected WebSocket clients.

## Architecture

1. **Kafka Producer** sends messages to a topic (will be implemented through other microservices, but for now can be simulated by running [test_producer.py](./test_producer.py)).

2. The **Kafka Consumer** subscribes to topics and receives messages.

3. The **WebSocket Server** forwards the messages to all connected clients.

---

## Installation

### Prerequisites

Ensure you have the following installed:

- [Docker](https://www.docker.com/)

- [Docker Compose](https://docs.docker.com/compose/)

If you would like to run notification service locally, ensure that you have the following installed:

- [Kafka](https://kafka.apache.org/)
- [Go](https://go.dev/)

---

## Running the Service

### Using Docker Compose

1. Clone the repository:

   ```sh
   git clone https://github.com/mahfoozm/Tracked.git Tracked
   cd Tracked/NotificationService
   ```

2. Start the service:

   ```sh
   docker compose up -d --build
   ```

3. The WebSocket server will be running at:

   ```
   ws://132.145.109.6:8080/ws
   ```

4. To stop the service:

   ```sh
   docker compose down
   ```

### Without Docker (Running Locally)

1. Start Kafka and Zookeeper locally.

3. Run the service using `go run`:

   ```sh
   go run main.go
   ```

### Simulating Events

1. Ensure that Kafka and the notification service are running.

2. Open [index.html](./index.html) in your browser and click the "Connect" button.

3. Run [test_producer.py](./test_producer.py) and enter values to send to through Kafka.

4. Observe as the notification service forwards the events to the browser.
