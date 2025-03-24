import React from "react";
import useWebSocket, { ReadyState } from 'react-use-websocket';
import {useState, useEffect} from "react"

const WEBSOCKET_BASE_URL = "localhost:8080";

const Profile = () => {
  const socketUrl = `ws://${WEBSOCKET_BASE_URL}/ws`;

  const [messages, setMessages] = useState([]);
  const { sendMessage, lastMessage, readyState } = useWebSocket(socketUrl);

  useEffect(() => {
    if (lastMessage !== null) {
      console.log(lastMessage)
      setMessages((prev) => prev.concat(lastMessage.data));
    }
  }, [lastMessage]);

  return (
    <div>
      <h2>Profile Page</h2>
      <p>User profile information will go here.</p>
      {messages.map((message, key) => <p id={key}>{message}</p>)}
    </div>
  );
};

export default Profile;
