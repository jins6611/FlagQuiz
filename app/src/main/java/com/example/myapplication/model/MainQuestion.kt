package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class MainQuestion(

    @SerializedName("questions") var questions: ArrayList<Questions> = arrayListOf(),

)