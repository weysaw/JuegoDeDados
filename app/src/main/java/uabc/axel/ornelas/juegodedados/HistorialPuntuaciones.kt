package uabc.axel.ornelas.juegodedados

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import uabc.axel.ornelas.juegodedados.databinding.ActivityHistorialPuntuacionesBinding

/**
 * Actividad que muestra la información de los puntajes
 *
 * @author Ornelas M Axel L
 * @version 02.10.2021
 */
class HistorialPuntuaciones : AppCompatActivity() {

    private lateinit var binding: ActivityHistorialPuntuacionesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialPuntuacionesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Se obtiene los datos de la pelicula
        val informacion = intent.getSerializableExtra("tablero") as ArrayList<ArrayList<String>>
        // Se le indica el layout al reclycler
        binding.filasRecycler.layoutManager = LinearLayoutManager(this)
        // Se crea un adaptador con el arreglo
        val adapter = AdaptadorRecycler(informacion)
        // Se colocan las peliculas en el adaptador
        binding.filasRecycler.adapter = adapter
    }
}