use sea_orm::entity::prelude::*;
use serde::{Deserialize, Serialize};
use crate::user::UserDTO;

#[derive(Debug, Clone, PartialEq, DeriveEntityModel)]
#[sea_orm(table_name = "pastes")]
pub struct Model {
    #[sea_orm(primary_key)]
    pub id: String,
    pub title: String,
    pub created: i64,
    pub report_book: bool,
    pub wrap: bool,
    pub creator_ip: String,
    pub expires_at: i64,
}

#[derive(Copy, Clone, Debug, EnumIter, DeriveRelation)]
pub enum Relation {}

impl ActiveModelBehavior for ActiveModel {}

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct PasteDTO {
    pub id: String,
    pub user: UserDTO,
    pub title: String,
    pub created: i64,
    pub report_book: bool,
    pub wrap: bool,
    pub expires_at: i64,
}

impl Model {
    pub fn to_public_dto(&self, user: UserDTO) -> PasteDTO {
        PasteDTO {
            id: self.id.clone(),
            user,
            title: self.title.clone(),
            created: self.created,
            report_book: self.report_book,
            wrap: self.wrap,
            expires_at: self.expires_at,
        }
    }
}