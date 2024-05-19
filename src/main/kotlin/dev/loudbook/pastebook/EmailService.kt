package dev.loudbook.pastebook

import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmailService {
    private val sender = "noreply@pastebook.dev"

    @Value("\${email.username}")
    private val username: String? = null

    @Value("\${email.password}")
    private val password: String? = null

    @Value("\${email.host}")
    private val host: String? = null

    fun sendEmail(email: String, subject: String, message: String) {
        println("Sending email to $email with subject: $subject and message: $message")

        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = host
        props["mail.smtp.port"] = "587"
        props["mail.smtp.ssl.trust"] = host

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            val msg = MimeMessage(session)
            msg.setFrom(InternetAddress(sender, "PasteBook"))
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
            msg.subject = subject
            msg.setContent(message, "text/html")

            Transport.send(msg)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendConfirmationEmail(email: String, id: Int) {
        val subject = "PasteBook Email Confirmation"
        val message = """
            <html>
                <body>
                    <h1>Welcome to PasteBook</h1>
                    <p>Your 6 digit email confirmation code is <strong>${id}</strong>.</p>
                    <br>
                    <p>Never share this code with others. Ever.</p>
                </body>
            </html>
        """.trimIndent()
        sendEmail(email, subject, message)
    }
}