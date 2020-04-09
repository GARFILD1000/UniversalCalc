package com.example.perfectcalc.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.perfectcalc.util.CalculatorButton

class CalculatorViewModel : ViewModel() {

    val resultLiveData = MutableLiveData<String>().apply { postValue("") }
    val lastResultLiveData = MutableLiveData<String>().apply { postValue("") }

    val pressedButtonLiveData = MutableLiveData<CalculatorButton>()
    val moreControlsEnabled = MutableLiveData<Boolean>().apply { postValue(false) }


    fun pressButton(button: CalculatorButton) {
        pressedButtonLiveData.postValue(button)

        resultLiveData.value
    }

    fun switchControls() {
        moreControlsEnabled.postValue(moreControlsEnabled.value?.not())
    }

}