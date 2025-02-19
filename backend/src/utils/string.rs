use rand::{distributions::Alphanumeric, thread_rng, Rng};

pub struct StringUtils;

impl StringUtils {
    pub fn generate_random_string(length: usize) -> String {
        thread_rng()
            .sample_iter(&Alphanumeric)
            .take(length)
            .map(char::from)
            .collect()
    }
}