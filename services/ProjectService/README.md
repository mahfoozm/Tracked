# **Tracked Project Service**

A FastAPI-based microservice for managing tracked projects.

## **🚀 Setup Instructions**

### **1️⃣ Install Dependencies**
Ensure you have the following installed:
- [Docker](https://www.docker.com/get-started)
- Python 3.8+ (Optional, if running locally)

---

### **2️⃣ Running the Service with Docker**
#### **🔹 Build the Docker Image**
Run the following command to build the Docker image:
```sh
docker build -t tracked-project-service .
```

#### **🔹 Run the Container**
```sh
docker run -p 5001:5001 tracked-project-service
```

If you want **auto-reloading** for development:
```sh
docker run -p 5001:5001 -v $(pwd):/app tracked-project-service uvicorn ProjectService.main:app --host 0.0.0.0 --port 5001 --reload
```

---

### **3️⃣ Running the Service Locally (Without Docker)**
If you prefer running FastAPI without Docker:
1. Install dependencies:
    ```sh
    pip install -r requirements.txt
    ```
2. Run the service:
    ```sh
    uvicorn ProjectService.main:app --host 0.0.0.0 --port 5001 --reload
    ```

---

### **4️⃣ API Endpoints**
| Method | Endpoint             | Description             |
|--------|----------------------|-------------------------|
| GET    | `/health`            | Check if the service is running |
| POST   | `/projects`          | Create a new project   |
| GET    | `/projects/{id}`     | Get project details    |
| DELETE | `/projects/{id}`     | Delete a project       |

Use [Swagger UI](http://localhost:5001/docs) to explore the API.

---

### **5️⃣ Debugging**
- To check running containers:
    ```sh
    docker ps
    ```
- To enter the container shell:
    ```sh
    docker exec -it tracked-project-service bash
    ```
- To view logs:
    ```sh
    docker logs tracked-project-service
    ```

---

### **6️⃣ Notes**
- If `main.py` is not found, ensure it’s inside `ProjectService/` and correctly referenced in `uvicorn ProjectService.main:app`.
- `docker-compose.yml` is temporarily removed in my branch. The full setup should be restored when integrating all services.
