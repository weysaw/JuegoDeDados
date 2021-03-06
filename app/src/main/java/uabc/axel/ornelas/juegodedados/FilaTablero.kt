package uabc.axel.ornelas.juegodedados

import android.graphics.Color
import android.widget.Button
import android.widget.TextView

/**
 * Contiene como se comporta la fila del balut
 *
 * @author Ornelas M Axel L
 * @version 03.11.2021
 */
class FilaTablero(
    val puntajes: Array<TextView>,
    val botonAccion: Button,
    private val puntajeTextoFila: TextView,
) {
    var nJugada: Int = 0

    //Puntaje que se puede realizar para la ronda
    var puntajeRonda: Int = 0

    //Puntaje Fila
    var puntajeFila: Int = 0
        private set
    var puntosFila: Int = 0

    init {
        puntajeTextoFila.setTextColor(Color.BLUE)
    }

    /**
     * Calcula el puntaje total de la fila solo sumando el ultimo valor
     */
    fun calcularPuntajeFila() {
        //Obtengo el número de la jugada actual
        val puntajeTexto = puntajes[nJugada]
        //Cambio el color del texto puesto
        puntajeTexto.setTextColor(Color.DKGRAY)
        // Obtengo el texto del puntaje
        val texto = puntajeTexto.text.toString()
        if (texto == "")
            puntajeTexto.text = "/" //Si la jugada no tiene valor se le pone un palo
        else
            puntajeFila += texto.toInt() //Sumo el puntaje de la fila con el nuevo
        //Pone el resultado total de la suma al final
        puntajeTextoFila.text = puntajeFila.toString()
        // Se desactiva el boton
        botonAccion.isEnabled = false
        //Se empieza de nuevo la ronda
        puntajeRonda = 0
        // Se incrementa la jugada
        nJugada++
    }

    /**
     * Se asignan los valores por defecto, se usa al reiniciar el juego
     */
    fun valoresDefecto() {
        nJugada = 0
        puntajeRonda = 0
        puntajeFila = 0
        puntajes.forEach { puntaje ->
            puntaje.text = ""
        }
        puntajeTextoFila.text = ""
        botonAccion.isEnabled = false
    }

}