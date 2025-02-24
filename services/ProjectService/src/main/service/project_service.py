from sqlalchemy.orm import Session
from repository.project_repository import create_project, get_project
from dtos.project_dto import ProjectCreateDTO
from fastapi import HTTPException

def create_project_service(db: Session, project_data: ProjectCreateDTO):
    return create_project(db, project_data.dict())

def get_project_service(db: Session, project_id: int):
    project = get_project(db, project_id)
    if not project:
        raise Exception("Project not found")
    return project

def delete_project_service(db: Session, project_id: int):
    project = get_project(db, project_id)
    if not project:
        raise HTTPException(status_code=404, detail="Project not found")
    
    delete_project(db, project)
    return {"message": f"Project {project_id} deleted successfully"}
