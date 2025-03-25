import React, { createContext, useState, useContext, useEffect } from "react";
import { login, signup, logout, getCurrentUser, isAuthenticated } from "./authService";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true); 

  useEffect(() => {
    const initializeUser = async () => {
      const token = localStorage.getItem("token");
      if (token) {
        try {
          const userData = await getCurrentUser();
          setUser({ ...userData, token });
        } catch (error) {
          console.error("Failed to rehydrate user from token:", error);
          localStorage.removeItem("token");
          setUser(null);
        }
      }
      setLoading(false);
    };

    initializeUser();
  }, []);

  const loginUser = async (email, password) => {
    try {
      const data = await login(email, password);
      setUser({ ...data, token: data.token });
      localStorage.setItem("token", data.token);
    } catch (err) {
      console.error("Error logging in", err);
      throw err;
    }
  };

  const signupUser = async (fullName, email, password) => {
    try {
      const data = await signup(fullName, email, password);
      setUser({ ...data, token: data.token });
      localStorage.setItem("token", data.token);
    } catch (err) {
      console.error("Error signing up", err);
      throw err;
    }
  };

  const logoutUser = () => {
    setUser(null);
    localStorage.removeItem("token");
  };

  return (
    <AuthContext.Provider value={{ user, loginUser, signupUser, logoutUser, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
