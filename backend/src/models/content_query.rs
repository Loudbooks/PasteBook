use serde::Deserialize;

#[derive(Deserialize)]
pub struct ContentQuery {
    pub compress: Option<bool>,
}