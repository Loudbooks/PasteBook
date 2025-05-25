use sea_orm::entity::prelude::*;
use serde::{Deserialize, Serialize};
use crate::user_metadata::UserDTO;

#[derive(Debug, Clone, PartialEq, DeriveEntityModel)]
#[sea_orm(table_name = "paste_metadata")]
pub struct Model {
    #[sea_orm(primary_key)]
    pub id: String,
    pub title: String,
    pub created: i64,
    pub wrap: bool,
    pub creator_ip: String,
    pub expires_at: i64,
    pub burn: bool,
}

#[derive(Copy, Clone, Debug, EnumIter, DeriveRelation)]
pub enum Relation {}

impl ActiveModelBehavior for ActiveModel {}

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct PasteDTO {
    pub id: String,
    pub title: String,
    pub user: UserDTO,
    pub created: i64,
    pub wrap: bool,
    pub expires_at: i64,
    pub burn: bool,
}

impl Model {
    pub fn to_public_dto(&self, user: UserDTO) -> PasteDTO {
        PasteDTO {
            id: self.id.clone(),
            title: self.title.clone(),
            user,
            created: self.created,
            wrap: self.wrap,
            expires_at: self.expires_at,
            burn: self.burn,
        }
    }
}