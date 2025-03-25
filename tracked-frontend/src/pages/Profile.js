import React, { useState, useEffect, useRef } from "react";
import useWebSocket from "react-use-websocket";
import { isAuthenticated } from "../services/authService";
import defaultProfileImage from "../assets/images/default-profile.png";

const WEBSOCKET_BASE_URL = "localhost:8080";

const Profile = () => {
  const socketUrl = `ws://${WEBSOCKET_BASE_URL}/ws`;

  const [messages, setMessages] = useState([]);
  const [userData, setUserData] = useState(null);
  const [error, setError] = useState(null);
  const [profileImage, setProfileImage] = useState(defaultProfileImage);
  const [selectedFileName, setSelectedFileName] = useState("");

  const fileInputRef = useRef(null);

  const { sendMessage, lastMessage } = useWebSocket(socketUrl);

  useEffect(() => {
    if (lastMessage !== null) {
      setMessages((prev) => prev.concat(lastMessage.data));
    }
  }, [lastMessage]);

  useEffect(() => {
    const fetchUser = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        setError("Not authenticated.");
        return;
      }

      try {
        const response = await fetch("http://localhost:8081/users/me", {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          throw new Error("Failed to fetch user data.");
        }

        const data = await response.json();
        setUserData(data);
      } catch (err) {
        console.error("Error fetching user info:", err);
        setError(err.message || "Error loading user data.");
      }
    };

    fetchUser();
  }, []);

  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const imageURL = URL.createObjectURL(file);
      setProfileImage(imageURL);
      setSelectedFileName(file.name);
    }
  };

  const handleRemoveImage = () => {
    setProfileImage(defaultProfileImage);
    setSelectedFileName("");
    if (fileInputRef.current) {
      fileInputRef.current.value = ""; 
    }
  };

  return (
    <div className="p-8 max-w-5xl mx-auto bg-white shadow-md rounded-lg mt-8 flex flex-col md:flex-row gap-6">
      <div className="flex flex-col items-center w-full md:w-1/3 border-r md:pr-6">
        <h3 className="text-xl font-bold text-center mb-2">Profile Picture</h3>
        <img
          src={profileImage}
          alt="Profile"
          className="w-40 h-40 rounded-full shadow object-cover"
        />
        <p className="text-sm text-gray-600 mt-3 text-center">
          Click Browse to upload Profile Image
        </p>

        <div className="flex flex-col items-center mt-2 w-full text-center">
          <input
            type="file"
            accept="image/*"
            onChange={handleImageUpload}
            ref={fileInputRef}
            className="block mx-auto"
          />
          {selectedFileName && (
            <button
              onClick={handleRemoveImage}
              className="text-red-500 text-xs mt-2 hover:underline"
            >
              Remove Image
            </button>
          )}
        </div>
      </div>

      <div className="w-full md:w-2/3">
        <h2 className="text-2xl font-bold text-blue-600 mb-4 text-center md:text-left">
          User Profile
        </h2>

        {error && <p className="text-red-500 text-center">{error}</p>}

        {userData ? (
          <div className="space-y-3">
            <p><strong>Full Name:</strong> {userData.fullName || "N/A"}</p>
            <p><strong>Email:</strong> {userData.email}</p>
            <p><strong>Username:</strong> {userData.username}</p>
            <p><strong>Roles:</strong> {userData.authorities?.join(", ") || "None"}</p>
            <p><strong>Status:</strong> {userData.enabled ? "Enabled" : "Disabled"}</p>
          </div>
        ) : !error ? (
          <p className="text-center text-gray-600">Loading profile...</p>
        ) : null}

        <hr className="my-6" />

        <h3 className="text-xl font-semibold text-blue-600 mb-2">
          Real-time Messages
        </h3>
        {messages.length > 0 ? (
          messages.map((msg, idx) => (
            <p key={idx} className="text-sm text-gray-700">{msg}</p>
          ))
        ) : (
          <p className="text-sm text-gray-500">No messages yet.</p>
        )}
      </div>
    </div>
  );
};

export default Profile;
