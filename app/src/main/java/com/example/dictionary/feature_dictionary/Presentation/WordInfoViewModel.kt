package com.example.dictionary.feature_dictionary.Presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.core.util.Resource
import com.example.dictionary.feature_dictionary.domain.use_case.GetWordInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordInfoViewModel @Inject constructor(
    private val getWordInfo: GetWordInfo
): ViewModel(){

    /** States **/
    /* Separate states away from WordInfoState class */
    private val _searchQuery = mutableStateOf<String>("")
    val searchQuery: State<String> = _searchQuery

    /* State to represent WordInfoState class */
    private val _state = mutableStateOf(WordInfoState())
    val state: State<WordInfoState> = _state

    /** Event flow **/
    /* Event flow will be used to sent one time event to the UI like showing snack bar for an error for example
     * for that we can simpy create our own ui event class which will be a sealed class */

    sealed class UIEvent {
        data class ShowSnackbar(val message: String): UIEvent()
    }

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    fun onSearch(query: String){
        _searchQuery.value = query
        /* we cancel the job at first because whenever we write in the text filed onSearch will be called again.
         * so we need every time this function is being called to clear cancel the last job to prevent memory leak */
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            /* call the use case function and now this return a flow to handle */
            getWordInfo(query)
                .onEach { result ->
                    /* Job of the viewmodel is to just deal with this cases and map the result to our state. */
                    when(result){
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )
                            _eventFlow.emit(UIEvent.ShowSnackbar(
                                result.message ?: "Unknown Error"
                            ))
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                    }
                }.launchIn(this)
        }
    }
}