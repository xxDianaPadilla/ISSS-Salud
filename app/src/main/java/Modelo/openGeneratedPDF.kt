package Modelo
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun openGeneratedPDF(context: Context, file: File) {
    val intent = Intent(Intent.ACTION_VIEW)
    val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    intent.setDataAndType(uri, "application/pdf")
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(intent)
}