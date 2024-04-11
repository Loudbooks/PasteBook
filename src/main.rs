mod post;
mod get;

use axum::Router;
use axum::routing::{get, post};

#[tokio::main]
async fn main() {
    let app = Router::new()
        .route("/upload", post(post::post))
        .route("/get/:data", get(get::get));

    let listener = tokio::net::TcpListener::bind("0.0.0.0:25658").await.unwrap();
    axum::serve(listener, app).await.unwrap();
}
