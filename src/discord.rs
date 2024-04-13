use std::env;
use reqwest::Client;
use serde_json::{json, Value};

pub(crate) async fn send(paste_url: &str, name: &str) -> String {
    let url = env::var("URL").unwrap();
    let client = Client::new();
    
    let content = format!("New paste created: [{}]({})", name, paste_url);
    
    let json = json!({
        "content": content,
    });
    
    let json_str = json.to_string();
    
    let result = client.post(url)
        .body(json_str)
        .send()
        .await.unwrap();
    
    let result_text = result.text().await.unwrap();
    let json: Value = serde_json::from_str(&result_text).unwrap();
    
    json.get("id").unwrap().to_string()
}

pub(crate) async fn delete(id: &str) {
    let url = env::var("URL").unwrap();
    let client = Client::new();
    
    let result = client.delete(format!("{}/{}", url, id))
        .send()
        .await.unwrap();
    
    println!("Deleted paste: {}", result.text().await.unwrap());
}