package dev.loudbook.pastebook

import jakarta.servlet.http.HttpServletRequest

object IPUtils {
    fun getIPFromRequest(request: HttpServletRequest): String? {
        var xRealIP = request.getHeader("Cf-Connecting-IP")

        if (xRealIP == null) {
            xRealIP = request.getHeader("CF-Connecting-IPv6")
        }

        if (xRealIP == null) {
            return request.remoteAddr
        }

        return xRealIP.split(",")[0]
    }
}