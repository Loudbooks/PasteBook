use actix_web::{get, HttpResponse};

// Note this "route" url gets appended after the baseurl specified in `mod.rs` make sure that the URL is correct.
#[get("/content")]
async fn get_content() -> HttpResponse {
    HttpResponse::Ok().body("ok")
}

#[get("/metadata")]
async fn get_metadata() -> HttpResponse {
    HttpResponse::Ok().body("ok")
}