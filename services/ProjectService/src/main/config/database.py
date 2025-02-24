from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base

# Database connection URL (Replace with actual credentials)
DATABASE_URL = "postgresql://user:password@db/projects_db"

# Initialize SQLAlchemy engine
engine = create_engine(DATABASE_URL)

# Create a configured session class
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# Add this line to define the Base class
Base = declarative_base()
