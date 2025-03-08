from ..model.project import Project
from sqlalchemy.orm import Session

def create_project(db: Session, project_data):
    new_project = Project(**project_data)
    db.add(new_project)
    db.commit()
    db.refresh(new_project)
    return new_project

def get_project(db: Session, project_id: int):
    return db.query(Project).filter(Project.id == project_id).first()

def delete_project(db: Session, project: Project):
    db.delete(project)
    db.commit()
