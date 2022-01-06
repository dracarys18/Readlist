package com.karthihegde.readlist.retrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karthihegde.readlist.retrofit.data.BookList
import com.karthihegde.readlist.retrofit.data.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RetroViewModel @Inject constructor(
    private val retroRepository: RetroRepository
) : ViewModel() {
    private val _retroState: MutableStateFlow<RetroState<BookList>> =
        MutableStateFlow(RetroState.PlaceHolder())
    val retroState: Flow<RetroState<BookList>> = _retroState
    private val _isError: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isError: Flow<Boolean> = _isError
    private val _bookList: MutableStateFlow<BookList?> = MutableStateFlow(null)
    val bookList: Flow<BookList?> = _bookList

    fun resetAll() {
        _bookList.value = null
        _isError.value = false
        _retroState.value = RetroState.PlaceHolder()
    }

    fun getBooks(query: String) {
        viewModelScope.launch {
            _retroState.value = RetroState.Loading()
            when (val result = retroRepository.getBooks(query = query)) {
                is RetroState.Failure -> {
                    _isError.value = true
                    _retroState.value = RetroState.Failure(message = result.message)
                }
                is RetroState.Success -> {
                    _bookList.value = result.data
                    _isError.value = false
                    _retroState.value = RetroState.Success(data = result.data)
                }
                else -> {}
            }
        }
    }
}

/**
 * A ViewModel to store the book which is clicked
 */
@HiltViewModel
class ClickBookViewModel @Inject constructor(
    private val retroRepository: RetroRepository
) : ViewModel() {
    private val _retroState: MutableStateFlow<RetroState<Item>> =
        MutableStateFlow(RetroState.PlaceHolder())
    val retroState: Flow<RetroState<Item>> = _retroState
    private val _isError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isError: Flow<Boolean> = _isError
    private val _clickBook: MutableStateFlow<Item?> = MutableStateFlow(null)
    val clickedBook: Flow<Item?> = _clickBook

    /**
     * Handle value change of [_clickBook]
     *
     * @param id
     */
    fun onValueChange(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _retroState.value = RetroState.Loading()
            when (val result = retroRepository.getBooksFromId(id = id)) {
                is RetroState.Failure -> {
                    _isError.value = true
                    _retroState.value = RetroState.Failure(message = result.message)
                }
                is RetroState.Success -> {
                    _isError.value = false
                    _clickBook.value = result.data
                    _retroState.value = RetroState.Success(data = result.data)
                }
                else -> {}
            }
        }
    }
}
