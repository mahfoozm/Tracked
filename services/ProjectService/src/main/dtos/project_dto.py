from pydantic import BaseModel
from typing import Optional

class ProjectCreateDTO(BaseModel):
    name: str
    description: str
    owner_id: int  # The owner must be an existing user

    class Config:
        orm_mode = True
