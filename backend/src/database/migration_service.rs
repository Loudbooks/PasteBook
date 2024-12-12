use crate::models::paste::Paste;
use crate::models::user::User;
use futures_util::StreamExt;
use mongodb::bson::{doc, Document};
use mongodb::{Collection, Database};

pub struct MigrationService {
    database: Database,
    admin_database: Database,
    users_collection: Collection<User>,
    pastes_collection: Collection<Paste>,
}

impl MigrationService {
    pub fn new(database: &Database, admin_database: &Database, users_collection: &Collection<User>, pastes_collection: &Collection<Paste>) -> Self {
        Self {
            database: database.clone(),
            admin_database: admin_database.clone(),
            users_collection: users_collection.clone(),
            pastes_collection: pastes_collection.clone(),
        }
    }
    
    pub async fn run_migrations(&self) {
        self.user_migration_12_11_24().await.map_err(|e| println!("Error migrating users: {}", e)).ok();
        self.paste_migration_12_11_24().await.map_err(|e| println!("Error migrating pastes: {}", e)).ok();
    }
    
    pub async fn user_migration_12_11_24(&self) -> Result<(), mongodb::error::Error> {
        if !self.database.list_collection_names().await?.contains(&"user".to_string()) {
            println!("No users to migrate.");
            return Ok(());
        }
        
        println!("Migrating users...");
        let old_user_collection = self.database.collection::<Document>("user");

        let mut amount = 0;
        let target_amount = old_user_collection.count_documents(doc! {}).await?;
        let mut cursor = old_user_collection.find(doc! {}).await?;

        while let Some(old_user) = cursor.next().await {
            let old_user = old_user?;
            let user_ip = old_user.get_str("_id").unwrap_or_default();
            let user_id = old_user.get_str("id").unwrap_or_default();
            let user_requests = old_user.get_i32("requests").unwrap_or_default();
            let user_banned = old_user.get_bool("banned").unwrap_or_default();
            let last_visit = old_user.get_i64("lastVisit").unwrap_or_default();
            
            if user_ip.is_empty() || user_id.is_empty() {
                println!("Skipping user with empty IP or ID.");
                continue;
            }

            let user = User {
                ip: user_ip.to_string(),
                id: user_id.to_string(),
                requests: user_requests as u64,
                created_at: last_visit,
                banned: user_banned,
            };

            self.users_collection.insert_one(user).await?;

            amount += 1;
        }

        println!("Migrated {} out of {} users", amount, target_amount);
        
        let qualified_old_database_name = self.database.name().to_owned() + ".user";
        let qualified_new_database_name = self.database.name().to_owned() + ".user_migrated_12_11_24";
        
        self.admin_database.run_command(doc! {
            "renameCollection": qualified_old_database_name,
            "to": qualified_new_database_name,
            "dropTarget": true
        }).await?;

        Ok(())
    }
    
    pub async fn paste_migration_12_11_24(&self) -> Result<(), mongodb::error::Error> {
        if !self.database.list_collection_names().await?.contains(&"pastePrivateDTO".to_string()) {
            println!("No pastes to migrate.");
            return Ok(());
        }
        
        println!("Migrating pastes...");
        let old_paste_collection = self.database.collection::<Document>("pastePrivateDTO");

        let mut amount = 0;
        let target_amount = old_paste_collection.count_documents(doc! {}).await?;
        let mut cursor = old_paste_collection.find(doc! {}).await?;

        while let Some(old_paste) = cursor.next().await {
            let old_paste = old_paste?;
            let paste_id = old_paste.get_str("_id").unwrap_or_default();
            let paste_expires_at = old_paste.get_i64("expires").unwrap_or_default();
            let paste_created_at = old_paste.get_i64("created").unwrap_or_default();
            let paste_creator_ip = old_paste.get_str("creatorIP").unwrap_or_default();
            let paste_report_book = old_paste.get_bool("reportBook").unwrap_or_default();
            let paste_wrap = old_paste.get_bool("wrap").unwrap_or_default();
            
            if paste_id.is_empty() {
                println!("Skipping paste with empty ID.");
                continue;
            }

            let paste = Paste {
                id: paste_id.to_string(),
                title: "".to_string(),
                created: paste_created_at as u64,
                report_book: paste_report_book,
                wrap: paste_wrap,
                creator_ip: paste_creator_ip.to_string(),
                expires_at: paste_expires_at as u64,
            };

            self.pastes_collection.insert_one(paste).await?;
            
            amount += 1;
        }

        println!("Migrated {} out of {} pastes", amount, target_amount);
        
        let qualified_old_database_name = self.database.name().to_owned() + ".pastePrivateDTO";
        let qualified_new_database_name = self.database.name().to_owned() + ".pastePrivateDTO_migrated_12_11_24";
        
        self.admin_database.run_command(doc! {
            "renameCollection": qualified_old_database_name,
            "to": qualified_new_database_name,
            "dropTarget": true
        }).await?;
        
        Ok(())
    }
}