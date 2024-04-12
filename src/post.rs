use std::fs;
use std::io::Write;
use std::time::{SystemTime, UNIX_EPOCH};
use axum::http::HeaderMap;

use random_word::Lang;
use serde_json::json;

pub async fn post(headers: HeaderMap, content: String) -> String {
    let data_dir = "./pastes/".to_string();
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

    let value = json!({
        "created": since_the_epoch.as_millis().to_string(),
        "content": content,
        "title": headers.get("title").unwrap().to_str().unwrap()
    });

    let mut file = fs::File::create(&path).unwrap();
    file.write_all(value.to_string().as_bytes()).unwrap();

    format!("https://paste.loudbook.dev/pastes/{}", filename)
}