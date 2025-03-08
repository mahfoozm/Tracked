from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from ..config.database import SessionLocal
from ..service.project_service import create_project_service, get_project_service, delete_project_service
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

