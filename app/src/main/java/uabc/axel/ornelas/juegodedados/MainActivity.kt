package uabc.axel.ornelas.juegodedados

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import uabc.axel.ornelas.juegodedados.databinding.ActivityMainBinding
import java.io.FileNotFoundException
import java.util.*
import kotlin.collections.ArrayList

/**
 * Actividad principal donde se muestra la interfaz
 *
 * @author Ornelas M Axel L
 * @version 02.10.2021
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var juego: Balut
    private val tablero = ArrayList<FilaTablero>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var categorias: Array<Array<View>>
        //Se obtienen las filas para mandarselos al juego de balut
        with(binding.tablero) {
            categorias = arrayOf(
                arrayOf(foursTabla, foursBtn),
                arrayOf(fivesTabla, fivesBtn),
                arrayOf(sixesTabla, sixesBtn),
                arrayOf(straightTabla, straightBtn),
                arrayOf(fullHouseTabla, fullHouseBtn),
                arrayOf(choiceTabla, choiceBtn),
                arrayOf(balutTabla, balutBtn)
            )
        }
        categorias.forEach { categoria ->
            //Se obtiene el primer dato
            val filaTexto: ArrayList<TextView> = inicializarFila(categoria[0] as TableRow)
            //Se obtiene el segundo dato
            val puntajeTotalTexto: TextView = filaTexto.removeLast()
            //Se crea el objeto de la fila
            val fila =
                FilaTablero(filaTexto.toTypedArray(), categoria[1] as Button, puntajeTotalTexto)
            //Agrega la fila al tablero
            tablero.add(fila)
        }
        //Se obtiene las imagenes de los dados
        val dados = inicializarImagenes()
        //Se crea el juego se mandan los dados y el tablero
        juego = Balut(dados, tablero.toTypedArray())
        inicializarPresionDado()
        //Se agrega el fondo para que se cambia el fondo
        registerForContextMenu(binding.menu)
    }


    private fun inicializarPresionDado() {
        //Se recorre por el tablero accinando la acción de cada boton
        tablero.forEach { fila ->
            fila.botonAccion.setOnClickListener {
                fila.calcularPuntajeFila()
                juego.reiniciarLanzamiento()
                juego.calcularPuntajeTotal()
                juego.calcularPuntos()
                binding.tirar.isEnabled = true
                juego.cambiarRonda()
                //Verifica si es la ultima ronda para mostrarlo al jugador
                if (juego.esFinDelJuego()) {
                    Toast.makeText(applicationContext,
                        "Presione reiniciar si quiere jugar de nuevo",
                        Toast.LENGTH_SHORT).show()
                    //Se desactiva el boton
                    binding.tirar.isEnabled = false
                    binding.puntaje.text = "Puntaje Total: ${juego.puntaje}"
                    binding.puntos.text = "Puntos: ${juego.puntos}"
                    //Se guardan las puntuaciones si supera al record
                    guardarPuntuaciones()
                    return@setOnClickListener
                }
                //Se actualizan las vistas de los textos del menu
                actualizarTextos()
            }
        }
    }

    /**
     * Se guardan las puntuaciones del juego actual
     */
    private fun guardarPuntuaciones() {
        val filename = "DatosBalut"
        var infoGuardada = ""
        var mejorPuntaje = false
        try {
            //Busca el archivo por si hay records previos
            applicationContext.openFileInput(filename).bufferedReader().useLines { lines ->
                infoGuardada = lines.fold("") { textAnt, textDesp ->
                    "$textAnt$textDesp"
                }
            }
            //Divide los datos por comas
            val datosPartidos = infoGuardada.split(",")
            //Si el tamaño de los datos es 3, significa que estan correctos los datos
            if (datosPartidos.size == 3) {
                // 0 es puntaje, 1 es puntos
                val puntajeRecord = datosPartidos[0].toInt()
                val puntosRecord = datosPartidos[1].toInt()
                //Debe de ser mayor el puntaje para que se nuevo record
                if (juego.puntaje > puntajeRecord) {
                    mejorPuntaje = true
                    // En caso que sea el mismo puntaje se determinara por los puntos
                } else if (juego.puntaje == puntajeRecord && juego.puntos > puntosRecord) {
                    mejorPuntaje = true
                }
            } else
                mejorPuntaje = true
        } catch (e: Exception) {
            when (e) {
                is NumberFormatException, is FileNotFoundException -> mejorPuntaje = true
                else -> throw e
            }
        }
        //Si hay mejor puntaje escribe en el record
        if (mejorPuntaje) {
            //Contenido para el archivo
            val contenido = "${juego.puntaje},${juego.puntos},${Date()}"
            Toast.makeText(applicationContext,
                "Nuevo record Registrado",
                Toast.LENGTH_SHORT).show()
            //Escribe el contenido en el archivo
            applicationContext.openFileOutput(filename, Context.MODE_PRIVATE)
                .use {
                    it.write(contenido.toByteArray())
                }
        }

    }

    /**
     * Actualiza los puntajes de los textos
     */
    private fun actualizarTextos() {
        with(binding) {
            ronda.text = "Ronda: ${juego.rondaActual} / ${Balut.NUMERO_RONDAS}"
            puntaje.text = "Puntaje Total: ${juego.puntaje}"
            puntos.text = "Puntos: ${juego.puntos}"
            roll.text = "Roll: ${juego.lanzamientoActual} / ${Balut.LIM_LANZAMIENTOS}"
        }
    }

    /**
     * Inicializa las imagenes de los dados y los devuelve como un arreglo
     */
    private fun inicializarImagenes(): Array<Dado> {
        // Se asigna el arreglo de dados
        return arrayOf(
            Dado(binding.dado1),
            Dado(binding.dado2),
            Dado(binding.dado3),
            Dado(binding.dado4),
            Dado(binding.dado5)
        )
    }

    /**
     * Inicializa la fila del tablero
     */
    private fun inicializarFila(fila: TableRow): ArrayList<TextView> {
        val filaTexto = arrayListOf<TextView>()
        //Agrega los recuadros
        for (i in 0..4) {
            //Crea el texto donde ira el numero
            val textView = TextView(applicationContext)
            textView.text = ""
            textView.textSize = 24.0f
            //Posición del texto
            textView.gravity = Gravity.CENTER
            //Establece el tamaño del textView
            val parametros = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT)
            //Se establece para que se recorra uniformemente
            parametros.weight = 1.0f
            textView.layoutParams = parametros
            //Establece un borde al fondo
            textView.background = getDrawable(R.drawable.borde)
            //Lo agrega a la vista del tablero
            fila.addView(textView)
            filaTexto.add(textView)
        }
        return filaTexto
    }

    /**
     * Coloca el menu de cambio de colores en el fondo
     */
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?,
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.setHeaderTitle("Color de fondo")
        menuInflater.inflate(R.menu.color_fondo, menu)
    }

    /**
     * Cambia el color del fondo dependiedo de la acción seleccionada
     */
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.rojo -> {
                binding.menu.background = ColorDrawable(Color.parseColor("#DB7093"))
                true
            }
            R.id.verde -> {
                binding.menu.background = ColorDrawable(Color.parseColor("#98FB98"))
                true
            }
            R.id.azul -> {
                binding.menu.background = ColorDrawable(Color.parseColor("#87CEEB"))
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    /**
     * Pone el menu de las opciones en la activity
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.opciones, menu)
        return true
    }

    /**
     * Realiza las acciones del menu de la derecha
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.reglas -> {
                Toast.makeText(this, "Se ha seleccionado reglas", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, Reglas::class.java)
                startActivity(intent)
                true
            }
            R.id.humano -> {
                Toast.makeText(this, "Se ha seleccionado humano", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, HistorialPuntuaciones::class.java)
                val informacion = ArrayList<ArrayList<String>>()
                informacion.add(arrayListOf("Categoria", "1er", "2do", "3er", "4to", "Ptj", "Pts"))
                tablero.forEach { fila ->
                    val filaTexto = ArrayList<String>()
                    filaTexto.add(fila.botonAccion.text.toString())
                    fila.puntajes.forEach { textView ->
                        filaTexto.add(textView.text.toString())
                    }
                    filaTexto.add(fila.puntajeFila.toString())
                    filaTexto.add(fila.puntosFila.toString())
                    informacion.add(filaTexto)
                }
                informacion.add(arrayListOf("", "", "", "", "",
                    juego.puntaje.toString(),
                    juego.puntos.toString()))
                intent.putExtra("tablero", informacion)
                startActivity(intent)
                true
            }
            R.id.reiniciar -> {
                Toast.makeText(this, "Se ha seleccionado reiniciar", Toast.LENGTH_SHORT).show()
                juego.reiniciarJuego()
                binding.tirar.isEnabled = true
                actualizarTextos()
                true
            }
            R.id.sobre -> {
                Toast.makeText(this, "Se ha seleccionado sobre el juego", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, Sobre::class.java))
                true
            }
            R.id.limpiar -> {
                applicationContext.openFileOutput("DatosBalut", Context.MODE_PRIVATE)
                    .use {
                        it.write("".toByteArray())
                    }
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    /**
     * Lanza los dados que esten seleccionados
     */
    fun lanzarDados(v: View) {
        juego.lanzarDados()
        actualizarTextos()
        if (juego.limiteLanzamiento()) {
            binding.tirar.isEnabled = false
            Toast.makeText(applicationContext,
                "Seleccione un botón de categoria",
                Toast.LENGTH_SHORT).show()
        }
    }
}