use actix_web::web;
// Psst, error was here :)
pub mod get_content;
pub mod upload;

// NOTE: These web::scope("URL")s are prepended to the URLs in your routes. Meaning that if you do /get/{id}/ and your get_content has "/content" your url will be "/get/{id}//content" which is not what you want.
pub fn configure_routes(cfg: &mut web::ServiceConfig) {
    cfg
    .service(
        web::scope("/get") // For content
            .service(get_content::get_content)
    )
    .service(
        web::scope("/get") // For metadata
            .service(get_content::get_metadata)
    )
    .service(
        web::scope("/upload") // For uploading
            .service(upload::upload)
    );
}
// We do the multiple service web::scopes because each have their own functions. 