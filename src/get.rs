use tokio::fs;
use axum::extract::Path;
use axum::response::Html;
use serde_json::{json, Value};

pub async fn get(Path(path): Path<String>) -> Html<String> {
    const PATH: &str = "/home/loudbook/pastebook/pastebook";
    
    let dir = format!("{}/pastes/{}", PATH, path);
    let file = format!("{}.json", dir);

    let content: Value = serde_json::from_str(fs::read_to_string(&file).await.unwrap().as_str()).unwrap();

    Html(json!(
        {
            "content": content.get("content").unwrap().as_str().unwrap(),
            "title": content.get("title").unwrap().as_str().unwrap(),
            "created": content.get("created").unwrap().as_str().unwrap(),
        }
    ).to_string())
}