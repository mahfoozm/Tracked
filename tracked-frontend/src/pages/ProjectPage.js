import React, { useState, useEffect } from "react";
import { useAuth } from "../services/AuthContext";
import { useParams } from "react-router-dom";

const ProjectPage = () => {
    const { user } = useAuth();
    const [tasks, setTasks] = useState([]);
    const { id } = useParams();

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
        }
      }, [user]);

    return (
        <div className="flex items-center justify-end w-full min-h-screen bg-gray-100 p-8">
            <div className="w-3/4 md:w-2/3 lg:w-1/2 h-[80vh] bg-white p-8 rounded-lg shadow-lg">
                <h2 className="text-3xl font-bold text-center text-blue-600 mb-6">
                    Project Owner:
                </h2>
                <h2 className="text-xl font-bold text-blue-600 mb-6">
                    Created At: 
                </h2>
                <h2 className="text-xl font-bold text-blue-600 mb-6">
                    Last Update: 
                </h2>
                <h2 className="text-xl font-bold text-blue-600 mb-6">
                    Description: 
                </h2>
            </div>
            <div className="w-3/4 md:w-2/3 lg:w-1/2 h-[80vh] bg-white p-8 rounded-lg shadow-lg">
                <h2 className="text-3xl font-bold text-center text-blue-600 mb-6">
                    Project Tasks
                </h2>
                <div className="mb-4">
                    <input 
                        type="text" 
                        placeholder="Search tasks..." 
                        className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>
                <div className="h-[61vh] overflow-y-auto border border-gray-200 rounded-md p-4">
                    {tasks.length > 0 ? (
                        <ul className="space-y-3">
                            {tasks.map((task) => (
                                <li 
                                    key={task.id} 
                                    className="p-4 bg-gray-50 rounded-md shadow-sm hover:text-green-300"
                                >
                                    {task.name}
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p className="text-center text-gray-500">No project tasks</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ProjectPage;
