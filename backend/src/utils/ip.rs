use actix_web::HttpRequest;

pub struct IPUtils;

impl IPUtils {
    pub fn get_ip_from_request(req: &HttpRequest) -> Option<String> {
        if let Some(ip) = req.headers().get("Cf-Connecting-IP").and_then(|v| v.to_str().ok()) {
            return Some(Self::extract_first_ip(ip));
        }
        
        if let Some(ip) = req.headers().get("X-Real-Ip").and_then(|v| v.to_str().ok()) {
            return Some(Self::extract_first_ip(ip));
        }

        if let Some(ip) = req.headers().get("CF-Connecting-IPv6").and_then(|v| v.to_str().ok()) {
            return Some(Self::extract_first_ip(ip));
        }

        req.peer_addr()
            .map(|addr| addr.ip().to_string())
    }
    
    fn extract_first_ip(ip_list: &str) -> String {
        ip_list.split(',').next().unwrap_or("").trim().to_string()
    }
}
