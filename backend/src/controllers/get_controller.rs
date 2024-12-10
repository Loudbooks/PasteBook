use crate::aws_service::AWSService;
use actix_web::{
    web, HttpRequest, HttpResponse, Responder,
};
use serde::Deserialize;
use std::sync::Arc;
use crate::mongodb_service::MongoService;

#[derive(Deserialize)]
pub struct ContentQuery {
    pub compress: Option<bool>,
}

pub async fn get_metadata_handler(
    mongo_service: web::Data<Arc<MongoService>>,
    request: HttpRequest,
    path: web::Path<String>,
) -> impl Responder {
    let ip = extract_ip(&request);
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

pub async fn get_content_handler(
    aws_service: web::Data<Arc<AWSService>>,
    mongo_service: web::Data<Arc<MongoService>>,
    request: HttpRequest,
    path: web::Path<String>,
    query: web::Query<ContentQuery>,
) -> impl Responder {
    let compress = query.compress.unwrap_or(true);
    let ip = extract_ip(&request);

    if mongo_service.is_user_banned(&ip).await.expect("Failed to check if user is banned") {
        return HttpResponse::Forbidden().body("Prohibited");
    }

    mongo_service.increment_requests(&ip).await.expect("Failed to increment requests");

    match aws_service.get_file(&path).await {
        Ok(data) => {
            if compress {
                let compressed = compress_data(&data);
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

fn compress_data(data: &[u8]) -> Vec<u8> {
    use flate2::write::GzEncoder;
    use flate2::Compression;
    use std::io::Write;

    let mut encoder = GzEncoder::new(Vec::new(), Compression::default());
    encoder.write_all(data).expect("Failed to write data");
    encoder.finish().expect("Failed to finish compression")
}

fn extract_ip(req: &HttpRequest) -> String {
    req.peer_addr()
        .map(|addr| addr.ip().to_string())
        .unwrap_or_else(|| "unknown".to_string())
}
