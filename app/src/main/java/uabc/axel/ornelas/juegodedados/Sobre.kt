package uabc.axel.ornelas.juegodedados

import android.content.pm.PackageInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uabc.axel.ornelas.juegodedados.databinding.ActivitySobreBinding
import java.io.FileNotFoundException

/**
 * Actividad para mostrar la información de la aplicación
 *
 * @author Ornelas M Axel L
 * @version 02.10.2021
 */
class Sobre : AppCompatActivity() {

    private lateinit var binding: ActivitySobreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySobreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        obtenerInformacion()
    }

    /**
     * Se obtiene la información de los archivos
     */
    private fun obtenerInformacion() {
        // Se obtiene la información del paquete
        val pInfo: PackageInfo = applicationContext.packageManager.getPackageInfo(
            applicationContext.packageName,
            0)
        // Se obtiene la versión de la aplicación
        val versionDelJuego = pInfo.versionName

        val nombreArchivo = "DatosBalut"
        var infoGuardada = ""
        try {
            // Abre el archivo y lee los datos de las puntuaciones
            applicationContext.openFileInput(nombreArchivo).bufferedReader().useLines { lines ->
                infoGuardada = lines.fold("") { textAnt, textDesp ->
                    "$textAnt\n$textDesp"
                }
            }
        } catch (e: FileNotFoundException) { }
        // Divide la información para analizarlo
        val datosPartidos = infoGuardada.split(",")
        with(binding) {
            val datoNoEncontrado = "Record no registrado"
            // Verifica el tamaño correcto
            if (datosPartidos.size == 3) {
                // Coloca los datos en los textos
                puntajeRecord.text = datosPartidos[0]
                puntosRecord.text = datosPartidos[1]
                fechaRecord.text = datosPartidos[2]
            } else {
                puntajeRecord.text = datoNoEncontrado
                puntosRecord.text = datoNoEncontrado
                fechaRecord.text = datoNoEncontrado
            }
            version.text = versionDelJuego
        }
    }


}