package com.example.currency.data


import javax.inject.Inject
import kotlin.jvm.Throws

interface ConvertRepository {

    @Throws
    suspend fun getRate(base: String, target: String): Double

    @Throws
    suspend fun getCurrencyList(): List<String>
}

class TestConvertRepositoryImp : ConvertRepository {
    override suspend fun getRate(base: String, target: String) = if (base == "EGP") 0.12 else 8.24
    override suspend fun getCurrencyList() = listOf("EGP", "SR")
}

class ConvertRepositoryImp @Inject constructor(
    private val apiService: ApiService,
) : ConvertRepository {

    override suspend fun getRate(base: String, target: String): Double {
        val response = apiService.latest(base = base, symbols = target)
        if (response.isSuccessful) {
            val rate = response
                .body()
                ?.rates?.values?.firstOrNull()

            if (rate != null) {
                return rate
            } else {
                throw Exception(response.body()?.error?.toString() ?: "error")
            }
        } else throw Exception("Network Error")
    }

    override suspend fun getCurrencyList(): List<String> {
        val response = apiService.getSymbols()
        if (response.isSuccessful) {
            val list = response
                .body()?.symbols?.keys?.toList()

            if (list != null) {
                return list
            } else {
                throw Exception(response.body()?.error?.toString() ?: "error")
            }
        } else throw Exception("Network Error")
    }
}