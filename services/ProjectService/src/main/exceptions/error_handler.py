from fastapi import HTTPException

def not_found_exception_handler():
    return HTTPException(status_code=404, detail="Project not found")
