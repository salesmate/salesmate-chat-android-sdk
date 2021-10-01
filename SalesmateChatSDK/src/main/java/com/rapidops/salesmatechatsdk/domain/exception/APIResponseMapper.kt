package com.rapidops.salesmatechatsdk.domain.exception

import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException

object APIResponseMapper {

    suspend fun <T> getResponse(call: suspend () -> Response<T>): T {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return body
            }
            response.errorBody()?.let { requestBody ->
                val error = decodeErrorResponse(requestBody)
                error.httpCode = response.code()
                throw SalesMateChatException(SalesMateChatException.Kind.REST_API, error)
            } ?: throw SalesMateChatException(SalesMateChatException.Kind.UNEXPECTED)
        } catch (e: Exception) {
            when (e) {
                is IOException -> throw SalesMateChatException(SalesMateChatException.Kind.NETWORK)
                is SalesMateChatException -> throw e
                else -> throw SalesMateChatException(SalesMateChatException.Kind.UNEXPECTED)
            }
        }
    }

    private fun decodeErrorResponse(errorBody: ResponseBody): Error {
        var apiErrorCode = 0
        var errorName = ""
        var errorMessage = ""

        val jsonObject = JsonParser.parseString(errorBody.string()).asJsonObject
        if (jsonObject.has("Error") && jsonObject.get("Error") != null) {
            val errorJsonObj = jsonObject.get("Error").asJsonObject
            if (errorJsonObj.has("Code")) {
                apiErrorCode = errorJsonObj.get("Code").asInt
            }
            if (errorJsonObj.has("Name")) {
                errorName = errorJsonObj.get("Name").asString
            }
            if (errorJsonObj.has("Message")) {
                errorMessage = errorJsonObj.get("Message").asString
            }
        }

        return Error(
            apiErrorCode,
            errorName,
            errorMessage
        )
    }
}