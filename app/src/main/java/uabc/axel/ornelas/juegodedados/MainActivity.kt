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
        with(binding) {
            val filaFours = Fila(inicializarFila(foursTabla), foursBtn)
            val filaFives = Fila(inicializarFila(fivesTabla), fivesBtn)
            val filaSixes = Fila(inicializarFila(sixesTabla), sixesBtn)
            val filaStraight = Fila(inicializarFila(straightTabla), straightBtn)
            val filaFullHouse = Fila(inicializarFila(fullHouseTabla), fullHouseBtn)
            val filaChoice = Fila(inicializarFila(choiceTabla), choiceBtn)
            val filaBalut = Fila(inicializarFila(balutTabla), balutBtn)
            val dados = inicializarImagenes()
            val tablero = arrayOf(filaFours,
                filaFives,
                filaSixes,
                filaStraight,
                filaFullHouse,
                filaChoice,
                filaBalut)
            juego = Balut(dados, tablero)
        }
        //Se agrega el fondo para que se cambia el fondo
        registerForContextMenu(binding.menu)
    }

    /**
     *
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
    private fun inicializarFila(fila: TableRow): Array<TextView> {
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
            val parametros = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
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
        return filaTexto.toTypedArray()
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
            R.id.compu -> {
                Toast.makeText(this, "Se ha seleccionado computadora", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.reiniciar -> {
                Toast.makeText(this, "Se ha seleccionado reiniciar", Toast.LENGTH_SHORT).show()
                juego.reiniciarJuego()
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
        juego.lanzarDados()
        //Actualiza los puntajes
        binding.puntaje.text = "Puntaje Total: ${juego.puntaje}"
        binding.puntos.text = "Puntos: ${juego.puntos}"
    }


}