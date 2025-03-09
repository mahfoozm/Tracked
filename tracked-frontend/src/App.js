import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Layout from "./components/layout/Layout";
import HomePage from "./pages/HomePage";
import Register from "./pages/Register";
import Login from "./pages/Login";
import Profile from "./pages/Profile"; 
import Projects from "./pages/Projects"; 
import Tasks from "./pages/Tasks"; 
import ProjectPage from "./pages/ProjectPage";
import { AuthProvider, useAuth } from "./services/AuthContext"; 

const ProtectedRoute = ({ element }) => {
  const { user } = useAuth(); 
  return user ? element : <Navigate to="/login" />; 
};

const RedirectIfAuthenticated = ({ element }) => {
  const { user } = useAuth(); 
  return user ? <Navigate to="/profile" /> : element; 
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <Layout>
          <Routes>
            <Route path="/" element={<HomePage />} />

            <Route path="/signup" element={<RedirectIfAuthenticated element={<Register />} />} />
            <Route path="/login" element={<RedirectIfAuthenticated element={<Login />} />} />

            <Route path="/profile" element={<ProtectedRoute element={<Profile />} />} />
            <Route path="/projects" element={<Projects />} />
            <Route path="/tasks" element={<ProtectedRoute element={<Tasks />} />} />

            <Route path="/projects/:id" element={<ProjectPage />} />
          </Routes>
        </Layout>
      </Router>
    </AuthProvider>
  );
}

export default App;
