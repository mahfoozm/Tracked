import React, { useState } from "react";

const CreateTaskPage = () => {

  const [taskName, setTaskName] = useState('');
  const [projectName, setProjectName] = useState('');
  const [assignee, setAssignee] = useState('');
  const [date, setDate] = useState('');
  const [description, setDescription] = useState('');
  
  const handleSubmit = () => {
    const projectData = {
      taskName,
      projectName,
      assignee,
      date,
      description,
    };
  
    console.log('Created Task JSON:', projectData);
  };

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
            {['Nexus','Five Night', 'C5', 'Project D', 'LongBox', 'Wizard', 'DC', 'The Name', 'Crimson Empire', 'Dark Pheonix', 'Rogue Squadron', 'Fate'].map((name) => (
              <button
                key={name}
                type="button"
                className={`px-4 py-1 rounded-full border transition text-sm ${
                  projectName === name
                    ? 'bg-blue-600 text-white border-blue-600'
                    : 'bg-white text-gray-700 border-gray-300'
                }`}
                onClick={() =>
                  setProjectName((prev) => (prev === name ? '' : name))
                }
              >
                {name}
              </button>
            ))}
          </div>

          <div className="font-semibold text-gray-700">Assignee:</div>
          <div className="h-32 overflow-y-auto border rounded p-2 flex flex-wrap gap-2">
            {['Alice', 'Bob', 'Charlie', 'Dana', 'Eli', 'Fred', 'Gerry', 'Henry', 'Ines', 'Jack', 'Kelvin', 'Lawrance',' Mikey', 'Nicole', 'Oscar', 'Penny', 'Quan'].map((name) => (
              <button
                key={name}
                type="button"
                className={`px-4 py-1 rounded-full border transition text-sm ${
                  assignee === name
                    ? 'bg-blue-600 text-white border-blue-600'
                    : 'bg-white text-gray-700 border-gray-300'
                }`}
                onClick={() =>
                  setAssignee((prev) => (prev === name ? '' : name))
                }
              >
                {name}
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
          >s
            Create Task
          </button>
        </div>   
      </div>
    </div>
  );
};

export default CreateTaskPage;