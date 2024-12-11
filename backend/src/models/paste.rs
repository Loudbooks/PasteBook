use serde::{Deserialize, Serialize};
use crate::models::user::UserDTO;

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct Paste {
    pub id: String,
    pub title: String,
    pub created: u64,
    pub report_book: bool,
    pub wrap: bool,
    pub creator_ip: String,
    #[serde(rename = "expiresAt")]
    pub expires_at: u64,
}

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct PasteDTO {
    pub id: String,
    pub user: UserDTO,
    pub title: String,
    pub created: u64,
    pub report_book: bool,
    pub wrap: bool,
    #[serde(rename = "expiresAt")]
    pub expires_at: u64,
}

impl Paste {
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
