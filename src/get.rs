use std::fs;
use axum::extract::Path;
use axum::response::Html;

pub async fn get(Path(path): Path<String>) -> Html<String> {
    let dir = format!("./pastes/{}", path);
    let file = format!("{}.txt", dir);

    let content = fs::read_to_string(file).unwrap();
    Html(content)
}