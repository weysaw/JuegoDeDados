package uabc.axel.ornelas.juegodedados

/**
 * Representa al juego de dados balut
 * Se maneja de una manera desacoplada al lenguaje
 * https://balutgame.com/play/
 * http://www.mathcs.emory.edu/~cheung/Courses/170/Syllabus/10/pokerCheck.html
 */
class Balut(private val dados: Array<Dado>, private val tablero: Array<Fila>) {
    companion object {
        const val NUMERO_RONDAS = 28
        const val LIM_LANZAMIENTOS = 3
    }

    enum class Categoria(val valor: Int) {
        FOURS(0), FIVES(1), SIXES(2), STRAIGHT(3), FULLHOUSE(4), CHOICE(5), BALUT(6)
    }

    //Puntos que se otorgan por ciertos criterios
    var puntos: Int = 0
        private set

    // Puntaje que se aumenta cuando se realiza cualquier movimiento
    var puntaje: Int = 0
        private set

    var lanzamientoActual: Int = 0
        private set

    // Ronda que determina cuando se termina el juego
    var rondaActual = 1
        private set

    /**
     * Pone el valor del dado a su valor por defecto
     */
    fun reiniciarJuego() {
        puntos = 0
        puntaje = 0
        rondaActual = 1
        lanzamientoActual = 0
        tablero.forEach { fila ->
            fila.nJugadas = 0
            fila.puntajes.forEach { puntaje ->
                puntaje.text = ""
            }
        }
        dados.forEach {
            it.valorDefecto()
        }
    }

    /**
     * Lanza todos los dados del juego
     */
    fun lanzarDados() {
        if (rondaActual >= NUMERO_RONDAS)
            return
        if (lanzamientoActual >= 3)
            return
        dados.forEach {
            if (lanzamientoActual == 0)
                it.primerLanzamiento()
            //Si el cuadro esta presionado no se lanza de nuevo
            else if (!it.presionado)
                it.lanzarDado()
        }
        lanzamientoActual++
        verificarJugada()
    }

    /**
     * Verifica la jugada de cada categoria
     */
    private fun verificarJugada() {
        // Verificar cuatros
        verificarFourFivesSixes(Categoria.FOURS.valor, 4)
        // Verificar cincos
        verificarFourFivesSixes(Categoria.FIVES.valor, 5)
        // Verificar seises
        verificarFourFivesSixes(Categoria.SIXES.valor, 6)
        verificarChoice()
        if (verificarBalut())
            return
        if (verificarFullHouse())
            return
        verificarStraight()
    }

    /**
     * Verifica cuantos dados hay del tipo indicado y los suma
     */
    private fun verificarFourFivesSixes(categoria: Int, valor: Int) {
        val fila = tablero[categoria]
        fila.botonAccion.isEnabled = true
        if (fila.nJugadas >= 4) {
            fila.botonAccion.isEnabled = false
            return
        }
        //Busca el número de coincidencias de un elemento
        val coincidencias = dados.count { dado ->
            dado.valor == valor
        }
        if (coincidencias == 0)
            fila.botonAccion.isEnabled = false
        //Multiplica el valor por el número de coincidencias
        fila.puntajeRonda = valor * coincidencias
        determinarPuntaje(fila)
    }

    /**
     * Verifica los 5 dados que esten en escalera
     */
    private fun verificarStraight() {
        val fila = tablero[Categoria.STRAIGHT.valor]
        fila.botonAccion.isEnabled = true
        if (fila.nJugadas >= 4) {
            fila.botonAccion.isEnabled = false
            return
        }
        fila.puntajeRonda = 0
        val mayor: Int = dados.maxOf { dado ->
            dado.valor
        }
        //Ya que son 5 dados, se le resta cuatro para llegar al minimo
        val min: Int = mayor - 4
        for (valor in mayor downTo min) {
            //Busca si el valor del dado coincide como escalera
            val encontrado = dados.any { dado -> dado.valor == valor }
            if (!encontrado) {
                fila.puntajeRonda = 0
                fila.botonAccion.isEnabled = false
                break
            }
            //Se suman los valores del dado actual
            fila.puntajeRonda += valor
        }
        determinarPuntaje(fila)
    }

    /**
     *
     */
    private fun verificarFullHouse(): Boolean {
        var esFullHouse = false
        val fila: Fila = tablero[Categoria.FULLHOUSE.valor]
        fila.puntajeRonda = 0
        var valor = dados[0].valor
        fila.botonAccion.isEnabled = true
        dados.forEach {
            println("Dado con valor ${it.valor}")
        }
        var coincidencias = dados.count { dado -> dado.valor == valor }
        var par = 0
        var tresDado = 0
        when (coincidencias) {
            3 -> tresDado = valor
            2 -> par = valor
        }
        valor = dados.find { dado -> dado.valor != valor }!!.valor
        coincidencias = dados.count { dado -> dado.valor == valor }
        if (tresDado != 0)
            esFullHouse = coincidencias == 2
        else if (par != 0)
            esFullHouse = coincidencias == 3
        if (esFullHouse)
            fila.puntajeRonda = dados.sumOf { dado -> dado.valor }
        else
            fila.botonAccion.isEnabled = false
        determinarPuntaje(fila)
        return esFullHouse
    }


    /**
     * Suma los valores de los dados actuales
     */
    private fun verificarChoice() {
        val fila = tablero[Categoria.CHOICE.valor]
        fila.botonAccion.isEnabled = true
        if (fila.nJugadas >= 4) {
            fila.botonAccion.isEnabled = false
            return
        }
        fila.puntajeRonda = 0
        //Suma los valores de cada dado
        fila.puntajeRonda = dados.sumOf { dado ->
            dado.valor
        }
        determinarPuntaje(fila)
    }

    /**
     * Verifica que los 5 dados sean del mismo tipo
     */
    private fun verificarBalut(): Boolean {
        //Obtengo la fila a verificar
        var fila = tablero[Categoria.BALUT.valor]
        fila.botonAccion.isEnabled = true
        // Si esta lleno la fila, no tiene caso seguir revisando
        if (fila.nJugadas >= 4) {
            fila.botonAccion.isEnabled = false
            return false
        }
        fila.puntajeRonda = 0
        //Obtengo el primer valor del dado
        val valor = dados[0].valor
        //Si el valor del dado es igual al del primer dado, significa que hay balut
        val esBalut = dados.all { dado -> dado.valor == valor }
        //Si todos los dados son el mismo valor, es un balut
        if (esBalut) fila.puntajeRonda = valor * 5 + 20 else fila.botonAccion.isEnabled = false
        determinarPuntaje(fila)
        return esBalut
    }

    /**
     *
     */
    private fun determinarPuntaje(fila: Fila) {
        var texto: String = fila.puntajeRonda.toString()
        //Si no hay ninguno, no muestra nada
        if (fila.puntajeRonda == 0)
            texto = ""
        //Posición de la jugada por la fila
        val pos = fila.nJugadas
        fila.puntajes[pos].text = texto
    }

    /**
     *
     */
    fun calcularPuntajeFila(pos: Int) {

    }

    /**
     *
     */
    private fun criterioPuntos() {

    }

    /**
     *
     */
    private fun realizarMovimiento() {

    }

}
