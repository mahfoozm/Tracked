

import React, { createContext, useState, useContext } from "react";
import { login, signup, logout } from "./authService"; 

const AuthContext = createContext();


export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);


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
    <AuthContext.Provider value={{ user, loginUser, signupUser, logoutUser }}>
      {children}  
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
