package dev.loudbook.pastebook

object ContentScanner {
    fun scanContent(content: String): String {
/*        val lines = content.split("\n").toMutableList()

        for ((index, line) in lines.withIndex()) {
            val words = line.split(" ").toMutableList()

            for ((wordIndex, word) in words.withIndex()) {
                if (InetAddressValidator.getInstance().isValidInet4Address(word)) {
                    words[wordIndex] = "***.***.***.***"
                } else if (InetAddressValidator.getInstance().isValidInet6Address(word)) {
                    words[wordIndex] = "****:****:****:****:****:****:****:****"
                }
            }

            lines[index] = words.joinToString(" ")
        }

        return lines.joinToString("\n")*/

        return content
    }
}