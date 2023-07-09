package com.bangkit.emergenz.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.emergenz.R
import com.bangkit.emergenz.data.response.history.DataItem
import com.bangkit.emergenz.databinding.CardHistoryBinding
import com.bangkit.emergenz.ui.viewmodel.HistoryViewModel

class HistoryAdapter(private var data: List<DataItem?>, private val viewModel: HistoryViewModel,private val name: String,private val context: Context,) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data[position]
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(private val binding: CardHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataItem) {
            val date = "${data.date} ${data.time}"

            binding.tvNamePlace3.text = data.instancename
            binding.tvNumberPhone3.text = data.phonenumber
            binding.tvTimeDate.text = date

            binding.ivCall3.setOnClickListener {
                setCustomDialogBox(data.instancename!!.toString(), data.phonenumber!!.toString(), context)
            }
        }
    }

    private fun setCustomDialogBox(instancename: String ,phoneNumber: String, context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.card_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnYes: Button = dialog.findViewById(R.id.btn_yes)
        val btnNo: Button = dialog.findViewById(R.id.btn_no)

        btnYes.setOnClickListener {
            makeCall(phoneNumber, context)
            dialog.dismiss()
            viewModel.saveHistory(name, instancename, phoneNumber)
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun makeCall(phoneNumber: String, context: Context) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        ContextCompat.startActivity(context, intent, bundleOf())
    }

}