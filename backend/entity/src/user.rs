use sea_orm::entity::prelude::*;
use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, PartialEq, DeriveEntityModel)]
#[sea_orm(table_name = "users")]
pub struct Model {
    #[sea_orm(primary_key)]
    pub ip: String,
    pub id: String,
    pub requests: i64,
    pub created_at: i64,
    pub banned: bool,
}

#[derive(Copy, Clone, Debug, EnumIter, DeriveRelation)]
pub enum Relation {}

impl ActiveModelBehavior for ActiveModel {}

impl Model {
    pub fn to_dto(&self) -> UserDTO {
        UserDTO {
            id: self.id.clone(),
            requests: self.requests,
            created_at: self.created_at,
            banned: self.banned,
        }
    }
}

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct UserDTO {
    pub id: String,
    pub requests: i64,
    pub created_at: i64,
    pub banned: bool,
}
