package uabc.axel.ornelas.juegodedados

class JuegoDeDados(private val dados: ArrayList<Dado>) {

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