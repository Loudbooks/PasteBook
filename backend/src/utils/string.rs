use rand::distr::Alphanumeric;
use rand::{rng, Rng};

pub struct StringUtils;

impl StringUtils {
    pub fn generate_random_string(length: usize) -> String {
        rng()
            .sample_iter(&Alphanumeric)
            .take(length)
            .map(char::from)
            .collect()
    }
}