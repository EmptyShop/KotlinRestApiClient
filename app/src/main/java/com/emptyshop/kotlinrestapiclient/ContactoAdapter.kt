package com.emptyshop.kotlinrestapiclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ContactoAdapter (
    var listaContactos: ArrayList<Contacto>
): RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder>(){

    inner class ContactoViewHolder(itemView: View): ViewHolder(itemView){
        val tvId = itemView.findViewById(R.id.tvId) as TextView
        val tvFullname = itemView.findViewById(R.id.tvFullname) as TextView
        val tvEmail = itemView.findViewById(R.id.tvEmail) as TextView
        val tvPhone = itemView.findViewById(R.id.tvPhone) as TextView
        val btnEditar = itemView.findViewById(R.id.btnEditar) as Button
        val btnBorrar = itemView.findViewById(R.id.btnBorrar) as Button
    }

    // esta interfaz la implementa MainActivity
    interface OnItemClicked{
        fun editarContacto(contacto: Contacto)
        fun borrarContacto(id: Int)
    }

    // esta variable es instancia de la clase que implementa la interfaz OnItemClicked
    private var onClick: OnItemClicked? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_contacto,
            parent, false)
        return ContactoViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        val contacto = listaContactos.get(position)

        holder.tvId.text = contacto.id.toString()
        holder.tvFullname.text = contacto.fullname
        holder.tvEmail.text = contacto.email
        holder.tvPhone.text = contacto.phone

        // asigna la funcionalidad al listener del evento onClick
        holder.btnEditar.setOnClickListener{
            onClick?.editarContacto(contacto)
        }

        // asigna la funcionalidad al listener del evento onClick
        holder.btnBorrar.setOnClickListener {
            onClick?.borrarContacto(contacto.id)
        }
    }

    override fun getItemCount(): Int {
        return listaContactos.size
    }

    fun setOnClick(onClick: OnItemClicked?){
        this.onClick = onClick
    }
}