import React, { useState } from "react";
import { Link } from "react-router-dom";
import loginImage from "../../assets/images/login-image.jpg"; 

const Login = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("User Logged In:", formData);
  };

  return (
    <div className="flex min-h-screen bg-gray-100">
      
      <div className="hidden md:flex w-1/2 items-center justify-center p-5">
        <img src={loginImage} alt="Project Management" className="w-4/5 h-4/5 object-cover rounded-lg shadow-md" />
      </div>

      
      <div className="flex items-center justify-center w-full md:w-1/2 bg-white p-8 rounded-lg shadow-md">
        <div className="w-96">
          <h2 className="text-2xl font-bold text-center text-blue-600">Login</h2>
          <form className="mt-4" onSubmit={handleSubmit}>
            <div className="mb-4">
              <label className="block text-gray-700">Email:</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
                className="w-full p-2 border border-gray-300 rounded mt-1"
              />
            </div>
            <div className="mb-4">
              <label className="block text-gray-700">Password:</label>
              <input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
                className="w-full p-2 border border-gray-300 rounded mt-1"
              />
            </div>
            <button className="w-full bg-blue-600 text-white p-2 rounded mt-4 hover:bg-blue-700">
              Sign In
            </button>
          </form>
          
          <p className="mt-4 text-center text-gray-700">
            Don't have an account?{" "}
            <Link to="/signup" className="text-blue-600 hover:underline">
              Click here
            </Link> to sign up.
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;
