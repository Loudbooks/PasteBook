mod aws_service;
mod mongodb_service;
mod models;
mod controllers;
mod mongoresult;
mod utils;
mod delete_service;

use crate::aws_service::AWSService;
use crate::controllers::get_controller::{get_content_handler, get_metadata_handler};
use crate::controllers::upload_controller::upload_handler;
use crate::delete_service::DeleteHandler;
use crate::mongodb_service::MongoService;
use actix_web::{web, App, HttpResponse, HttpServer, Responder};
use dotenv::dotenv;
use std::env;
use std::sync::Arc;
use aws_sdk_s3::types::builders::CorsRuleBuilder;

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    env_logger::init();
    dotenv().ok();

    let database_url = env::var("S3_ENDPOINT").expect("S3_ENDPOINT must be set");
    let aws_access_key = env::var("S3_ACCESS_KEY_ID").expect("S3_ACCESS_KEY_ID must be set");
    let aws_secret_key = env::var("S3_SECRET_ACCESS_KEY").expect("S3_SECRET_ACCESS_KEY must be set");
    let bucket_name = env::var("S3_BUCKET").expect("S3_BUCKET must be set");
    let mongo_url = env::var("SPRING_DATA_MONGODB_URI").expect("SPRING_DATA_MONGODB_URI must be set");

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

    let mongo_service = Arc::new(
        MongoService::new(
            &mongo_url,
            &bucket_name,
        )
            .await
            .expect("Failed to initialize MongoService")
    );

    let delete_handler = DeleteHandler::new(Arc::clone(&aws_service), Arc::clone(&mongo_service));
    delete_handler.start_delete_loop();

    let cors = CorsRuleBuilder::default()
        .allowed_origins(vec!["*"])
        .allowed_methods(vec!["GET", "POST"])
        .allowed_headers(vec!["*"])
        .build();

    HttpServer::new(move || {
        App::new().wrap(cors)
            .app_data(web::Data::new(Arc::clone(&aws_service)))
            .app_data(web::Data::new(Arc::clone(&mongo_service)))
            .route("/get/{id}/content", web::get().to(get_content_handler))
            .route("/get/{id}/metadata", web::get().to(get_metadata_handler))
            .route("/upload", web::post().to(upload_handler))
    })
        .bind(("0.0.0.0", 8080))?
        .run()
        .await
}

async fn health_check() -> impl Responder {
    println!("Health check");
    HttpResponse::Ok().body("OK")
}