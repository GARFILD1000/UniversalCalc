package com.example.perfectcalc.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class ResultsViewModel : ViewModel() {
    var resultLiveData = MutableLiveData<String>()
    var needUpdateHistory = MutableLiveData<Boolean>()

    var lastResults = LinkedList<String>()

    fun showResult(result: String) {
        resultLiveData.postValue(result)
    }

    fun addLastResult(result: String) {
        if (result.isNotEmpty()) {
            lastResults.addLast(result)
            needUpdateHistory.postValue(true)
        }
    }

    fun clearLastResults() {
        lastResults.clear()
        needUpdateHistory.postValue(true)
    }
}