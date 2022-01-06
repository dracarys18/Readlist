package com.karthihegde.readlist.viewmodels

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SearchViewModel : ViewModel() {
    private val _text: MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue(""))
    val text: Flow<TextFieldValue> = _text

    fun onValueChange(newVal: TextFieldValue) {
        _text.value = newVal
    }
}
