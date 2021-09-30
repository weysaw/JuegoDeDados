package uabc.axel.ornelas.juegodedados

import android.graphics.Color
import android.widget.Button
import android.widget.TextView

class Fila(val puntajes: Array<TextView>, val botonAccion: Button) {
    var nJugada: Int = 0

    //Puntaje que se puede realizar para la ronda
    var puntajeRonda: Int = 0

    var puntajeFila: Int = 0
        private set
    init {
        puntajes[puntajes.lastIndex].setTextColor(Color.MAGENTA)
    }

    /**
     * Calcula el puntaje total de la fila solo sumando el ultimo valor
     */
    fun calcularPuntajeFila() {
        //Obtengo el número de la jugada actual
        val puntajeTexto = puntajes[nJugada]
        // Obtengo el texto del puntaje
        val texto = puntajeTexto.text.toString()
        //Sumo el puntaje de la fila con el nuevo
        puntajeFila += if (texto == "" || texto == "/") 0 else texto.toInt()
        puntajeTexto.setTextColor(Color.BLUE)
        //Si la jugada no tiene valor se le pone un palo
        if (puntajeTexto.text == "")
            puntajeTexto.text = "/"
        //Pone el resultado total de la suma al final
        puntajes[puntajes.lastIndex].text = puntajeFila.toString()
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
        for (puntaje in puntajes)
            puntaje.text = ""
    }

}