package Modelo
import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import diana.padilla.isss_salud.R
import net.sf.jasperreports.engine.*
import net.sf.jasperreports.export.*
import java.io.File
import java.util.HashMap
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;



class ReportGenerator(private val context: Context) {

    @SuppressLint("ResourceType")
    fun generateReport(selectedDUI: String): File? {
        return try {
            // Cargar el archivo .jasper desde la carpeta raw
            val reportInputStream = context.resources.openRawResource(R.raw.expediente_medico)

            // Cargar el archivo Jasper
            val jasperReport = JasperCompileManager.compileReport(reportInputStream)

            // Parámetros para el reporte
            val parameters = HashMap<String, Any>()
            parameters["?"] = selectedDUI

            // Fuente de datos (puedes personalizar o usar una fuente de datos real)
            val jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, JREmptyDataSource())

            // Crear el archivo PDF
            val outputDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "reports")
            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }
            val outputFile = File(outputDir, "Expediente.pdf")

            // Exportar a PDF
            val exporter = JRPdfExporter()
            exporter.setExporterInput(SimpleExporterInput(jasperPrint))
            exporter.setExporterOutput(SimpleOutputStreamExporterOutput(outputFile))

            // Configuración del exportador
            val pdfConfig = SimplePdfExporterConfiguration()
            exporter.setConfiguration(pdfConfig)

            // Exportar reporte
            exporter.exportReport()

            // Retornar el archivo generado
            outputFile

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}