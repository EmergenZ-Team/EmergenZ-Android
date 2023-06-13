package com.bangkit.emergenz.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.emergenz.data.response.article.DataRecom
import com.bangkit.emergenz.databinding.CardArticleBinding
import com.bangkit.emergenz.ui.activity.ViewArticleActivity
import com.bumptech.glide.Glide

class ArticleAdapter(private val email: String, private var data: List<DataRecom?>, private val context: Context) : RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() {

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
        fun bind(data: DataRecom?) {
            Glide.with(binding.root.context).load(data?.image).centerCrop().into(binding.ivArticlePhoto)
            binding.tvArticleName.text = data?.title
            binding.tvArticleDesc.text = data?.author

            binding.root.setOnClickListener {
                val intent = ViewArticleActivity.newIntent(context,
                    email, data?.newsId.toString())
                binding.root.context.startActivity(intent)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<DataRecom>) {
        data = newData
        notifyDataSetChanged()
    }
}