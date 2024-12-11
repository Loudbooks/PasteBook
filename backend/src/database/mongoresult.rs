use mongodb::error::Error;

pub type MongoResult<T> = Result<T, Error>;