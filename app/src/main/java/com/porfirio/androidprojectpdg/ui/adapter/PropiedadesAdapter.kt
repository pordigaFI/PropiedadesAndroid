package com.porfirio.androidprojectpdg.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.porfirio.androidprojectpdg.data.remote.model.PropiedadDto
import com.porfirio.androidprojectpdg.databinding.PropiedadElementBinding


class PropiedadesAdapter (
    private val propiedades:List<PropiedadDto>,
    private val onPropiedadClicked: (PropiedadDto) -> Unit
): RecyclerView.Adapter<PropiedadesAdapter.ViewHolder>(){

    class ViewHolder(private val binding: PropiedadElementBinding): RecyclerView.ViewHolder(binding.root){
        val ivThumbnail = binding.ivThumbnail
        fun bind(propiedad: PropiedadDto){
            //binding.tvTitle.text = "Jugador Tenis"
            binding.tvPropiedad.text = propiedad.propiedad
            binding.tvEstatus.text = propiedad.estatus
            binding.tvTipo.text = propiedad.tipo
            binding.tvPrecio.text = propiedad.precio

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PropiedadElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = propiedades.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val propiedad = propiedades[position]

        holder.bind(propiedad)

        //Con Glide
        Glide.with(holder.itemView.context)
            .load(propiedad.thumbnail)
            .into(holder.ivThumbnail)

        //Procesamiento del clic al elemento
        holder.itemView.setOnClickListener {
            onPropiedadClicked(propiedad)
        }
    }
}