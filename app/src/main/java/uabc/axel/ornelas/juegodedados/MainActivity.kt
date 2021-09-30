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
        val texto = arrayListOf<TextView>()
        //Agregar fours
        for (i in 1..4) {
            val textView = TextView(applicationContext)
            textView.text = i.toString()
            textView.textSize = 24.0f
            textView.gravity = Gravity.CENTER
            val parametros = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
            parametros.weight = 1.0f
            textView.layoutParams = parametros
            textView.background = getDrawable(R.drawable.borde)
            binding.foursTabla.addView(textView)
            texto.add(textView)
        }
        // Se asigna el arreglo de dados
        val dados = arrayOf(
            Dado(binding.dado),
            Dado(binding.dado2),
            Dado(binding.dado3),
            Dado(binding.dado4),
            Dado(binding.dado5)
        )
        juego = Balut(dados)
        //Se agrega el fondo para que se cambia el fondo
        registerForContextMenu(binding.menu)
    }

    /**
     * Coloca el menu de cambio de colores en el fondo
     */
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
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
    }
}