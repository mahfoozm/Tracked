# Use an official Python runtime
FROM python:3.8

# Set the working directory
WORKDIR /app

# Install dependencies directly in the Dockerfile
RUN pip install --no-cache-dir fastapi uvicorn sqlalchemy psycopg2 python-dotenv

# Copy the rest of the application
COPY . .

# Set PYTHONPATH so Docker knows where to find `src/`
ENV PYTHONPATH=/app/src

# Expose the FastAPI port
EXPOSE 5001

# Run FastAPI using Uvicorn
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "5001", "--reload"]
