use log::info;
use mongodb::bson::{doc, uuid};
use mongodb::{Client, Collection, Cursor};
use mongodb::options::ClientOptions;
use crate::database::migration_service::MigrationService;
use crate::models::paste::Paste;
use crate::models::user::User;
use crate::database::mongoresult::MongoResult;

pub struct MongoService {
    user_collection: Collection<User>,
    paste_collection: Collection<Paste>,
}

impl MongoService {
    pub async fn new(uri: &str, db_name: &str) -> MongoResult<Self> {
        info!("Connecting to MongoDB...");
        let client_options = ClientOptions::parse(uri).await?;
        let client = Client::with_options(client_options)?;

        let database = client.database(db_name);
        let admin_database = client.database("admin");
        let user_collection = database.collection::<User>("users");
        let paste_collection = database.collection::<Paste>("pastes");

        info!("Connected to MongoDB.");
        
        let migration_service = MigrationService::new(&database, &admin_database, &user_collection, &paste_collection);
        migration_service.run_migrations().await;

        Ok(Self {
            user_collection,
            paste_collection,
        })
    }

    pub async fn increment_requests(&self, ip: &str) -> MongoResult<()> {
        let filter = doc! { "ip": ip };
        let update = doc! {
            "$inc": { "requests": 1 },
            "$setOnInsert": {
                "_id": uuid::Uuid::new().to_string(),
                "created_at": chrono::Utc::now().timestamp_millis(),
                "banned": false
            }
        };

        self.user_collection.update_one(filter, update).upsert(true).await?;
        Ok(())
    }

    pub async fn put_paste(&self, paste: Paste) -> MongoResult<()> {
        self.paste_collection.insert_one(paste).await?;
        Ok(())
    }

    pub async fn delete_paste(&self, id: &str) -> MongoResult<()> {
        self.paste_collection.delete_one(doc! { "id": id }).await?;
        Ok(())
    }

    pub async fn is_user_banned(&self, ip: &str) -> MongoResult<bool> {
        if let Some(user) = self.user_collection.find_one(doc! { "ip": ip }).await? {
            Ok(user.banned)
        } else {
            Ok(false)
        }
    }

    pub async fn get_user(&self, ip: &str) -> MongoResult<Option<User>> {
        let user = self.user_collection.find_one(doc! { "ip": ip }).await?;
        Ok(user)
    }

    pub async fn get_paste_metadata(&self, id: &str) -> MongoResult<Option<Paste>> {
        let paste = self.paste_collection.find_one(doc! { "id": id }).await?;
        Ok(paste)
    }

    pub async fn get_all_pastes_metadata(&self) -> MongoResult<Cursor<Paste>> {
        let cursor = self
            .paste_collection
            .find(doc! {})
            .await?;
        Ok(cursor)
    }
}