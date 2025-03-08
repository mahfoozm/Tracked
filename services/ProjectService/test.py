from fastapi import FastAPI
import uvicorn

app = FastAPI()

@app.get("/")
def read_root():
    return {"Hello": "World"}

# Run the application
if __name__ == "__main__":
    uvicorn.run("test:app", host="0.0.0.0", port=5001, reload=True)
