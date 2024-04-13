use std::fs::DirEntry;
use std::time::Duration;

use axum::Router;
use axum::routing::{get, post};
use serde_json::Value;
use tokio::fs;
use tokio::task;

mod post;
mod get;

#[tokio::main]
async fn main() {
    let app = Router::new()
        .route("/upload", post(post::post))
        .route("/get/:data", get(get::get));

    delete_files().await;
    delete_loop().await;
    
    let listener = tokio::net::TcpListener::bind("0.0.0.0:25658").await.unwrap();
    axum::serve(listener, app).await.unwrap();
}


async fn delete_loop() {
    let forever = task::spawn(async {
        let mut interval = tokio::time::interval(Duration::from_secs(1800));

        loop {
            interval.tick().await;
            delete_files().await;
        }
    });

    forever.await.expect("Failed to loop");
}

const PATH: &str = "/home/loudbook/pastebook/pastebook";
const DELETION: u128 = 1000 * 60 * 60 * 9;

async fn delete_files() {
    let paths = std::fs::read_dir(format!("{}/pastes", PATH)).unwrap();

    println!("Beginning deletion...");
    
    let mut amount_deleted = 0;
    
    for path in paths {
        if path.is_err() {
            continue;
        }

        let path = path.unwrap();

        let file_name = path.file_name();
        let file_name = file_name.to_str().unwrap();

        let content = fs::read_to_string(format!("{}/pastes/{}", PATH, file_name.to_string()));

        if content.await.is_err() {
            continue;
        }

        let created = get_creation(&path).await;

        if created.is_none() {
            continue;
        }

        let created = created.unwrap();
        let created = created.parse::<u128>();

        if created.is_err() {
            continue;
        }

        let created = created.unwrap();
        let now = std::time::SystemTime::now().duration_since(std::time::UNIX_EPOCH).unwrap().as_millis();

        if now.checked_sub(created).unwrap_or(u128::MAX) > DELETION {
            fs::remove_file(format!("{}/pastes/{}", PATH, file_name.to_string())).await.expect("Failed to delete file");
            amount_deleted += 1;
        }
    }
    
    println!("Deleted {} files", amount_deleted);
}

async fn get_creation(path: &DirEntry) -> Option<String> {
    let file_name = path.file_name();
    let file_name = file_name.to_str().unwrap_or("");

    let content = fs::read_to_string(format!("{}/pastes/{}", PATH, file_name.to_string())).await;
    
    if content.is_err() {
        return None
    }
    
    let json: Value = serde_json::from_str(&*content.unwrap().to_string()).unwrap_or(Value::Null);

    if json.is_null() {
        return None
    }

    let created = json.get("created").unwrap_or(&Value::Null);

    if created.is_null() {
        return None
    }

    let max = i64::MAX.to_string();
    let created = created.as_str().unwrap_or(max.as_str());

    Some(created.to_string())
}