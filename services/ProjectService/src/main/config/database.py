from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from dotenv import load_dotenv
import os

# Load environment variables from the .env file
load_dotenv()

class Database:
    """Class to encapsulate the database setup and session management."""

    # Define the base class for your SQLAlchemy models
    Base = declarative_base()

    def __init__(self):
        # Fetch the database URL from the environment variables
        self.database_url = os.getenv("DATABASE_URL")

        # Ensure the URL is provided
        if not self.database_url:
            raise ValueError("DATABASE_URL is not set in the environment variables")

        # Initialize the database engine and session
        self.engine = create_engine(self.database_url, echo=True, future=True)
        self.SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=self.engine)

    def initialize_db(self):
        """Initializes the database connection and creates tables if they don't exist."""
        try:
            # Create all tables in the database
            self.Base.metadata.create_all(bind=self.engine)
            print("Database connection successful and tables created!")
        except Exception as e:
            print("Failed to initialize the database!")
            print(e)

# Initialize the database object
db = Database()

# Expose the session local for use in other parts of the app
SessionLocal = db.SessionLocal

Base = db.Base


if __name__ == "__main__":
    db.initialize_db()
