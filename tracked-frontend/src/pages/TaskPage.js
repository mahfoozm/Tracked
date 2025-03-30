import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom"
import { useAuth } from "../services/AuthContext"; 


const TaskPage = () => {

    const { user } = useAuth();
    const { id } = useParams();
    const [taskData, setTaskData] = useState(null);
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

    useEffect(() => {
        fetchTask();
    }, [id]);

    const fetchTask = async () => {
        const response = await fetch(`http://localhost:8082/task/${id}`, {
        });
        const retrivedTask = await response.json()
        setTaskData(retrivedTask);
    }


    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100 p-6">
            <div className="w-full max-w-3xl bg-white p-8 rounded-lg shadow-xl">
                <h2 className="text-4xl font-extrabold text-center text-blue-600 mb-8">
                    {taskData ? taskData.name : "Task Name"}
                </h2>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-lg">
                    <div className="font-semibold text-gray-700">Task Name:</div>
                    <div className="text-gray-900">
                        {taskData ? taskData.name : "Task Name"}
                    </div>

                    <div className="font-semibold text-gray-700">Created At:</div>
                    <div className="text-gray-900">
                        {taskData ? taskData.createdAt : "Created At Date"}
                    </div>

                    <div className="font-semibold text-gray-700">Last Update:</div>
                    <div className="text-gray-900">
                        {taskData ? taskData.updatedAt : "Last Updated Date"}
                    </div>

                    <div className="font-semibold text-gray-700">Due At:</div>
                    <div className="text-gray-900">
                        {taskData ? taskData.endDate : "Due Date"}
                    </div>

                    <div className="font-semibold text-gray-700">Status:</div>
                    <div className="text-gray-900">
                        {taskData ? taskData.status : "Status"}
                    </div>

                    <div className="font-semibold text-gray-700">Project:</div>
                    <div className="text-gray-900">
                        {taskData ? taskData.project.name : "Due Date"}
                    </div>

                    <div className="font-semibold text-gray-700">Assignee:</div>
                    <div className="text-gray-900">
                        {taskData ? taskData.assignee.fullName : "Assignee"}
                    </div>
                </div>
                <div className="mt-6">
                    <h3 className="font-semibold text-gray-700">Description:</h3>
                    <p className="text-gray-900 mt-2 bg-gray-50 p-4 rounded-md">[Task Description]</p>
                </div>
            </div>
        </div>
    );
};

export default TaskPage;
