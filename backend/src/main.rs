mod models;
mod utils;
mod delete_service;
mod database;
mod routes;

use crate::routes::configure_routes;
use crate::delete_service::DeleteHandler;
use database::postgres_service::PostgresService;
use actix_cors::Cors;
use actix_web::{web, App, HttpServer};
use std::env;
use std::sync::Arc;

#[cfg(feature="local-dev")]
fn load_env() {
    dotenv::dotenv().ok();
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    println!("PasteBook backend service starting...");

    env_logger::init();
    
    #[cfg(feature="local-dev")]
    load_env();

    let postgres_uri = env::var("POSTGRES_URI").expect("POSTGRES_URI must be set");
    let max_payload_size = env::var("MAX_PAYLOAD_SIZE").unwrap_or("10".to_string()).parse::<usize>().unwrap();
    
    let postgres_service = Arc::new(
        PostgresService::new(
            &postgres_uri,
        )
            .await
            .expect("Failed to initialize PostgresService")
    );

    DeleteHandler::new(Arc::clone(&postgres_service));
    
    println!("Pre-bind complete. Starting server.");
    
    HttpServer::new(move || {
        let cors = Cors::permissive();

        App::new()
            .wrap(cors)
            .app_data(web::PayloadConfig::default().limit(max_payload_size * 1024 * 1024))
            .app_data(web::Data::new(Arc::clone(&postgres_service)))
            .configure(configure_routes)
    })
        .bind(("::", 8080))?
        .run()
        .await
}