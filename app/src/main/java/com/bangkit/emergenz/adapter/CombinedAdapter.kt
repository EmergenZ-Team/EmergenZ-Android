package com.bangkit.emergenz.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.emergenz.R
import com.bangkit.emergenz.data.local.model.CallUrgent
import com.bangkit.emergenz.data.response.call.Result
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.FIRE
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.HOSPITAL
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.POLICE
import com.bangkit.emergenz.ui.viewmodel.HistoryViewModel

class CombinedAdapter(
    private var dataFromRecyclerView1: List<CallUrgent>,
    private var dataList2: List<Result>,
    private val query: String,
    private val context: Context,
    private val viewModel: HistoryViewModel,
    private val name: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CALL_URGENT -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.card_telp_urgent, parent, false)
                RecyclerView1ViewHolder(view)
            }
            CALL_PLACE -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.card_telp, parent, false)
                RecyclerView2ViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            CALL_URGENT -> {
                val viewHolder1 = holder as RecyclerView1ViewHolder
                viewHolder1.bind(dataFromRecyclerView1[position])
            }
            CALL_PLACE -> {
                val viewHolder2 = holder as RecyclerView2ViewHolder
                viewHolder2.bind(dataList2[position - dataFromRecyclerView1.size], query)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataFromRecyclerView1.size + dataList2.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < dataFromRecyclerView1.size) {
            CALL_URGENT
        } else {
            CALL_PLACE
        }
    }

    inner class RecyclerView1ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTitle: TextView = itemView.findViewById(R.id.tv_name_place)
        private val textViewNumber: TextView = itemView.findViewById(R.id.tv_number_phone)
        private val imageViewCall: ImageView = itemView.findViewById(R.id.iv_call)
        fun bind(data: CallUrgent) {
            textViewTitle.text = data.name
            textViewNumber.text = data.formattedPhoneNumber

            imageViewCall.setOnClickListener {
                setCustomDialogBox(data.name!!.toString(), data.formattedPhoneNumber!!.toString(), context)
            }
        }
    }

    inner class RecyclerView2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTitle: TextView = itemView.findViewById(R.id.tv_name_place2)
        private val textViewNumber: TextView = itemView.findViewById(R.id.tv_number_phone2)
        private val imageViewCall: ImageView = itemView.findViewById(R.id.iv_call2)
        private val imageViewPhoto: ImageView = itemView.findViewById(R.id.iv_user_photo2)
        fun bind(data: Result, query: String) {
            textViewTitle.text = data.name
            textViewNumber.text = data.formattedPhoneNumber
            when (query) {
                POLICE -> (
                        imageViewPhoto.setImageResource(R.drawable.ic_tab_police)
                        )
                FIRE -> (
                        imageViewPhoto.setImageResource(R.drawable.ic_tab_fire)
                        )
                HOSPITAL -> (
                        imageViewPhoto.setImageResource(R.drawable.ic_tab_hospital)
                        )
            }
            imageViewCall.setOnClickListener {
                setCustomDialogBox(data.name!!.toString() ,data.formattedPhoneNumber!!.toString(), context)
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
        startActivity(context, intent, bundleOf())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData1(newData1: List<CallUrgent>) {
        dataFromRecyclerView1 = newData1
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setData2(newData2: List<Result>) {
        dataList2 = newData2
        notifyDataSetChanged()
    }

    companion object {
        private const val CALL_URGENT = 0
        private const val CALL_PLACE = 1
    }
}





