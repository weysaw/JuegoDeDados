package uabc.axel.ornelas.juegodedados

import android.widget.Button
import android.widget.TextView
import java.lang.NumberFormatException

class Fila(val puntajes: Array<TextView>, val botonAccion: Button) {
    var nJugadas: Int = 0
    //Puntaje que se puede realizar para la ronda
    var puntajeRonda: Int = 0
    var puntajeFila: Int = 0

    init {
        botonAccion.setOnClickListener {
            calcularPuntajeFila()
        }
    }

    /**
     *
     */
    fun calcularPuntajeFila() {
        try {
            puntajeFila = puntajes.sumOf { it.text.toString().toInt() }
            puntajes[puntajes.lastIndex].text = puntajeFila.toString()
        } catch (e: NumberFormatException) {
            println("No hay numero")
        }
    }

    /**
     *
     */
    fun valoresDefecto() {
        nJugadas = 0
        puntajeRonda = 0
        puntajeFila = 0
    }

}