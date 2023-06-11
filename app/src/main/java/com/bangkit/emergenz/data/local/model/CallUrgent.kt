package com.bangkit.emergenz.data.local.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CallUrgent(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("place_id")
    val place_id: String,

    @field:SerializedName("formatted_phone_number")
    val formattedPhoneNumber: String? = null
) : Parcelable