use sea_orm::*;
use uuid::Uuid;
use chrono::Utc;
use ::entity::{paste, user};
use log::info;
use migration::{Migrator, MigratorTrait};

pub struct PostgresService {
    db: DatabaseConnection,
}

impl PostgresService {
    pub async fn new(uri: &str) -> Result<Self, DbErr> {
        info!("Connecting to PostgreSQL...");
        let db = Database::connect(uri).await?;
        info!("Running migrations...");
        Migrator::up(&db, None).await?;
        info!("Connected to PostgreSQL.");
        Ok(Self { db })
    }

    pub async fn increment_requests(&self, ip_str: &str) -> Result<(), DbErr> {
        use user::Entity as User;

        if let Some(user) = User::find_by_id(ip_str).one(&self.db).await? {
            let mut active_user: user::ActiveModel = user.into_active_model();
            let requests = active_user.requests.take().unwrap_or(0);
            active_user.requests = Set(requests + 1);
            
            active_user.update(&self.db).await?;
        } else {
            let user = user::ActiveModel {
                ip: Set(ip_str.to_string()),
                id: Set(Uuid::new_v4().to_string()),
                created_at: Set(Utc::now().timestamp_millis()),
                banned: Set(false),
                requests: Set(1),
            };
            
            user.insert(&self.db).await?;
        }
        Ok(())
    }

    pub async fn put_paste(&self, paste: paste::ActiveModel) -> Result<(), DbErr> {
        paste.insert(&self.db).await?;
        Ok(())
    }

    pub async fn delete_paste(&self, id_str: &str) -> Result<(), DbErr> {
        use paste::Entity as Paste;
        Paste::delete_by_id(id_str).exec(&self.db).await?;
        Ok(())
    }

    pub async fn is_user_banned(&self, ip: &str) -> Result<bool, DbErr> {
        use user::Entity as User;
        let user = User::find_by_id(ip).one(&self.db).await?;
        Ok(user.map(|u| u.banned).unwrap_or(false))
    }

    pub async fn get_user(&self, ip: &str) -> Result<Option<user::Model>, DbErr> {
        user::Entity::find_by_id(ip).one(&self.db).await
    }

    pub async fn get_paste_metadata(&self, id: &str) -> Result<Option<paste::Model>, DbErr> {
        paste::Entity::find_by_id(id).one(&self.db).await
    }

    pub async fn get_all_pastes_metadata(&self) -> Result<Vec<paste::Model>, DbErr> {
        paste::Entity::find().all(&self.db).await
    }
}
