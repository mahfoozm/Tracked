from fastapi import FastAPI
from src.main.config.database import Database
from src.main.controller.project_controller import router as project_router
from src.main.exceptions.error_handler import not_found_exception_handler
from fastapi.exceptions import RequestValidationError
from sqlalchemy.exc import SQLAlchemyError
import uvicorn


# Initialize FastAPI app
app = FastAPI(title="Project Service", version="1.0")

# Initialize Database
db = Database()
db.initialize_db()

# Register controllers (routes)
app.include_router(project_router, prefix="/api/v1")

# Register error handlers
app.add_exception_handler(RequestValidationError, not_found_exception_handler)
app.add_exception_handler(SQLAlchemyError, not_found_exception_handler)

# Run the application
if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=5001, reload=True)
