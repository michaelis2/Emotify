package com.example.emotify.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel for managing the selected country code in the app.
 */

class MainViewModel : ViewModel() {
    private val _countryCode = MutableLiveData<String>()
    val countryCode: LiveData<String> get() = _countryCode

    /**
     * Updates the country code value.
     *
     * @param code The new country code to be set.
     */
    fun setCountryCode(code: String) {
        _countryCode.value = code
    }
}
