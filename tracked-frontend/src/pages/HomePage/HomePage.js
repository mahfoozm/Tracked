import React from "react";
import { useNavigate } from "react-router-dom";
import projectImg from "../../assets/images/projects.jpg";

const HomePage = () => {
  const navigate = useNavigate();

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <section className="w-full max-w-4xl text-center">
        <img className="w-full rounded-lg shadow-md" src={projectImg} alt="Projects illustration" />
      </section>

      
      <section id="features" className="mt-8 grid gap-6 md:grid-cols-3 text-center">
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

      
      <section className="mt-12 text-center">
        <h2 className="text-2xl font-bold text-blue-600">Ready to boost your team's productivity?</h2>
        <p className="text-gray-700 mt-2">Sign up today and experience project collaboration like never before!</p>
        <div className="mt-6 space-x-4">
          <button 
            className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition"
            onClick={() => navigate("/signup")}
          >
            Register
          </button>
          <button 
            className="bg-gray-600 text-white px-6 py-3 rounded-lg hover:bg-gray-700 transition"
            onClick={() => navigate("/login")}
          >
            Sign In
          </button>
        </div>
      </section>
    </div>
  );
};

export default HomePage;
