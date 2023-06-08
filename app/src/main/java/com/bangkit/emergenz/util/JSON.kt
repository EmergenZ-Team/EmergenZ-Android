package com.bangkit.emergenz.util

import org.json.JSONException
import org.json.JSONObject

fun extractErrorMessageFromJson(errorBody: String?): String {
    if (errorBody.isNullOrEmpty()) {
        return "Unknown error occurred"
    }

    try {
        val jsonObject = JSONObject(errorBody)
        val errorMessage = jsonObject.getString("message")
        return errorMessage
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    return "Error occurred"
}