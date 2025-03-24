import React from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../services/AuthContext"; 
import projectImg from "../assets/images/projects.jpg";

const HomePage = () => {
  const navigate = useNavigate();
  const { user } = useAuth(); 

  const handleButtonClick = (route) => {
    if (user) {
      navigate("/profile");
    } else {
      navigate(route);
    }
  };

  return (
    <div className="bg-gray-100 flex flex-col min-h-screen">
      <div className="flex justify-center items-center mt-4">
        <img 
          src={projectImg} 
          alt="Project Collaboration"
          className="w-[80%] max-w-5xl rounded-lg shadow-md object-cover"
        />
      </div>

      <section id="features" className="grid gap-6 md:grid-cols-3 text-center px-6 mt-8">
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold text-blue-600">Team Collaboration</h2>
          <p className="text-gray-700 mt-2">Join teams, communicate effortlessly, and work together seamlessly.</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold text-blue-600">Task Timeline</h2>
          <p className="text-gray-700 mt-2">Visualize upcoming tasks and deadlines to keep your projects on track.</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold text-blue-600">Notifications</h2>
          <p className="text-gray-700 mt-2">Get timely reminders so you never miss an important deadline.</p>
        </div>
      </section>

      <section className="mt-12 text-center px-6">
        <h2 className="text-2xl font-bold text-blue-600">Ready to boost your team's productivity?</h2>
        <p className="text-gray-700 mt-2">Sign up today and experience project collaboration like never before!</p>
        <div className="mt-6 space-x-4">
          <button 
            className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition"
            onClick={() => handleButtonClick("/signup")}
          >
            Register
          </button>
          <button 
            className="bg-gray-600 text-white px-6 py-3 rounded-lg hover:bg-gray-700 transition"
            onClick={() => handleButtonClick("/login")}
          >
            Sign In
          </button>
        </div>
      </section>
    </div>
  );
};

export default HomePage;
