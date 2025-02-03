use actix_web::{get, web, HttpRequest, HttpResponse, Responder};
use std::sync::Arc;
use crate::utils::data::DataUtils;
use crate::utils::ip::IPUtils;
use crate::database::aws_service::AWSService;
use crate::database::mongodb_service::MongoService;
use crate::types::content;

#[get("/{id}/content")]
async fn get_content(
    aws_service: web::Data<Arc<AWSService>>,
    mongo_service: web::Data<Arc<MongoService>>,
    request: HttpRequest,
    path: web::Path<String>,
    query: web::Query<content::ContentQuery>,
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
                let compressed = DataUtils::compress_data(&data);
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

#[get("/{id}/metadata")]
async fn get_metadata(
    mongo_service: web::Data<Arc<MongoService>>,
    request: HttpRequest,
    path: web::Path<String>,
) -> impl Responder {
    let ip = IPUtils::extract_ip(&request);
    if mongo_service.is_user_banned(&ip).await.expect("Failed to check if user is banned") {
        return HttpResponse::Forbidden().body("Prohibited");
    }

    match mongo_service.get_paste_metadata(&path).await {
        Ok(Some(metadata)) => {
            let user = mongo_service.get_user(&metadata.creator_ip).await.unwrap();

            let public_dto = metadata.to_public_dto(user.unwrap().to_dto());
            HttpResponse::Ok().json(public_dto)
        }
        Ok(None) => HttpResponse::NotFound().body("Not Found"),
        Err(_) => HttpResponse::InternalServerError().body("Internal Server Error"),
    }
}