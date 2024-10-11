package www.desafio3.marvin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class Adaptador(private val recursos: List<Recurso>) : RecyclerView.Adapter<Adaptador.ViewHolder>() {
    private var onItemClick: OnItemClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulorv: TextView = view.findViewById(R.id.TituloText)
        val tiporv: TextView = view.findViewById(R.id.TipoText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recurso, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recurso = recursos[position]
        holder.titulorv.text = recurso.titulo
        holder.tiporv.text = "Tipo de recurso: " + recurso.tipo


        holder.itemView.setOnClickListener {
            onItemClick?.onItemClick(recurso)
        }
    }

    override fun getItemCount(): Int {
        return recursos.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClick = listener
    }

    interface OnItemClickListener {
        fun onItemClick(recurso: Recurso)
    }
}