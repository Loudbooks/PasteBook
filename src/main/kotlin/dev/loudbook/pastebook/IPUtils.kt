package dev.loudbook.pastebook

import jakarta.servlet.http.HttpServletRequest

object IPUtils {
    fun getIPFromRequest(request: HttpServletRequest): String {
        val xRealIP = request.getHeader("X-REAL-IP")
        return if (xRealIP != null) {
            xRealIP.split(",")[0]
        } else {
            request.remoteAddr
        }
    }
}