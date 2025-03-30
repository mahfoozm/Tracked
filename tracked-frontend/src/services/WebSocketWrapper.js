import React, { useEffect } from "react";
import { useAuth } from "./AuthContext";
import useWebSocket from "react-use-websocket";

const WEBSOCKET_BASE_URL = "localhost:8080";

export default function WebSocketWrapper({ children }) {
  const { user } = useAuth();

  // WebSocket connection with user ID
  const { lastMessage } = useWebSocket(
    user ? `ws://${WEBSOCKET_BASE_URL}/ws?userId=${user.id}` : null,
    {
      shouldReconnect: () => true,
      reconnectAttempts: 10,
      reconnectInterval: 3000,
    }
  );

  useEffect(() => {
    console.log(lastMessage)
    if (lastMessage) {
      const task = JSON.parse(lastMessage.data)
      alert("New task has been created: " + task.name)
    }
  }, [lastMessage, user])
  useEffect(() => {
    console.log('user:')
    console.log(user)
  }, [user])

  return (
    <>
      ({ children })
    </>
  )
}
