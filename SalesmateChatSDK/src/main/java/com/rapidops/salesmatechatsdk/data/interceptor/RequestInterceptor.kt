package com.rapidops.salesmatechatsdk.data.interceptor

import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

internal class RequestInterceptor(
    private val appSettingsDataSource: IAppSettingsDataSource,
) : Interceptor {

    companion object {
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val newRequestBuilder: Request.Builder = request.newBuilder()
        addRequiredHeader(request, newRequestBuilder)
        request = newRequestBuilder.build()

        return chain.proceed(request)
    }

    private fun addRequiredHeader(request: Request, requestBuilder: Request.Builder) {
        requestBuilder.addHeader("x-contact-id", "")
//        requestBuilder.addHeader("x-unique-id", appSettingsDataSource.androidUniqueId)
        requestBuilder.addHeader("x-unique-id", "a44dc310-f254-4cbd-9d95-42392d0f2a08")
        requestBuilder.addHeader("x-linkname", appSettingsDataSource.salesMateChatSetting.tenantId)
        requestBuilder.addHeader(
            "x-workspace-id",
            appSettingsDataSource.salesMateChatSetting.workspaceId
        )
        requestBuilder.addHeader("x-verified-id", "")
    }
}
