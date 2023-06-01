package com.bangkit.emergenz.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.emergenz.R
import com.bangkit.emergenz.data.response.Result
import com.bangkit.emergenz.databinding.CardTelpBinding

class CallAdapter(private var data: List<Result>, private val context: Context) : RecyclerView.Adapter<CallAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardTelpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class MyViewHolder(private val binding: CardTelpBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Result) {
            binding.tvNamePlace.text = data.name
            binding.tvNumberPhone.text = data.formattedPhoneNumber

            binding.ivCall.setOnClickListener {
                setCustomDialogBox()
            }
        }
    }

    private fun setCustomDialogBox() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.card_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnYes: Button = dialog.findViewById(R.id.btn_yes)
        val btnNo: Button = dialog.findViewById(R.id.btn_no)

        btnYes.setOnClickListener {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<Result>) {
        data = newData
        notifyDataSetChanged()
    }
}
