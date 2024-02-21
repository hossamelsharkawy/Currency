package com.example.currency.data


data class SymbolsResponse(
    var success: Boolean = false,
    var symbols: Map<String, String> = emptyMap(),
    val error:ApiError?=null
)

