from fastapi import HTTPException

def project_not_found_exception():
    return HTTPException(status_code=404, detail="Project not found")
