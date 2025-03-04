import React, { useState } from "react";
import { Link } from "react-router-dom";
import registerImage from "../../assets/images/register-image.jpg"; // Ensure correct image path

const Register = () => {
  const [formData, setFormData] = useState({
    name: "",
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
    console.log("User Registered:", formData);
  };

  return (
    <div className="flex min-h-screen bg-gray-100">
      
      <div className="hidden md:flex w-1/2 items-center justify-center p-5">
        <img src={registerImage} alt="Work Setup" className="w-4/5 h-4/5 object-cover rounded-lg shadow-md" />
      </div>

      
      <div className="flex items-center justify-center w-full md:w-1/2 bg-white p-8 rounded-lg shadow-md">
        <div className="w-96">
          <h2 className="text-2xl font-bold text-center text-blue-600">Register</h2>
          <form className="mt-4" onSubmit={handleSubmit}>
            <div className="mb-4">
              <label className="block text-gray-700">Name:</label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                required
                className="w-full p-2 border border-gray-300 rounded mt-1"
              />
            </div>
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
              Sign Up
            </button>
          </form>
          
          <p className="mt-4 text-center text-gray-700">
            Already have an account?{" "}
            <Link to="/login" className="text-blue-600 hover:underline">
              Click here
            </Link> to sign in.
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;
