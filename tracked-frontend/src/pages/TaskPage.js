import React from "react";

const TaskPage = () => {

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100 p-6">
            <div className="w-full max-w-3xl bg-white p-8 rounded-lg shadow-xl">
                <h2 className="text-4xl font-extrabold text-center text-blue-600 mb-8">
                    Task Name
                </h2>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-lg">
                    <div className="font-semibold text-gray-700">Task Name:</div>
                    <div className="text-gray-900">[Task Name]</div>
                    
                    <div className="font-semibold text-gray-700">Created At:</div>
                    <div className="text-gray-900">[Created Date]</div>
                    
                    <div className="font-semibold text-gray-700">Last Update:</div>
                    <div className="text-gray-900">[Last Updated Date]</div>
                    
                    <div className="font-semibold text-gray-700">Due At:</div>
                    <div className="text-gray-900">[Due Date]</div>
                    
                    <div className="font-semibold text-gray-700">Status:</div>
                    <div className="text-gray-900">[Status]</div>
                    
                    <div className="font-semibold text-gray-700">Project:</div>
                    <div className="text-gray-900">[Project Name]</div>
                    
                    <div className="font-semibold text-gray-700">Assignee:</div>
                    <div className="text-gray-900">[Assignee Name]</div>
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
