package com.rapidops.salesmatechatsdk.domain.exception

import com.google.gson.JsonParser
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException

internal object APIResponseMapper {

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
                throw SalesmateChatException(SalesmateChatException.Kind.REST_API, error)
            } ?: throw SalesmateChatException(SalesmateChatException.Kind.UNEXPECTED)
        } catch (e: Exception) {
            when (e) {
                is IOException -> throw SalesmateChatException(SalesmateChatException.Kind.NETWORK)
                is SalesmateChatException -> throw e
                else -> throw SalesmateChatException(SalesmateChatException.Kind.UNEXPECTED)
            }
        }
    }

    private fun decodeErrorResponse(errorBody: ResponseBody): Error {
        val jsonObject = JsonParser.parseString(errorBody.string()).asJsonObject
        return GsonUtils.gson.fromJson(jsonObject, Error::class.java)
        /*var apiErrorCode = 0
        var errorName = ""
        var errorMessage = ""

        val jsonObject = JsonParser.parseString(errorBody.string()).asJsonObject
        if (jsonObject.has("Error") && jsonObject.get("Error") != null) {
            val errorJsonObj = jsonObject.get("Error").asJsonObject
            if (errorJsonObj.has("Code")) {
                apiErrorCode = errorJsonObj.get("Code").asInt
            }
            if (errorJsonObj.has("code")) {
                apiErrorCode = errorJsonObj.get("code").asInt
            }
            if (errorJsonObj.has("Name")) {
                errorName = errorJsonObj.get("Name").asString
            }
            if (errorJsonObj.has("name")) {
                errorName = errorJsonObj.get("name").asString
            }
            if (errorJsonObj.has("Message")) {
                errorMessage = errorJsonObj.get("Message").asString
            }
            if (errorJsonObj.has("message")) {
                errorMessage = errorJsonObj.get("message").asString
            }
        }

        return Error(
            apiErrorCode,
            errorName,
            errorMessage
        )*/
    }
}