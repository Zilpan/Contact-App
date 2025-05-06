package com.example.contactapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class ContactAdapter(
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private val contacts = ArrayList<Contact>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.contactName)
        val phoneText: TextView = view.findViewById(R.id.contactPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.nameText.text = contact.name
        holder.phoneText.text = contact.phone
        holder.itemView.setOnClickListener {
            onClick(contact.phone)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun submitList(newContacts: List<Contact>) {
        contacts.clear()
        contacts.addAll(newContacts)
        notifyDataSetChanged()
    }
}