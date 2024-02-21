package com.example.currency.data


data class UIState(
    var form: String = "",
    var to: String = "",
    var amount: String = "1",
    var symbols: List<String> = emptyList(),
    var isLoading: Boolean = true,
    var error: String? =null,

    ) {
    var result: String = ""
        private set

    fun canConvert() = form.isNotBlank() and to.isNotBlank() and amount.isNotBlank()

    fun setRate(rate: Double) {
        result = amount.toDouble().times(rate).toString()
    }

}


sealed class UIEvent {
    data object Swipe : UIEvent()
    data class From(val form: String) : UIEvent()
    data class To(val to: String) : UIEvent()
    data class Amount(val amount: String) : UIEvent()

}