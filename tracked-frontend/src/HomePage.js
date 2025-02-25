import React from 'react';
import './HomePage.css';
import projectImg from './images/projects.jpg';

const HomePage = () => {
  return (
    <div className="homepage">
      <nav className="navbar">
        <div className="logo-container">
          <div className="logo">Tracked</div>
          <div className="logo-subtext">Your one stop for everything project management</div>
        </div>
      </nav>

      <section className="hero">
        <img
          className="hero-image"
          src={projectImg}
          alt="Projects illustration"
        />
      </section>

      <section id="features" className="features">
        <div className="feature">
          <h2>Team Collaboration</h2>
          <p>
            Join teams, communicate effortlessly, and work together seamlessly.
          </p>
        </div>
        <div className="feature">
          <h2>Task Timeline</h2>
          <p>
            Visualize upcoming tasks and deadlines to keep your Projects on track.
          </p>
        </div>
        <div className="feature">
          <h2>Notifications</h2>
          <p>
            Get timely reminders so you never miss an important deadline.
          </p>
        </div>
      </section>

      
      <section className="cta">
        <h2>Ready to boost your team's productivity?</h2>
        <p>
          Sign up today and experience project collaboration like never before!
        </p>
        <div className="cta-buttons">
          <button className="button signup-button">Register</button>
          <button className="button signin-button">Sign In</button>
        </div>
      </section>

      
      <footer>
        <p>&copy; {new Date().getFullYear()} Tracked. All rights reserved.</p>
      </footer>
    </div>
  );
};

export default HomePage;
