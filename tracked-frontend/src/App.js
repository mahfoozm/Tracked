import React, { useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Layout from "./components/layout/Layout";
import HomePage from "./pages/HomePage";
import Register from "./pages/Register";
import Login from "./pages/Login";
import Profile from "./pages/Profile"; 
import UserProjects from "./pages/UserProjects"; 
import UserTasks from "./pages/UserTasks"; 
import ProjectPage from "./pages/ProjectPage";
import TaskPage from "./pages/TaskPage";
import CreateProjectPage from "./pages/CreateProjectPage";
import CreateTaskPage from "./pages/CreateTaskPage";
import { AuthProvider, useAuth } from "./services/AuthContext";
import useWebSocket from "react-use-websocket";
import WebSocketWrapper from "./services/WebSocketWrapper";

const WEBSOCKET_BASE_URL = "132.145.109.6:8080";

const ProtectedRoute = ({ element }) => {
  const { user } = useAuth(); 
  return user ? element : <Navigate to="/login" />; 
};

const RedirectIfAuthenticated = ({ element }) => {
  const { user } = useAuth(); 
  return user ? <Navigate to="/profile" /> : element; 
};

function App() {
  // const { user } = useAuth();

  // // WebSocket connection with user ID
  // const { lastMessage } = useWebSocket(
  //   user ? `ws://${WEBSOCKET_BASE_URL}/ws?userId=${user.id}` : null,
  //   {
  //     shouldReconnect: () => true,
  //     reconnectAttempts: 10,
  //     reconnectInterval: 3000,
  //   }
  // );

  // useEffect(() => {
  //   console.log(lastMessage)
  // }, [lastMessage])

  // useEffect(() => {
  //   if (lastMessage !== null) {
  //     try {
  //       const notification = JSON.parse(lastMessage.data);
  //       // Check if this notification is for the current user
  //       if (!notification.userId || notification.userId === userData?.id) {
  //         setNotifications(prev => [...prev, notification]);
  //       }
  //     } catch (e) {
  //       console.error("Failed to parse notification:", e);
  //     }
  //   }
  // }, [lastMessage, userData]);

  return (
    <AuthProvider>
      <WebSocketWrapper>
        <Router>
          <Layout>
            <Routes>
              <Route path="/" element={<HomePage />} />

              <Route path="/signup" element={<RedirectIfAuthenticated element={<Register />} />} />
              <Route path="/login" element={<RedirectIfAuthenticated element={<Login />} />} />

              <Route path="/profile" element={<ProtectedRoute element={<Profile />} />} />
              <Route path="/projects" element={<ProtectedRoute element={<UserProjects />} />} />
              <Route path="/tasks"  element={<ProtectedRoute element={<UserTasks />} />} />

              <Route path="/projects/:id" element={<ProjectPage />} />
              <Route path="/tasks/:id" element={<TaskPage />} />

              <Route path="/create_project" element={<CreateProjectPage />} />
              <Route path="/create_task" element={<CreateTaskPage />} />

            </Routes>
          </Layout>
        </Router>
      </WebSocketWrapper>
    </AuthProvider>
  );
}

export default App;
