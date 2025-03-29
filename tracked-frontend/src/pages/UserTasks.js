import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../services/AuthContext"; 

const UserTasks = () => {
  const { user } = useAuth();
  const [tasks, setTasks] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const navigate = useNavigate();
  const [userData, setUserData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUser = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Session expired. Please log in again.");
        navigate("/login");
        return;
      }
    
      try {
        const response = await fetch("http://localhost:8081/users/me", {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });
    
        if (!response.ok) throw new Error("Failed to fetch user data.");
    
        const data = await response.json();
        setUserData(data);
      } catch (err) {
        console.error("Error fetching user info:", err);
        setError(err.message || "Error loading user data.");
      }
    };
    
    fetchUser();
  }, [navigate]);

  const handleClick = () => {
    navigate("/create_task"); 
  };  

  useEffect(() => {
    if (!user) {
        setTasks([
            { id: 1, name: "Read Nexus" },
            { id: 2, name: "Change Engine Fluid" },
            { id: 3, name: "Read Elementals" },
            { id: 4, name: "Place Pre-Orders" },
            { id: 5, name: "Change Blinker Fluid" },
            { id: 6, name: "Pay Taxes" }, 
            { id: 7, name: "Drive 2 Store" }, 
            { id: 8, name: "Go to Aeroport" }, 
            { id: 9, name: "Edit DE10 Lite Project" }, 
            { id: 10, name: "Air Tires" }, 
            { id: 11, name: "Fix Alignment" }, 
            { id: 12, name: "Go 2 Aeroport Again" }
        ]);
      } else {
        fetchTasks();
      }
  }, [user]);

  const fetchTasks = async () => {
    if (userData) {
      const response = await fetch(`http://localhost:8082/task?assignee_user_id=${userData.id}`, {
    });
    const gotTasks = response.json;
    setTasks(gotTasks);

    console.log("Got Tasks: ", gotTasks);
    }
  }

  const searchTasks = tasks.filter(task =>
    task.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="flex items-center justify-center w-full min-h-screen bg-gray-100 py-8">
      <div className="w-3/4 md:w-2/3 lg:w-1/2 bg-white p-8 rounded-lg shadow-lg">
        <div className="relative mb-4">
          <h2 className="text-4xl font-extrabold text-blue-600 text-center">
            {userData ? `${userData.fullName}'s Tasks` : "User's Tasks"}
          </h2>
          <button 
            onClick={handleClick}
            className="absolute right-0 top-1/2 -translate-y-1/2 bg-white text-blue-600 px-4 py-2 rounded-lg hover:bg-gray-200">
            Create Task
          </button>
        </div>
        <div className="mb-4">
          <input 
            type="text" 
            placeholder="Search tasks..." 
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div className="h-[61vh] overflow-y-auto border border-gray-200 rounded-md p-4">
          {searchTasks.length > 0 ? (
            <ul className="space-y-3">
              {searchTasks.map((task) => (
                <div>
                  <li 
                    key={task.id} 
                    className="p-4 bg-gray-50 rounded-md shadow-sm hover:text-green-300"
                    onClick={() => navigate(`/tasks/${task.id}`)}
                   >
                    {task.name}
                  </li>
                </div>
              ))}
            </ul>
          ) : (
            <p className="text-center text-gray-500">No tasks assigned :(</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default UserTasks;