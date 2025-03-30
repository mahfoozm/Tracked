import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../services/AuthContext"; 

const CreateTaskPage = () => {

  const [taskName, setTaskName] = useState('');
  const [projectName, setProjectName] = useState('');
  const [allProjects, setAllProjects] = useState([]);
  const [assignee, setAssignee] = useState('');
  const [date, setDate] = useState('');
  const [description, setDescription] = useState('');
  const [allUsers, setAllUsers] = useState([]);
  const [userData, setUserData] = useState(null);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  
  const handleSubmit = () => {

    const formattedEndDate = new Date(date).toISOString().split("T")[0];
    const today = new Date().toISOString().split("T")[0];

    const taskData = {
      name : taskName,
      projectId : projectName,
      assigneeUserId : assignee.id,
      creatorUserId : userData.id,
      startDate : today,
      endDate: formattedEndDate,
      status: "IN_PROGRESS"
    };
  
    console.log('Created Task JSON:', taskData);
    addTask(taskData);

    navigate("/tasks");
  };

  const addTask = async (taskData) => {
    if (userData) {
      const response = await fetch(`http://localhost:8082/task`, {
        method: "POST",
        body: JSON.stringify(taskData),
        headers: {
          "Content-Type" : "application/json"
        }
      });
    }
  }

  const retrieveAllUsers = async() => {
    const token = localStorage.getItem("token");

    const response = await fetch(`http://localhost:8081/users/`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      }
    }
    );
    if (response.ok) {

      setAllUsers(await response.json())
    }
    
  }

  const fetchProjects = async () => {
    const token = localStorage.getItem("token");

      const response = await fetch(`http://localhost:8083/projects`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      }
    });
    if (response.ok) {
      setAllProjects(await response.json());
    }
  }

  useEffect(() => {
    fetchProjects();
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
      retrieveAllUsers();
    }, [navigate]);

  return (
    <div className="flex items-center justify-center w-full min-h-screen bg-gray-100 py-8">
      <div className="w-3/4 md:w-2/3 lg:w-1/2 bg-white p-8 rounded-lg shadow-lg">
        <div className="relative mb-12">
          <h2 className="text-4xl font-extrabold text-blue-600 text-center">
            Create Task
          </h2>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-lg mb-6">
          <div className="font-semibold text-gray-700">Task Name:</div>
          <input
            type="text"
            className="border rounded p-2 text-gray-900"
            placeholder="Task Name"
            value={taskName}
            onChange={(e) => setTaskName(e.target.value)}
          />

          <div className="font-semibold text-gray-700">Part of:</div>
          <div className="h-32 overflow-y-auto border rounded p-2 flex flex-wrap gap-2">
            {/* ['Nexus','Five Night', 'C5', 'Project D', 'LongBox', 'Wizard', 'DC', 'The Name', 'Crimson Empire', 'Dark Pheonix', 'Rogue Squadron', 'Fate'] */}
            {allProjects.map((project, index) => (
              <button
                key={index}
                type="button"
                className={`px-4 py-1 rounded-full border transition text-sm ${
                  projectName === project.id
                    ? 'bg-blue-600 text-white border-blue-600'
                    : 'bg-white text-gray-700 border-gray-300'
                }`}
                onClick={() =>
                  setProjectName((prev) => (prev.id === project.id ? '' : project.id))
                }
              >
                {project.name}
              </button>
            ))}
          </div>

          <div className="font-semibold text-gray-700">Assignee:</div>
          <div className="h-32 overflow-y-auto border rounded p-2 flex flex-wrap gap-2">
            {/* ['Alice', 'Bob', 'Charlie', 'Dana', 'Eli', 'Fred', 'Gerry', 'Henry', 'Ines', 'Jack', 'Kelvin', 'Lawrance',' Mikey', 'Nicole', 'Oscar', 'Penny', 'Quan'] */}
            {allUsers.map((user, index) => (
              <button
                key={index}
                type="button"
                className={`px-4 py-1 rounded-full border transition text-sm ${
                  assignee === user
                    ? 'bg-blue-600 text-white border-blue-600'
                    : 'bg-white text-gray-700 border-gray-300'
                }`}
                onClick={() =>
                  setAssignee((prev) => (prev.id === user.id ? '' : user))
                }
              >
                {user.fullName}
              </button>
            ))}
          </div>

          <div className="font-semibold text-gray-700">Due Date:</div>
          <input
            type="date"
            className="border rounded p-2 text-gray-900"
            placeholder="Due Date"
            value={date}
            onChange={(e) => setDate(e.target.value)}
          />

          <div className="font-semibold text-gray-700">Description:</div>
          <textarea
            rows="4"
            className="border rounded p-2 text-gray-900 col-span-full"
            placeholder="Enter a description..."
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </div>
        <div className="flex-grow" />
        <div className="flex justify-center">
          <button
            onClick={handleSubmit}
            className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 transition"
          >
            Create Task
          </button>
        </div>   
      </div>
    </div>
  );
};

export default CreateTaskPage;