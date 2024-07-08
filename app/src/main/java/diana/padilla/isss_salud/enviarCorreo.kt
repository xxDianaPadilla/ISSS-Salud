package diana.padilla.isss_salud

import android.widget.Toast
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

class enviarCorreo {

    suspend fun enviarCorreo(receptor: String, asunto: String, mensaje: String) = withContext(Dispatchers.IO) {

        val props = Properties().apply {
            put("mail.smtp.host", "smtp.gmali.com")
            put("mail.smtp.socketFactory.port", "465")
            put("mail.smtp.socketFactory.class", "java.net.ssl.SSLSocketFactory")
            put("mail.smtp.auth", "true")
            put("mail.smtp.port", "465")
        }

        //Incio de sesión
        val session = Session.getInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("issssalud@gmail.com", "ejwx ahup kxjy xjsw")
            }
        })

        //Hacemos el envio
        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress("issssalud@gmail.com"))
                addRecipient(Message.RecipientType.TO, InternetAddress(receptor))
                subject = asunto
                setText(mensaje)
            }
            Transport.send(message)
            println("El correo ha sido enviado satisfactoriamente")
        }catch (e: MessagingException){
            e.printStackTrace()
            println("Correo no enviado, error: ${e.message}")
        }
    }
}