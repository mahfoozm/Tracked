import React, { useState, useEffect } from "react";
import { useAuth } from "../services/AuthContext";
import { useParams, useNavigate } from "react-router-dom";

const ProjectPage = () => {

    const { user } = useAuth();
    const { id } = useParams();
    const [projectData, setProjectData] = useState(null);
    const [tasks, setTasks] = useState([]);
    const [searchQuery, setSearchQuery] = useState("");
    const navigate = useNavigate();
    const [userData, setUserData] = useState(null);
    const [error, setError] = useState(null);

    const handleClick = () => {
        navigate("/create_task");
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
        if (!userData) {
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
    }, [userData]);

    useEffect(() => {
        fetchProject();
    }, [id]);

    const fetchProject = async () => {
        const response = await fetch(`http://localhost:8083/projects/${id}`, {
        });
        const retrievedProject = await response.json();
        setProjectData(retrievedProject);
    }

    const fetchTasks = async () => {
        const response = await fetch(`http://localhost:8082/task?project_id=${id}`, {
        });
        const retrievedTasks = await response.json();
        setTasks(retrievedTasks);
    }

    const searchTasks = tasks.filter(task =>
        task.name.toLowerCase().includes(searchQuery.toLowerCase())
    );

    return (
        <div className="flex items-center justify-center w-full min-h-screen bg-gray-100 p-8 gap-x-8">
            <div className="w-3/4 md:w-2/3 lg:w-1/2 h-[80vh] bg-white p-8 rounded-lg shadow-lg">
                <h2 className="text-4xl font-extrabold text-center text-blue-600 mb-8">
                    {projectData ? projectData.name : "Project Name"}
                </h2>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-lg">
                    {/* <div className="font-semibold text-gray-700">Project Author:</div>
                    <div className="text-gray-900">
                        {projectData ? projectData.name : "Project Name"}
                    </div> */}

                    {/* <div className="font-semibold text-gray-700">Members:</div>
                    <div className="text-gray-900">
                        {projectData ? projectData.userProjectTables : "Members"}
                    </div> */}

                    <div className="font-semibold text-gray-700">Created At:</div>
                    <div className="text-gray-900">
                        {projectData ? projectData.createdAt : "Created At Date"}
                    </div>

                    <div className="font-semibold text-gray-700">Last Update:</div>
                    <div className="text-gray-900">
                        {projectData ? projectData.updatedAt : "Last Updated Date"}
                    </div>
                </div>
                <div className="mt-6">
                    <h3 className="font-semibold text-gray-700">Description:</h3>
                    <p className="text-gray-900 mt-2 bg-gray-50 p-4 rounded-md">[Project Description]</p>
                </div>
            </div>
            <div className="w-3/4 md:w-2/3 lg:w-1/2 h-[80vh] bg-white p-8 rounded-lg shadow-lg">
                <div className="relative mb-4">
                    <h2 className="text-4xl font-extrabold text-blue-600 text-center">
                        Project Tasks
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
                                <li
                                    key={task.id}
                                    className="p-4 bg-gray-50 rounded-md shadow-sm hover:text-green-500 cursor-pointer"
                                    onClick={() => navigate(`/tasks/${task.id}`)}
                                >
                                    {task.name}
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p className="text-center text-gray-500">No matching tasks :(</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ProjectPage;
