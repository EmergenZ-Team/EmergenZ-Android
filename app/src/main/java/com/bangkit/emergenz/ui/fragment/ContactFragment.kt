package com.bangkit.emergenz.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.emergenz.R
import com.bangkit.emergenz.adapter.ContactAdapter
import com.bangkit.emergenz.data.local.model.Contact
import com.bangkit.emergenz.databinding.FragmentContactBinding

class ContactFragment : Fragment(){

    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContact()
    }

    private fun setContact() {
        val contactList = mutableListOf<Contact>()
        val layoutManager = LinearLayoutManager(requireActivity())
        val adapter = ContactAdapter(contactList)
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val sortOrder = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"

        val cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)

                val contact = Contact(name, number)
                contactList.add(contact)
            }
        }

        binding.apply {
            rvContact.layoutManager = layoutManager
            rvContact.adapter = adapter
        }

        adapter.setOnItemClickCallback(object: ContactAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Contact) {
                setCustomDialogBox(data)
            }
        })
    }

    private fun setCustomDialogBox(contact: Contact) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.card_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnYes: Button = dialog.findViewById(R.id.btn_yes)
        val btnNo: Button = dialog.findViewById(R.id.btn_no)

        btnYes.setOnClickListener {
            contact.number?.let { number -> makeCall(number) }
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}