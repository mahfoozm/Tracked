from sqlalchemy.orm import Session
from sqlalchemy.exc import SQLAlchemyError
from ..repository.project_repository import create_project, get_project, delete_project
from ..model.project import Project, User  # Adjust the path based on your actual project structure
from ..dtos.project_dto import ProjectCreateDTO
from fastapi import HTTPException
import datetime

def create_project_service(db: Session, project_data: ProjectCreateDTO):
    # Create project in the repository
    owner = db.query(User).filter(User.id == project_data.owner_id).first()
    if not owner:
        raise HTTPException(status_code=400, detail=f"User with ID {project_data.owner_id} does not exist")

    
    project = create_project(db, project_data.dict())  # Passing the data as a dictionary
    return {
        "id": project.id,
        "name": project.name,
        "description": project.description,
        "owner_id": project.owner_id,
        "created_at": project.created_at.isoformat(),
        "updated_at": project.updated_at.isoformat()
    }

def get_project_service(db: Session, project_id: int):
    project = get_project(db, project_id)
    try:
        project = get_project(db, project_id)
        if not project:
            raise HTTPException(status_code=404, detail=f"Project with ID {project_id} not found")  # ✅ Return a 404

        return project
    except SQLAlchemyError as e:
        print(f"Database error: {e}")  # Debugging log
        raise HTTPException(status_code=500, detail="Database error occurred")

def delete_project_service(db: Session, project_id: int):
    project = get_project(db, project_id)
    if not project:
        raise HTTPException(status_code=404, detail="Project not found")
    
    delete_project(db, project)
    return {"message": f"Project {project_id} deleted successfully"}


def update_project_service(db: Session, project_id: int, project_data: ProjectCreateDTO):
    # Retrieve the project by its ID
    project = db.query(Project).filter(Project.id == project_id).first()
    
    if not project:
        raise HTTPException(status_code=404, detail="Project not found")

    # Update project fields
    project.name = project_data.name
    project.description = project_data.description
    project.updated_at = datetime.utcnow()  # Update the timestamp for when the project is updated

    # Commit the changes to the database
    db.commit()
    db.refresh(project)

    # Return the updated project information
    return {
        "id": project.id,
        "name": project.name,
        "description": project.description,
        "leadUserId": project.lead_user_id,
        "createdAt": project.created_at.isoformat(),
        "updatedAt": project.updated_at.isoformat()
    }