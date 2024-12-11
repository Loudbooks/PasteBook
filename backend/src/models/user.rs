use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct User {
    pub ip: String,
    pub id: String,
    pub requests: u64,
    pub created_at: i64,
    pub banned: bool,
}

#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct UserDTO {
    pub id: String,
    pub requests: u64,
    pub created_at: i64,
    pub banned: bool,
}

impl User {
    pub fn to_dto(&self) -> UserDTO {
        UserDTO {
            id: self.id.clone(),
            requests: self.requests,
            created_at: self.created_at,
            banned: self.banned,
        }
    }
}