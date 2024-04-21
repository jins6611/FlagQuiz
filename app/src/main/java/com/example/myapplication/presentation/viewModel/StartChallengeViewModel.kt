package com.example.myapplication.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.MainQuestion
import com.example.myapplication.presentation.StartChallengeActivity
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartChallengeViewModel : ViewModel() {

    private val _questionList = MutableStateFlow(MainQuestion())
    val questionList: StateFlow<MainQuestion> = _questionList

    fun getQuestions(mContext: StartChallengeActivity) {
        viewModelScope.launch {
            try {
                val jsonString = readJsonFromAssets(mContext, "question.json")
                _questionList.value = parseJsonToModel(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun readJsonFromAssets(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    private fun parseJsonToModel(jsonString: String): MainQuestion {
        val gson = Gson()
        return gson.fromJson(jsonString, MainQuestion::class.java)
    }
}