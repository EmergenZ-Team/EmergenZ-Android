package com.bangkit.emergenz.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.emergenz.data.local.model.CachedArticle
import com.bangkit.emergenz.databinding.CardArticleBinding
import com.bumptech.glide.Glide

class ArticleAdapter(private var data: List<CachedArticle?>, private val context: Context) : RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(private val binding: CardArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CachedArticle?) {
            Glide.with(binding.root.context).load(data?.imageUrl).centerCrop().into(binding.ivArticlePhoto)
            binding.tvArticleName.text = data?.title
            binding.tvArticleDesc.text = data?.summary

            binding.root.setOnClickListener {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<CachedArticle?>) {
        data = newData
        notifyDataSetChanged()
    }
}