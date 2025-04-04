import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../services/AuthContext"; 

const CreateProjectPage = () => {

  const { user } = useAuth();
  const [name, setProjectName] = useState('');
  const [description, setDescription] = useState('');
  const [userData, setUserData] = useState(null);
  const [error, setError] = useState(null);
  const navigate = useNavigate(); 
  const [allUsers, setAllUsers] = useState([]);
  const [selectedUsers, setSelectedUsers] = useState([]);

  const handleSubmit = async () => {
    const projectData = {
      name,
      projectMembers: [...new Set([...selectedUsers, userData.id])], 
      description,
    };

    console.log("Created Project JSON:", projectData);

    try {
      await addProject(projectData);
      navigate("/projects");
    } catch (e) {
      console.error("Error: ", e);
    }
  };

  const addProject = async (projectData) => {
    if (userData) {
      const response = await fetch(`http://localhost:8083/projects`, {
        method: "POST",
        body: JSON.stringify(projectData),
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
    retrieveAllUsers();
  }, [navigate]);

  return (
    <div className="flex items-center justify-center w-full min-h-screen bg-gray-100 py-8">
      <div className="w-3/4 md:w-2/3 lg:w-1/2 bg-white p-8 rounded-lg shadow-lg">
        <div className="relative mb-12">
          <h2 className="text-4xl font-extrabold text-blue-600 text-center">
            Create Project
          </h2>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-lg mb-6">

          <div className="font-semibold text-gray-700">Project Name:</div>
          <input
            type="text"
            className="border rounded p-2 text-gray-900"
            placeholder="Project Name"
            value={name}
            onChange={(e) => setProjectName(e.target.value)}
          />

          <div className="font-semibold text-gray-700">Members:</div>
          <div className="h-32 overflow-y-auto border rounded p-2 flex flex-wrap gap-2">
             {/* ['Alice', 'Bob', 'Charlie', 'Dana', 'Eli', 'Fred', 'Gerry', 'Henry', 'Ines', 'Jack', 'Kelvin', 'Lawrance',' Mikey', 'Nicole', 'Oscar', 'Penny', 'Quan'] */}
            {allUsers.map((user, index) => (
              <button
                key={index}
                type="button"
                className={`px-4 py-1 rounded-full border transition text-sm ${
                  selectedUsers.includes(user.id)
                    ? 'bg-blue-600 text-white border-blue-600'
                    : 'bg-white text-gray-700 border-gray-300'
                }`}
                onClick={() => {
                  setSelectedUsers((prev) =>
                    prev.includes(user.id)
                      ? prev.filter((n) => n !== user.id)
                      : [...prev, user.id]
                  );
                }}
              >
                {user.fullName}
              </button>
            ))}
          </div>

          <div className="font-semibold text-gray-700">Description:</div>
          <textarea
            rows="4"
            className="border rounded p-2 text-gray-900 md:col-span-1"
            placeholder="Enter a description..."
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </div>

        <div className="flex justify-center">
          <button
            onClick={handleSubmit}
            className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 transition"
          >
            Create Project
          </button>
        </div>

      </div>
    </div>
  );
};

export default CreateProjectPage;