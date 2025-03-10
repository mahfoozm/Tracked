from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from ..config.database import SessionLocal
from ..service.project_service import create_project_service, get_project_service, delete_project_service, update_project_service
from ..dtos.project_dto import ProjectCreateDTO
from fastapi import HTTPException

router = APIRouter()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.post("/projects")
def create_project(project_data: ProjectCreateDTO, db: Session = Depends(get_db)):
    return create_project_service(db, project_data)

@router.get("/projects/{project_id}")
def get_project(project_id: int, db: Session = Depends(get_db)):
    return get_project_service(db, project_id)  

@router.delete("/projects/{project_id}")
def delete_project(project_id: int, db: Session = Depends(get_db)):
    try:
        return delete_project_service(db, project_id)
    except HTTPException as e:
        raise e  # Rethrow the exception to be handled properly


@router.put("/projects/{project_id}")
def update_project(project_id: str, project_data: ProjectCreateDTO, db: Session = Depends(get_db)):
    # Call the service to update the project by its ID
    updated_project = update_project_service(db, project_id, project_data)

    if updated_project is None:
        raise HTTPException(status_code=404, detail="Project not found")

    return {
        "id": updated_project.id,
        "name": updated_project.name,
        "description": updated_project.description,
        "leadUserId": updated_project.lead_user_id,
        "createdAt": updated_project.created_at.isoformat(),
        "updatedAt": updated_project.updated_at.isoformat()
    }
