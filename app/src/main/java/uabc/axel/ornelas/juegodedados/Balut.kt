package uabc.axel.ornelas.juegodedados

/**
 * Representa al juego de dados balut
 * Se maneja de una manera desacoplada al lenguaje
 * https://balutgame.com/play/
 */
class Balut(private val dados: Array<Dado>) {
    companion object {
        const val NUMERO_RONDAS = 28
        const val LIM_LANZAMIENTOS = 3
    }
    enum class categoria {

    }
    private val tablero = Array(7) { IntArray(4) }
    //Puntos que se otorgan por ciertos criterios
    private var puntos: Int = 0
    // Puntaje que se aumenta cuando se realiza cualquier movimiento
    private var puntaje: Int = 0
    /**
     * Pone el valor del dado a su valor por defecto
     */
    fun reiniciarJuego() {
        dados.forEach {
            it.valorDefecto()
        }
    }

    /**
     * Lanza todos los dados del juego
     */
    fun lanzarDados() {
        dados.forEach {
            //Si el cuadro esta presionado no se lanza de nuevo
            if (!it.presionado)
                it.lanzarDado()
        }
    }





}