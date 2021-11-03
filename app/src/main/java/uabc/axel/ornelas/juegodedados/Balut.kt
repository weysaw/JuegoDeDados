package uabc.axel.ornelas.juegodedados

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color

/**
 * Representa al juego de dados balut y contiene la lógica de este aplicada a android
 * Referencias
 * https://balutgame.com/play/
 * http://www.mathcs.emory.edu/~cheung/Courses/170/Syllabus/10/pokerCheck.html
 * @author Ornelas M Axel L
 * @version 02.10.2021
 */
class Balut(
    private val dados: Array<Dado>,
    private val tablero: Array<FilaTablero>,
    private val context: Context
) {
    companion object {
        const val NUMERO_RONDAS = 28
        const val LIM_LANZAMIENTOS = 3
        const val FOURS = 0
        const val FIVES = 1
        const val SIXES = 2
        const val STRAIGHT = 3
        const val FULLHOUSE = 4
        const val CHOICE = 5
        const val BALUT = 6
    }

    //Puntos que se otorgan por ciertos criterios
    var puntos: Int = -2
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
        puntos = -2
        puntaje = 0
        rondaActual = 1
        reiniciarLanzamiento()
        tablero.forEach { fila ->
            fila.valoresDefecto()
        }
        valorDefectoDados()
    }

    /**
     * Reinicia el lanzamiento a su estado inicial
     */
    fun reiniciarLanzamiento() {
        lanzamientoActual = 0
    }

    /**
     * Cambia a la siguiente ronda, desactiva todos los botones y devuelve los dados a su estado
     * inicial
     */
    fun cambiarRonda() {
        rondaActual++
        valorDefectoDados()
        tablero.forEach { fila ->
            fila.botonAccion.isEnabled = false
            if (fila.nJugada < 4)
                fila.puntajes[fila.nJugada].text = ""
        }
    }

    /**
     * Lanza todos los dados del juego
     */
    fun lanzarDados() {
        if (esFinDelJuego())
            return
        if (limiteLanzamiento())
            return
        dados.forEach {
            if (lanzamientoActual == 0) {
                tablero.forEach { fila ->
                    if (fila.nJugada < 4)
                        fila.botonAccion.isEnabled = true
                }
                it.primerLanzamiento()
            }
            //Si el cuadro esta presionado no se lanza de nuevo
            else if (!it.presionado)
                it.lanzarDado()
        }
        lanzamientoActual++
        verificarJugada()
    }

    /**
     * Determina si ya es el final del juego
     */
    fun esFinDelJuego(): Boolean {
        return rondaActual > NUMERO_RONDAS
    }

    /**
     * Determina si el lanzamiento actual es el limite
     */
    fun limiteLanzamiento(): Boolean {
        return lanzamientoActual >= LIM_LANZAMIENTOS
    }

    /**
     * Suma todas la puntuaciones totales y calcula los puntos
     */
    fun calcularPuntajeTotal() {
        puntaje = tablero.sumOf { fila -> fila.puntajeFila }
    }

    /**
     * Calcula los puntos por la categoria
     */
    fun calcularPuntos() {
        val puntosCategoria = IntArray(7)
        //Se verifica el criterio de puntos por cada categoria
        puntosCategoria[FOURS] = if (tablero[FOURS].puntajeFila >= 52) 2 else 0
        puntosCategoria[FIVES] = if (tablero[FIVES].puntajeFila >= 65) 2 else 0
        puntosCategoria[SIXES] = if (tablero[SIXES].puntajeFila >= 78) 2 else 0
        puntosCategoria[STRAIGHT] = if (tablero[STRAIGHT].puntajes.all { cuadro ->
                val texto = cuadro.text
                texto != "" && texto != "/"
            }) 4 else 0
        puntosCategoria[FULLHOUSE] = if (tablero[FULLHOUSE].puntajes.all { cuadro ->
                val texto = cuadro.text
                texto != "" && texto != "/"
            }) 3 else 0
        puntosCategoria[CHOICE] = if (tablero[CHOICE].puntajeFila >= 100) 2 else 0
        puntosCategoria[BALUT] = tablero[BALUT].puntajes.sumOf {
            var suma = 0
            if (it.text != "" && it.text != "/")
                suma = 2
            suma
        }
        //Coloca los puntos en cada fila
        for (i in puntosCategoria.indices)
            tablero[i].puntosFila = puntosCategoria[i]
        //Suma todos los puntos
        puntos = puntosCategoria.sum()
        //Si es menor a 300 el puntaje es -2
        var puntosScore: Int = -2
        //Se verifica el rango del puntaje
        if (puntaje in 300..649) {
            var izq = 300
            val lim = 649
            //Se suma en 50 en 50 hasta llegar al limite
            while (izq != lim && puntaje in izq..lim) {
                puntosScore++
                izq += 50
            }
        } else if (puntaje in 650..812)
            puntosScore = 6

        puntos += puntosScore
    }

    /**
     * Coloca a los dados a su valor inicial de 1
     */
    private fun valorDefectoDados() {
        dados.forEach { it.valorDefecto() }
    }

    /**
     * Verifica la jugada de cada categoria
     */
    private fun verificarJugada() {
        // Verificar cuatros
        verificarFourFivesSixes(FOURS, 4)
        // Verificar cincos
        verificarFourFivesSixes(FIVES, 5)
        // Verificar seises
        verificarFourFivesSixes(SIXES, 6)
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
        if (fila.nJugada >= 4)
            return
        //Busca el número de coincidencias de un elemento
        val coincidencias = dados.count { dado ->
            dado.valor == valor
        }
        //Multiplica el valor por el número de coincidencias
        fila.puntajeRonda = valor * coincidencias
        determinarPuntaje(fila)
    }

    /**
     * Verifica los 5 dados que esten en escalera
     */
    private fun verificarStraight() {
        val fila = tablero[STRAIGHT]
        //Si ya no se pueden hacer más jugada no verifica
        if (fila.nJugada >= 4)
            return
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
                break
            }
            //Se suman los valores del dado actual
            fila.puntajeRonda += valor
        }
        determinarPuntaje(fila)
    }

    /**
     * Verifica si hay un par y un 3 de un tipo
     */
    private fun verificarFullHouse(): Boolean {
        var esFullHouse = false
        val filaTablero: FilaTablero = tablero[FULLHOUSE]
        // Si esta lleno la fila, no tiene caso seguir revisando
        if (filaTablero.nJugada >= 4)
            return false
        //Se reinicia el puntaje de la ronda
        filaTablero.puntajeRonda = 0
        //Se obtiene el 1er valor, para determinar sus coincidencias
        var valor = dados[0].valor
        //Se determinan las coincidencias
        var coincidencias = dados.count { dado -> dado.valor == valor }
        //Se utilizan para verificar si se encuentran los valores
        var par = 0
        var tresDado = 0
        //Se determina para que valor corresponde las coincidencias
        when (coincidencias) {
            3 -> tresDado = valor
            2 -> par = valor
        }
        //Se busca si hay otro valor diferente al primero encontrado, esto no provoca error porque
        // se valido con las otras categorias
        valor = dados.find { dado -> dado.valor != valor }!!.valor
        // Se determina nuevamente las coincidencias para el nuevo valor
        coincidencias = dados.count { dado -> dado.valor == valor }
        //Si tiene valor el de tres debe de tener 2 coincidencias para que sea fullhouse
        if (tresDado != 0)
            esFullHouse = coincidencias == 2
        //Si tiene valor el par  debe de tener 3 coincidencias para que sea fullhouse
        else if (par != 0)
            esFullHouse = coincidencias == 3
        //Si es full house se determina el puntaje
        if (esFullHouse)
            filaTablero.puntajeRonda = dados.sumOf { dado -> dado.valor }
        //Se muestra el puntaje
        determinarPuntaje(filaTablero)
        return esFullHouse
    }


    /**
     * Suma los valores de los dados actuales
     */
    private fun verificarChoice() {
        val fila = tablero[CHOICE]
        // Si esta lleno la fila, no tiene caso seguir revisando
        if (fila.nJugada >= 4)
            return
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
        var fila = tablero[BALUT]
        // Si esta lleno la fila, no tiene caso seguir revisando
        if (fila.nJugada >= 4)
            return false

        fila.puntajeRonda = 0
        //Obtengo el primer valor del dado
        val valor = dados[0].valor
        //Si el valor del dado es igual al del primer dado, significa que hay balut
        val esBalut = dados.all { dado -> dado.valor == valor }
        //Si todos los dados son el mismo valor, es un balut
        if (esBalut) {
            fila.puntajeRonda = valor * 5 + 20
            AlertDialog.Builder(context)
                .setTitle("¡¡BALUT!!")
                .setMessage("HA SALIDO UN BALUT APROVECHALO")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create().show()
        }
        determinarPuntaje(fila)
        return esBalut
    }

    /**
     * Determina el puntaje por cada fila
     */
    private fun determinarPuntaje(filaTablero: FilaTablero) {
        var texto: String = filaTablero.puntajeRonda.toString()
        //Si no hay ninguno, no muestra nada
        if (filaTablero.puntajeRonda == 0)
            texto = ""
        //Posición de la jugada por la fila
        val puntajeTexto = filaTablero.puntajes[filaTablero.nJugada]
        puntajeTexto.setTextColor(Color.WHITE)
        puntajeTexto.text = texto
    }
}
