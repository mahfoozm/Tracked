from fastapi import Request, HTTPException
from fastapi.responses import JSONResponse
import traceback  # Import for debugging
from sqlalchemy.exc import SQLAlchemyError


async def not_found_exception_handler(request: Request, exc: Exception):
    print(f"🔴 Exception occurred: {exc.__class__.__name__}: {str(exc)}")  # Debugging log
    print(traceback.format_exc())  # Print the full traceback for deeper debugging

    if isinstance(exc, HTTPException):
        return JSONResponse(
            status_code=exc.status_code,
            content={"message": exc.detail}
        )
    elif isinstance(exc, SQLAlchemyError):  # Handle database-related errors
        return JSONResponse(
            status_code=500,
            content={"message": "Database error occurred", "error": str(exc)}
        )
    else:
        return JSONResponse(
            status_code=500,
            content={"message": "An unexpected error occurred.", "error": str(exc)}
        )
