use actix_web::web;

pub mod get_content;

// NOTE: These web::scope("URL")s are prepended to the URLs in your routes. Meaning that if you do /get/{id}/ and your get_content has "/content" your url will be "/get/{id}//content" which is not what you want.
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