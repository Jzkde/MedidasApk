package com.example.medidasfati.daos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medidasfati.R
import com.example.medidasfati.models.Presupuesto

class PresupuestoAdapter(
    private var presupuestos: List<Presupuesto>,
    private val onItemClick: (Presupuesto) -> Unit
) : RecyclerView.Adapter<PresupuestoAdapter.PresupuestoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresupuestoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_presupuesto, parent, false)
        return PresupuestoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PresupuestoViewHolder, position: Int) {
        val presupuesto = presupuestos[position]
        holder.bind(presupuesto)

        holder.itemView.setOnClickListener {
            onItemClick(presupuesto)
        }
    }

    override fun getItemCount(): Int {
        return presupuestos.size
    }

    fun updateData(newPresupuestos: List<Presupuesto>) {
        this.presupuestos = newPresupuestos
        notifyDataSetChanged()
    }

    class PresupuestoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewCliente: TextView = itemView.findViewById(R.id.textViewCliente)
        private val textViewAmbiente: TextView = itemView.findViewById(R.id.textViewAmbiente)
        private val textViewAncho: TextView = itemView.findViewById(R.id.textViewAncho)
        private val textViewAlto: TextView = itemView.findViewById(R.id.textViewAlto)
        private val textViewSistema: TextView = itemView.findViewById(R.id.textViewSistema)
        private val textViewCaida: TextView = itemView.findViewById(R.id.textViewCaida)

        fun bind(presupuesto: Presupuesto) {
            textViewCliente.text = "Cliente: ${presupuesto.clienteNombre}"
            textViewAmbiente.text = "Amb: ${presupuesto.ambiente}"
            textViewAncho.text = "Ancho: ${presupuesto.ancho}"
            textViewAlto.text = "Alto: ${presupuesto.alto}"
            textViewSistema.text = "Sistema: ${presupuesto.sistema}"
            textViewCaida.text = if (presupuesto.caida == true) {
                "Caida: ADELANTE"
            } else {
                "Caida: COMUN"
            }
        }
    }
}