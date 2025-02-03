pub struct DataUtils;

impl DataUtils {
    pub fn compress_data(data: &[u8]) -> Vec<u8> {
        use flate2::write::GzEncoder;
        use flate2::Compression;
        use std::io::Write;
    
        let mut encoder = GzEncoder::new(Vec::new(), Compression::default());
        encoder.write_all(data).expect("Failed to write data");
        encoder.finish().expect("Failed to finish compression")
    }
}