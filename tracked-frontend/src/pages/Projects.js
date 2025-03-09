import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../services/AuthContext"; 

const Projects = () => {
  const { user } = useAuth();
  const [projects, setProjects] = useState([]);
  const navigate = useNavigate(); 

  const toProject = (e) => {
      navigate("/profile"); 
  };

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
    }
  }, [user]);

  return (
    <div className="flex items-center justify-center w-full min-h-screen bg-gray-100">
      <div className="w-3/4 md:w-2/3 lg:w-1/2 h-[80vh] bg-white p-8 rounded-lg shadow-lg">
        <h2 className="text-3xl font-bold text-center text-blue-600 mb-6">
          {user ? `${user}'s Projects` : "User's Projects"}
        </h2>
        <div className="mb-4">
          <input 
            type="text" 
            placeholder="Search projects..." 
            className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div className="h-[61vh] overflow-y-auto border border-gray-200 rounded-md p-4">
          {projects.length > 0 ? (
            <ul className="space-y-3">
              {projects.map((project) => (
                <div>
                  <li key={project.id} className="p-4 bg-gray-50 rounded-md shadow-sm hover:text-green-300"
                   onClick={toProject}
                   >
                    {project.name}
                  </li>
                </div>
                
              ))}
            </ul>
          ) : (
            <p className="text-center text-gray-500">No projects assigned</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default Projects;