package uabc.axel.ornelas.juegodedados

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Es el adaptador para que se miren los elementos del recycler
 *
 * @author Ornelas M Axel L
 * @version 02.10.2021
 */
class AdaptadorRecycler(private val localDataSet: ArrayList<ArrayList<String>>) :
    RecyclerView.Adapter<AdaptadorRecycler.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        //Indica el estilo que debe tener el recycler
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fila_tablero, parent, false)
        //Devuelve la vista creada
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Coloca la información de los puntajes a los campos de texto
        with (holder) {
            categoria.text = localDataSet[position][0]
            for (i in holder.datos.indices)
                datos[i].text = localDataSet[position][i + 1]
            puntajeFila.text = localDataSet[position][5]
            puntosFila.text = localDataSet[position][6]
        }

    }

    /**
     * Tamaño de los datos
     */
    override fun getItemCount(): Int {
        return localDataSet.size
    }

    /**
     * Clase interna para localizar los datos que se necesitan para colocar la info
     */
    inner class ViewHolder(
        itemView: View,
        val categoria: TextView = itemView.findViewById(R.id.categoria),
        val datos: Array<TextView> = arrayOf(
            itemView.findViewById(R.id.primerDato),
            itemView.findViewById(R.id.segundoDato),
            itemView.findViewById(R.id.tercerDato),
            itemView.findViewById(R.id.cuartoDato),
        ),
        val puntajeFila: TextView = itemView.findViewById(R.id.puntajeFila),
        val puntosFila: TextView = itemView.findViewById(R.id.puntosFila),
    ) : RecyclerView.ViewHolder(itemView) {


    }
}