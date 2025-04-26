mod models;
mod utils;
mod delete_service;
mod database;
mod routes;

use crate::routes::configure_routes;
use database::aws_service::AWSService;
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

    let database_url = env::var("S3_ENDPOINT").expect("S3_ENDPOINT must be set");
    let aws_access_key = env::var("S3_ACCESS_KEY_ID").expect("S3_ACCESS_KEY_ID must be set");
    let aws_secret_key = env::var("S3_SECRET_ACCESS_KEY").expect("S3_SECRET_ACCESS_KEY must be set");
    let bucket_name = env::var("S3_BUCKET").expect("S3_BUCKET must be set");
    let postgres_uri = env::var("POSTGRES_URI").expect("POSTGRES_URI must be set");
    let max_payload_size = env::var("MAX_PAYLOAD_SIZE").unwrap_or("10".to_string()).parse::<usize>().unwrap();

    let aws_service = Arc::new(
        AWSService::new(
            &database_url,
            &bucket_name,
            &aws_access_key,
            &aws_secret_key
        )
            .await
            .expect("Failed to initialize AWSService"),
    );

    aws_service.create_bucket(&bucket_name).await.expect("Failed to create bucket");

    let postgres_service = Arc::new(
        PostgresService::new(
            &postgres_uri,
        )
            .await
            .expect("Failed to initialize PostgresService")
    );

    DeleteHandler::new(Arc::clone(&aws_service), Arc::clone(&postgres_service));
    
    println!("Pre-bind complete. Starting server.");
    
    HttpServer::new(move || {
        let cors = Cors::permissive();

        App::new()
            .wrap(cors)
            .app_data(web::PayloadConfig::default().limit(max_payload_size * 1024 * 1024))
            .app_data(web::Data::new(Arc::clone(&aws_service)))
            .app_data(web::Data::new(Arc::clone(&postgres_service)))
            .configure(configure_routes)
    })
        .bind(("::", 8080))?
        .run()
        .await
}