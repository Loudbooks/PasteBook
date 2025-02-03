use actix_web::{get, HttpResponse};

// Unlike typescript, we have to use empty strings instead of a / because stupid
#[get("/content")]
async fn get_content() -> HttpResponse {
    HttpResponse::Ok().body("ok")
}

#[get("/metadata")]
async fn get_metadata() -> HttpResponse {
    HttpResponse::Ok().body("ok")
}