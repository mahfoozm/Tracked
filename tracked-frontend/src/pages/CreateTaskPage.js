import React, { useState } from "react";

const CreateTaskPage = () => {
  const [searchQuery, setSearchQuery] = useState("");

  return (
    <div className="flex items-center justify-center w-full min-h-screen bg-gray-100">
      <div className="w-3/4 md:w-2/3 lg:w-1/2 h-[80vh] bg-white p-8 rounded-lg shadow-lg">
        <div className="relative mb-4">
          <h2 className="text-4xl font-extrabold text-blue-600 text-center">
            Create Task
          </h2>
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
      </div>
    </div>
  );
};

export default CreateTaskPage;