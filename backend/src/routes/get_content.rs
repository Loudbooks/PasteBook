use actix_web::{get, web, HttpRequest, HttpResponse, Responder};
use std::sync::Arc;
use crate::utils::datautils;
use crate::utils::iputils::IPUtils;
use crate::controllers::get_controller::{get_metadata_handler};
use crate::database::aws_service::AWSService;
use crate::database::mongodb_service::MongoService;
use crate::controllers::get_controller::ContentQuery; // if needed


// Note this "route" url gets appended after the baseurl specified in `mod.rs` make sure that the URL is correct.

// Prepend the web::scope("/get") -> "/get/{id}/content"
#[get("/{id}/content")]
async fn get_content(
    aws_service: web::Data<Arc<AWSService>>,
    mongo_service: web::Data<Arc<MongoService>>,
    request: HttpRequest,
    path: web::Path<String>,
    query: web::Query<ContentQuery>,
) -> impl Responder {
    let compress = query.compress.unwrap_or(true);
    let ip = IPUtils::extract_ip(&request);

    if mongo_service.is_user_banned(&ip).await.expect("Failed to check if user is banned") {
        return HttpResponse::Forbidden().body("Prohibited");
    }

    mongo_service.increment_requests(&ip).await.expect("Failed to increment requests");

    match aws_service.get_file(&path).await {
        Ok(data) => {
            if compress {
                let compressed = datautils::compress_data(&data);
                HttpResponse::Ok()
                    .content_type("text/plain; charset=utf-8")
                    .append_header(("Content-Encoding", "gzip"))
                    .body(compressed)
            } else {
                HttpResponse::Ok()
                    .content_type("text/plain; charset=utf-8")
                    .body(data)
            }
        }
        Err(_) => HttpResponse::NotFound().body("File not found"),
    }
}


// Prepend the web::scope("/get") -> "/get/{id}/metadata"
#[get("/{id}/metadata")]
async fn get_metadata(
    mongo_service: web::Data<Arc<MongoService>>,
    request: HttpRequest,
    path: web::Path<String>,
) -> impl Responder {
    // Delegate to the controller's logic.
    get_metadata_handler(mongo_service, request, path).await
}