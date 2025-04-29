use crate::utils::ip::IPUtils;
use crate::utils::string::StringUtils;
use actix_web::{post, web, HttpRequest, HttpResponse};
use std::sync::Arc;
use std::time::{SystemTime, UNIX_EPOCH};
use sea_orm::IntoActiveModel;
use entity::{paste_content, paste_metadata};
use crate::database::postgres_service::PostgresService;

#[post("")]
async fn upload(
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
        .and_then(|v| v.to_str().ok()?.parse::<i64>().ok())
        .unwrap_or(86_400_000);

    let since_the_epoch = SystemTime::now()
        .duration_since(UNIX_EPOCH)
        .unwrap()
        .as_millis() as i64;

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

    let paste = paste_metadata::Model {
        id: file_id.clone(),
        title: title.to_string(),
        created: since_the_epoch,
        report_book,
        wrap,
        creator_ip: ip.clone(),
        expires_at: expires,
    };
    
    let paste_content = paste_content::Model {
        id: file_id.clone(),
        content: body.clone(),
    };
    
    let active_metadata: paste_metadata::ActiveModel = paste.into_active_model();
    let active_content: paste_content::ActiveModel = paste_content.into_active_model();
    
    if let Err(e) = postgres_service.put_paste(active_metadata, active_content).await {
        println!("Failed to save to database: {:?}", e);
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