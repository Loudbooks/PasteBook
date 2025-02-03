use actix_web::web;
pub mod get;
pub mod upload;

pub fn configure_routes(cfg: &mut web::ServiceConfig) {
    cfg
    .service(
        web::scope("/get")
            .service(get::get_content)
            .service(get::get_metadata)
    )
    .service(
        web::scope("/upload")
            .service(upload::upload)
    );
}