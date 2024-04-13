use std::fs;
use std::io::Write;
use std::time::{SystemTime, UNIX_EPOCH};
use axum::http::HeaderMap;

use random_word::Lang;
use serde_json::json;
use crate::discord;

pub async fn post(headers: HeaderMap, content: String) -> String {
    const PATH: &str = "/home/loudbook/pastebook/pastebook";

    let data_dir = format!("{}/pastes/", PATH);
    let filename = format!("{}-{}-{}", random_word::gen(Lang::En), random_word::gen(Lang::En), random_word::gen(Lang::En));
    let path = format!("{}{}.json", data_dir, filename);
    println!("{:?}", headers);

    if !fs::metadata(&data_dir).is_ok() {
        fs::create_dir(&data_dir).unwrap();
    }

    let start = SystemTime::now();
    let since_the_epoch = start
        .duration_since(UNIX_EPOCH)
        .expect("Time went backwards");

    let url = format!("https://paste.loudbook.dev/pastes/{}", filename);
    let title = headers.get("title").unwrap().to_str().unwrap();

    let id = discord::send(&url, title).await;
    
    let value = json!({
        "created": since_the_epoch.as_millis().to_string(),
        "content": content,
        "title": title,
        "id": id
    });

    let mut file = fs::File::create(&path).unwrap();
    file.write_all(value.to_string().as_bytes()).unwrap();
    
    url
}