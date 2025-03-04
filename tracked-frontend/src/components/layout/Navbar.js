import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

const Navbar = () => {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false); // Temporary state for auth logic

  const handleLogout = () => {
    setIsLoggedIn(false); // Reset login state
    // Temporary, need to change to work with the user-service
    navigate("/login"); // Redirect to login page after logout
  };

  return (
    <nav className="bg-blue-600 text-white p-4 shadow-md">
      <div className="container mx-auto flex justify-between items-center">
        <div className="flex flex-col items-start">
          <Link to="/" className="text-2xl font-bold">Tracked</Link>
          <span className="text-sm text-gray-200">One stop for everything Project Management</span>
        </div>

        
        <div className="flex-1 flex justify-center space-x-8">
          <Link to="/projects" className="hover:text-gray-300">Projects</Link>
          <Link to="/tasks" className="hover:text-gray-300">Tasks</Link>
          <Link to="/profile" className="hover:text-gray-300">Profile</Link>
        </div>

        
        <div>
          {isLoggedIn ? (
            <button
              onClick={handleLogout}
              className="bg-white text-blue-600 px-4 py-2 rounded-lg hover:bg-gray-200"
            >
              Logout
            </button>
          ) : (
            <Link
              to="/login"
              className="bg-white text-blue-600 px-4 py-2 rounded-lg hover:bg-gray-200"
            >
              Login
            </Link>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
