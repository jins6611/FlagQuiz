package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class Questions(
    @SerializedName("answer_id") var answerId: Int? = null,
    @SerializedName("countries") var countries: ArrayList<Countries> = arrayListOf(),
    @SerializedName("country_code") var countryCode: String? = null

)