use std::fs;
use std::io::Write;

use random_word::Lang;

pub async fn post(data: String) {
    let data_dir = "./pastes/".to_string();
    let filename = format!("{}-{}-{}", random_word::gen(Lang::En), random_word::gen(Lang::En), random_word::gen(Lang::En));
    let path = format!("{}{}.txt", data_dir, filename);

    if !fs::metadata(&data_dir).is_ok() {
        fs::create_dir(&data_dir).unwrap();
    }

    let mut file = fs::File::create(&path).unwrap();
    file.write_all(data.as_bytes()).unwrap();
}