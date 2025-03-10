from ..model.project import Project
from sqlalchemy.orm import Session

def create_project(db: Session, project_data):
    new_project = Project(**project_data)
    db.add(new_project)
    db.commit()
    db.refresh(new_project)
    return new_project

def get_project(db: Session, project_id: int):
    project = db.query(Project).filter(Project.id == project_id).first()
    return project  # ✅ Returns None if the project does not exist

def delete_project(db: Session, project: Project):
    db.delete(project)
    db.commit()

def update_project(db: Session, project_id: int, project_data: dict):
    project = db.query(Project).filter(Project.id == project_id).first()

    if not project:
        return None

    for key, value in project_data.items():
        setattr(project, key, value)

    db.commit()
    db.refresh(project)

    return project
