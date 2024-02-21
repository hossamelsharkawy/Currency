package com.example.currency.data


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val convertRepository: ConvertRepository,
) : ViewModel() {


    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    init {
        getSymbols()
    }

    private fun update(function: (UIState) -> UIState) = _uiState.update(function)

    private fun convert() = with(_uiState.value) {

        if (canConvert()) {
            viewModelScope
                .launch {
                    update {
                        it.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    try {
                        val rate = convertRepository.getRate(base = form, target = to)
                        update {
                            it.apply {
                                setRate(rate)
                                isLoading = false
                                error = null
                            }

                        }

                    } catch (e: Exception) {
                        update {
                            it.copy(
                                error = e.message,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun getSymbols() {
        viewModelScope.launch {
            val list = convertRepository.getCurrencyList()
            update {
                it.copy(
                    symbols = list,
                    form = list.first(),
                    isLoading = false, error = null
                )
            }
        }
    }


    fun onEvent(uiEvent: UIEvent) {
        when (uiEvent) {
            is UIEvent.From -> {
                update { it.copy(form = uiEvent.form) }
                convert()
            }

            is UIEvent.To -> {
                update { it.copy(to = uiEvent.to) }
                convert()
            }

            is UIEvent.Amount -> {
                update { it.copy(amount = uiEvent.amount) }
                convert()
            }

            UIEvent.Swipe -> {
                update {
                    it.copy(
                        form = it.to,
                        to = it.form,
                    )
                }
                convert()
            }
        }
    }


}