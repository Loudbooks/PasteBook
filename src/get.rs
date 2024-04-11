use std::fs;
use std::time::SystemTime;
use axum::extract::Path;
use axum::response::Html;
use chrono::DateTime;
use serde_json::{json, Value};

pub async fn get(Path(path): Path<String>) -> Html<String> {
    let dir = format!("./pastes/{}", path);
    let file = format!("{}.txt", dir);

    let content = fs::read_to_string(&file).unwrap();
    let creation = fs::metadata(file).unwrap().created();

    let system_time = SystemTime::now();

    let creation_str = if creation.is_ok() {
        let date_time: DateTime<chrono::Utc> = creation.unwrap_or(SystemTime::now()).into();
        date_time.format("%Y-%m-%d %H:%M:%S").to_string()
    } else {
        "Unknown".to_string()
    };

    Html(json!(
        {
            "content": content,
            "creation": creation_str
        }
    ).to_string())
}