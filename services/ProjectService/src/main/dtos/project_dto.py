from pydantic import BaseModel
from typing import Optional

class ProjectCreateDTO(BaseModel):
    name: str
    description: Optional[str]
    lead_user_id: int
