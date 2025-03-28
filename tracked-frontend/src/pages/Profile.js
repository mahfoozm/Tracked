import React, { useState, useEffect, useRef } from "react";
import useWebSocket from "react-use-websocket";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../services/AuthContext";
import defaultProfileImage from "../assets/images/default-profile.png";

const WEBSOCKET_BASE_URL = "localhost:8080";

const Profile = () => {
  const socketUrl = `ws://${WEBSOCKET_BASE_URL}/ws`;
  const navigate = useNavigate();
  const { logoutUser } = useAuth();

  const [messages, setMessages] = useState([]);
  const [userData, setUserData] = useState(null);
  const [error, setError] = useState(null);
  const [profileImage, setProfileImage] = useState(defaultProfileImage);
  const [selectedFileName, setSelectedFileName] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [hasChanges, setHasChanges] = useState(false);
  const [editedFields, setEditedFields] = useState({});

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
        logoutUser();
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
        setEditedFields({
          fullName: data.fullName || "",
          email: data.email || "",
        });

        // Extract file name and construct image URL
        if (data.profileImageFilename) {
          const filename = data.profileImageFilename.split("/").pop();
          setProfileImage(`http://localhost:8081/uploads/profile-images/${filename}`);
        } else {
          setProfileImage(defaultProfileImage);
        }
      } catch (err) {
        console.error("Error fetching user info:", err);
        setError(err.message || "Error loading user data.");
      }
    };

    fetchUser();
  }, [logoutUser, navigate]);

  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const imageURL = URL.createObjectURL(file);
      setProfileImage(imageURL);
      setSelectedFileName(file.name);
      setHasChanges(true);
    }
  };

  const handleRemoveImage = () => {
    setProfileImage(defaultProfileImage);
    setSelectedFileName("");
    setHasChanges(true);
    if (fileInputRef.current) {
      fileInputRef.current.value = "";
    }
  };

  const handleFieldChange = (field, value) => {
    setEditedFields((prev) => ({ ...prev, [field]: value }));
    setHasChanges(true);
  };

  const toggleEdit = () => {
    if (isEditing && userData) {
      setEditedFields({
        fullName: userData.fullName || "",
        email: userData.email || "",
      });
      setHasChanges(false);
    }
    setIsEditing((prev) => !prev);
  };

  const handleSaveChanges = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      setError("Not authenticated.");
      return;
    }

    if (!editedFields.fullName.trim() || !editedFields.email.trim()) {
      setError("Full name and email are required.");
      return;
    }

    try {
      const formData = new FormData();
      formData.append("fullName", editedFields.fullName.trim());
      formData.append("email", editedFields.email.trim());

      if (fileInputRef.current?.files[0]) {
        formData.append("profileImage", fileInputRef.current.files[0]);
      }

      const response = await fetch("http://localhost:8081/users/update", {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (!response.ok) {
        const errorData = await response.json();
        const errorMsg =
          errorData.detail || errorData.message || "Failed to update profile.";
        setError(errorMsg);
        return;
      }

      const updatedUser = await response.json();
      setUserData(updatedUser);
      setIsEditing(false);
      setHasChanges(false);
      setError(null);

      alert("Profile updated. Please log in again.");
      logoutUser();
      navigate("/login");
    } catch (err) {
      console.error("Error updating profile:", err);
      setError(err.message || "Update failed.");
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
        <div className="flex justify-between items-center">
          <h2 className="text-2xl font-bold text-blue-600 mb-4">User Profile</h2>
          {userData && (
            <button
              onClick={toggleEdit}
              className="text-blue-600 hover:underline text-sm"
            >
              {isEditing ? "Cancel" : "Edit"}
            </button>
          )}
        </div>

        {error && <p className="text-red-500 text-center">{error}</p>}

        {userData ? (
          <div className="space-y-3">
            <p>
              <strong>Full Name:</strong>{" "}
              {isEditing ? (
                <input
                  type="text"
                  value={editedFields.fullName}
                  onChange={(e) => handleFieldChange("fullName", e.target.value)}
                  className="border border-gray-300 px-2 py-1 rounded ml-2"
                />
              ) : (
                userData.fullName || "N/A"
              )}
            </p>
            <p>
              <strong>Email:</strong>{" "}
              {isEditing ? (
                <input
                  type="email"
                  value={editedFields.email}
                  onChange={(e) => handleFieldChange("email", e.target.value)}
                  className="border border-gray-300 px-2 py-1 rounded ml-2"
                />
              ) : (
                userData.email
              )}
            </p>
            <p>
              <strong>Roles:</strong>{" "}
              {userData.authorities?.join(", ") || "None"}
            </p>
            <p>
              <strong>Status:</strong>{" "}
              {userData.enabled ? "Enabled" : "Disabled"}
            </p>

            {hasChanges && (
              <button
                onClick={handleSaveChanges}
                className="mt-4 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
              >
                Save Changes
              </button>
            )}
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
            <p key={idx} className="text-sm text-gray-700">
              {msg}
            </p>
          ))
        ) : (
          <p className="text-sm text-gray-500">No messages yet.</p>
        )}
      </div>
    </div>
  );
};

export default Profile;
