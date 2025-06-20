package com.example.medidasfati.daos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medidasfati.R
import com.example.medidasfati.models.Medida

class MedidaAdapter(
    private var medidas: List<Medida>,
    private val onItemClick: (Medida) -> Unit
) : RecyclerView.Adapter<MedidaAdapter.MedidaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedidaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medida, parent, false)
        return MedidaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedidaViewHolder, position: Int) {
        val medida = medidas[position]
        holder.bind(medida)

        holder.itemView.setOnClickListener {
            onItemClick(medida)
        }
    }

    override fun getItemCount(): Int {
        return medidas.size
    }

    fun updateData(newMedidas: List<Medida>) {
        this.medidas = newMedidas
        notifyDataSetChanged()
    }

    class MedidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewCliente: TextView = itemView.findViewById(R.id.textViewCliente)
        private val textViewAmbiente: TextView = itemView.findViewById(R.id.textViewAmbiente)
        private val textViewAncho: TextView = itemView.findViewById(R.id.textViewAncho)
        private val textViewAlto: TextView = itemView.findViewById(R.id.textViewAlto)
        private val textViewSistema: TextView = itemView.findViewById(R.id.textViewSistema)
        private val textViewCaida: TextView = itemView.findViewById(R.id.textViewCaida)

        fun bind(medida: Medida) {
            textViewCliente.text = "Cliente: ${medida.cliente}"
            textViewAmbiente.text = "Amb: ${medida.ambiente}"
            textViewAncho.text = "Ancho: ${medida.ancho}"
            textViewAlto.text = "Alto: ${medida.alto}"
            textViewSistema.text = "Sistema: ${medida.sistema}"
            textViewCaida.text = if (medida.caida == true) {
                "Caida: ADELANTE"
            } else {
                "Caida: COMUN"
            }
        }
    }
}