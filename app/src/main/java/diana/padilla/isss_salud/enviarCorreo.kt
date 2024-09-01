package diana.padilla.isss_salud


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

    suspend fun enviarCorreo(receptor: String, asunto: String, mensaje: String) = withContext(Dispatchers.IO) {

        val props = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.socketFactory.port", "465")
            put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            put("mail.smtp.auth", "true")
            put("mail.smtp.port", "465")
        }


        //Incio de sesión
        val session = Session.getInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("issssaludteam@gmail.com", "x x q n t k b m a h m a y y w c")
            }
        })

        //Hacemos el envio
        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress("issssalud@gmail.com"))
                addRecipient(Message.RecipientType.TO, InternetAddress(receptor))
                subject = asunto
                setContent(mensaje, "text/html; charset=utf-8")
            }
            Transport.send(message)
            println("El correo ha sido enviado satisfactoriamente")
        }catch (e: MessagingException){
            e.printStackTrace()
            println("Correo no enviado, error: ${e.message}")
        }
    }
