use actix_web::{post, HttpResponse};

// Unlike typescript, we have to use empty strings instead of a / because stupid (explained in `mod.rs`)
#[post("")]
async fn get_content() -> HttpResponse {
    HttpResponse::Ok().body("ok")
}