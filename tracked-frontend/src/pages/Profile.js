import React, { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../services/AuthContext";
import defaultProfileImage from "../assets/images/default-profile.png";

const Profile = () => {
  const navigate = useNavigate();
  const { logoutUser } = useAuth();

  const [notifications, setNotifications] = useState([]);
  const [userData, setUserData] = useState(null);
  const [error, setError] = useState(null);
  const [profileImage, setProfileImage] = useState(defaultProfileImage);
  const [selectedFileName, setSelectedFileName] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [hasChanges, setHasChanges] = useState(false);
  const [editedFields, setEditedFields] = useState({});

  const fileInputRef = useRef(null);
  
  // Get current user data first, then establish websocket connection with userId
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
        const response = await fetch("http://132.145.109.6:8081/users/me", {
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
          setProfileImage(`http://132.145.109.6:8081/uploads/profile-images/${filename}`);
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

      const response = await fetch("http://132.145.109.6:8081/users/update", {
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
  
  // Format the timestamp to a readable date/time
  const formatTimestamp = (timestamp) => {
    return new Date(timestamp * 1000).toLocaleString();
  };

  return (
    <div className="p-8 max-w-5xl mx-auto bg-white shadow-md rounded-lg mt-8 flex flex-col md:flex-row gap-6">
      {/* Left column - profile picture section (unchanged) */}
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

      {/* Right column - profile details and notifications */}
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
          Your Notifications
        </h3>
        <div className="bg-gray-50 p-4 rounded-lg max-h-60 overflow-y-auto">
          {notifications.length > 0 ? (
            notifications.map((notification, idx) => (
              <div key={idx} className="mb-3 p-2 bg-white shadow-sm rounded border-l-4 border-blue-500">
                <p className="text-sm text-gray-700">{notification.content}</p>
                <p className="text-xs text-gray-500 mt-1">
                  {formatTimestamp(notification.timestamp)}
                </p>
              </div>
            ))
          ) : (
            <p className="text-sm text-gray-500">No notifications yet.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default Profile;