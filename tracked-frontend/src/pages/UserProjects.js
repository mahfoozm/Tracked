import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../services/AuthContext"; 

const UserProjects = () => {
  const { user } = useAuth();
  const [projects, setProjects] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const navigate = useNavigate(); 
  const [userData, setUserData] = useState(null);
  const [error, setError] = useState(null);

  const handleClick = () => {
    navigate("/create_project"); 
  };

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

  useEffect(() => {
    if (!user) {
      setProjects([
        { id: 1, name: "Comic Website" },
        { id: 2, name: "Comic Webscraper" },
        { id: 3, name: "Nexus Comic Archive" },
        { id: 4, name: "Project D" },
        { id: 5, name: "C5R" },
        { id: 6, name: "Daytona 500" }, 
        { id: 7, name: "Hakone" }, 
        { id: 8, name: "Poison Elves" }, 
        { id: 9, name: "Twin Charger" }, 
        { id: 10, name: "Crimson Empire" }, 
        { id: 11, name: "Enemy of the Empire" }, 
        { id: 12, name: "Z06" }
      ]);
    } else {
      fetchProjects();
    }
  }, [user]);

  const fetchProjects = async () => {
    if (userData) {
      const response = await fetch(`http://localhost:8081/projects/user/${userData.id}`, {
    });
    const gotProjects = response.json;
    setProjects(gotProjects);

    console.log("Got Projects: ", projects);
    }
  }

  const searchProjects = projects.filter(project =>
    project.name.toLowerCase().includes(searchQuery.toLowerCase())
);

  return (
    <div className="flex items-center justify-center w-full min-h-screen bg-gray-100 py-8">
      <div className="w-3/4 md:w-2/3 lg:w-1/2 bg-white p-8 rounded-lg shadow-lg">
        <div className="relative mb-4">
          <h2 className="text-4xl font-extrabold text-blue-600 text-center">
            {userData? `${userData.fullName}'s Projects` : "User's Projects"}
          </h2>
          <button 
            onClick={handleClick}
            className="absolute right-0 top-1/2 -translate-y-1/2 bg-white text-blue-600 px-4 py-2 rounded-lg hover:bg-gray-200">
            Create Project
          </button>
        </div>
        <div className="mb-4">
          <input 
            type="text" 
            placeholder="Search projects..." 
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div className="h-[61vh] overflow-y-auto border border-gray-200 rounded-md p-4">
          {searchProjects.length > 0 ? (
            <ul className="space-y-3">
              {searchProjects.map((project) => (
                <div>
                  <li 
                    key={project.id} 
                    className="p-4 bg-gray-50 rounded-md shadow-sm hover:text-green-300"
                    onClick={() => navigate(`/projects/${project.id}`)}
                   >
                    {project.name}
                  </li>
                </div>
                
              ))}
            </ul>
          ) : (
            <p className="text-center text-gray-500">No projects assigned :(</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default UserProjects;