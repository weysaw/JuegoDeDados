package uabc.axel.ornelas.juegodedados

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import uabc.axel.ornelas.juegodedados.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //Se usa para acceder a los elementos
    private lateinit var binding: ActivityMainBinding
    private lateinit var juego: Balut

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tablero = ArrayList<Fila>()
        //Se obtienen las filas para mandarselos al juego de balut
        with(binding) {
            val categorias = arrayOf(
                arrayOf(foursTabla, foursBtn),
                arrayOf(fivesTabla, fivesBtn),
                arrayOf(sixesTabla, sixesBtn),
                arrayOf(straightTabla, straightBtn),
                arrayOf(fullHouseTabla, fullHouseBtn),
                arrayOf(choiceTabla, choiceBtn),
                arrayOf(balutTabla, balutBtn),
            )
            categorias.forEach { categoria ->
                //Se obtiene el primer dato
                val filaTexto: ArrayList<TextView> = inicializarFila(categoria[0] as TableRow)
                //Se obtiene el segundo dato
                val puntajeTotalTexto: TextView = filaTexto.removeLast()
                //Se crea el objeto de la fila
                val fila = Fila(filaTexto.toTypedArray(), categoria[1] as Button, puntajeTotalTexto)
                //Agrega la fila al tablero
                tablero.add(fila)
            }
            //Se obtiene las imagenes de los dados
            val dados = inicializarImagenes()
            //Se crea el juego se mandan los dados y el tablero
            juego = Balut(dados, tablero.toTypedArray())
        }
        inicializarDatoPresion(tablero)
        //Se agrega el fondo para que se cambia el fondo
        registerForContextMenu(binding.menu)
    }

    private fun inicializarDatoPresion(tablero: ArrayList<Fila>) {
        //Se recorre por el tablero accinando la acción de cada boton
        tablero.forEach { fila ->
            fila.botonAccion.setOnClickListener {
                fila.calcularPuntajeFila()
                juego.reiniciarLanzamiento()
                juego.cambiarRonda()
                juego.calcularPuntajeTotal()
                juego.calcularPuntos()
                binding.tirar.isEnabled = true
                actualizarTextos()
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
                true
            }
            R.id.historial -> {
                Toast.makeText(this, "Se ha seleccionado historial", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.humano -> {
                Toast.makeText(this, "Se ha seleccionado humano", Toast.LENGTH_SHORT).show()
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
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    /**
     * Lanza los dados que esten seleccionados
     */
    fun lanzarDados(v: View) {
        if (juego.esFinDelJuego()) {
            Toast.makeText(applicationContext,
                "Presione reiniciar si quiere jugar de nuevo",
                Toast.LENGTH_SHORT).show()
            binding.tirar.isEnabled = false
        }

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