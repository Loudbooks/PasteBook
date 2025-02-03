use actix_web::web;

pub mod get_content;

pub fn configure_routes(cfg: &mut web::ServiceConfig) {
    cfg
    .service(
        web::scope("/get/{id}")
            .service(get_content::get_content)
    )
    .service(
        web::scope("/get/{id}")
            .service(get_content::get_metadata)
    );
}