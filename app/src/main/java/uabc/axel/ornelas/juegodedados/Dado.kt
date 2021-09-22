package uabc.axel.ornelas.juegodedados

import android.graphics.Color
import android.widget.ImageView

class Dado(private var cara: ImageView) {
    var presionado = false
        private set(valor) {
            field = valor
            cara.setBackgroundColor(if (presionado) Color.RED else 0)
        }
    //Puede que no se utilice el valor
    var valor: Int = 1
        private set(num) {
            field = num
            cambiarImagen()
        }

    init {
        cambiarImagen()
        //Cada vez que se presiona el boton realiza la acción
        cara.setOnClickListener {
            presionarDado()
        }
    }

    /**
     * Lanza el dado y cambia su valor
     */
    fun lanzarDado() {
        valor = (1..6).random()
    }

    /**
     * Cambia la imagen de la cara del dado
     */
    private fun cambiarImagen() {
        var imagen = 1
        when (valor) {
            1 -> imagen = R.drawable.cara1
            2 -> imagen = R.drawable.cara2
            3 -> imagen = R.drawable.cara3
            4 -> imagen = R.drawable.cara4
            5 -> imagen = R.drawable.cara5
            6 -> imagen = R.drawable.cara6
        }
        cara.setImageResource(imagen)
    }

    /**
     * Pone el valor por defecto del dado
     */
    fun valorDefecto() {
        valor = 1
        presionado = false
    }

    /**
     * Indica que dado se ha presionado
     */
    private fun presionarDado() {
        presionado = !presionado
    }
}