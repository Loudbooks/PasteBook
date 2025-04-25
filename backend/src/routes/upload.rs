use crate::database::aws_service::AWSService;
use crate::utils::ip::IPUtils;
use crate::utils::string::StringUtils;
use actix_web::{post, web, HttpRequest, HttpResponse};
use std::sync::Arc;
use std::time::{SystemTime, UNIX_EPOCH};
use crate::database::postgres_service::PostgresService;
use crate::models::paste;

#[post("")]
async fn upload(
    aws_service: web::Data<Arc<AWSService>>,
    postgres_service: web::Data<Arc<PostgresService>>,
    req: HttpRequest,
    body: String
) -> HttpResponse {
    let title = req
        .headers()
        .get("title")
        .and_then(|v| v.to_str().ok())
        .unwrap_or("");
    if title.is_empty() {
        return HttpResponse::BadRequest().body("Title is required");
    }

    let report_book = req
        .headers()
        .get("reportBook")
        .map(|v| v.to_str().unwrap_or("false") == "true")
        .unwrap_or(false);

    let wrap = req
        .headers()
        .get("wrap")
        .map(|v| v.to_str().unwrap_or("false") == "true")
        .unwrap_or(false);

    let mut expires = req
        .headers()
        .get("expires")
        .and_then(|v| v.to_str().ok()?.parse::<u64>().ok())
        .unwrap_or(86_400_000);

    let since_the_epoch = SystemTime::now()
        .duration_since(UNIX_EPOCH)
        .unwrap()
        .as_millis() as u64;

    if expires < 60_000 {
        return HttpResponse::BadRequest().body("Expire time too short");
    }
    if expires < since_the_epoch {
        expires += since_the_epoch;
    }
    if expires > since_the_epoch + 2_765_000_000 {
        return HttpResponse::BadRequest().body("Expire time too long");
    }

    let ip = match IPUtils::get_ip_from_request(&req) {
        Some(ip) => ip,
        None => return HttpResponse::BadRequest().body("Failed to get IP"),
    };

    let file_id = StringUtils::generate_random_string(5);

    let paste = paste::Model {
        id: file_id.clone(),
        title: title.to_string(),
        created: since_the_epoch,
        report_book,
        wrap,
        creator_ip: ip.clone(),
        expires_at: expires,
    };
    
    let active_paste: paste::ActiveModel = paste.clone().into();

    if let Err(e) = aws_service.put_file(&file_id, body.as_ref()).await {
        return HttpResponse::InternalServerError().body(format!("Failed to upload file: {:?}", e));
    }
    if let Err(e) = postgres_service.put_paste(active_paste).await {
        return HttpResponse::InternalServerError().body(format!("Failed to save to database: {:?}", e));
    }

    postgres_service.increment_requests(&ip).await.expect("Failed to increment requests");

    let host_domain = req
        .headers()
        .get("X-Domain-Name")
        .and_then(|v| v.to_str().ok());
    let response_body = if let Some(domain) = host_domain {
        format!("https://{}/p/{}", domain, file_id)
    } else {
        file_id.clone()
    };

    HttpResponse::Ok().body(response_body)
}