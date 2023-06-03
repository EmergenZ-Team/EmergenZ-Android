package com.bangkit.emergenz.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.emergenz.R
import com.bangkit.emergenz.data.local.model.Contact
import com.bangkit.emergenz.databinding.CardTelpBinding

class ContactAdapter(private val listContact: List<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: CardTelpBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardTelpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount() = listContact.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val contact = listContact[position]
        holder.binding.apply {
            tvNamePlace.text = contact.name
            tvNumberPhone.text = contact.number
            ivCall.setOnClickListener{onItemClickCallback.onItemClicked(listContact[holder.adapterPosition])}
            ivUserPhoto.setImageResource(R.drawable.ic_person)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Contact)
    }
}