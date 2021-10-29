package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.app.extension.decrypt
import com.rapidops.salesmatechatsdk.app.utils.FileUtil
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import java.io.File
import javax.inject.Inject


internal class DownloadTranscriptUseCase @Inject constructor(
    private val conversationDataSource: IConversationDataSource,
) :
    UseCase<String, File>() {


    override suspend fun execute(params: String?): File {
        val conversationId = params!!
        val response = conversationDataSource.downloadTranscript(conversationId)
        val data = response.data.decrypt()
        return FileUtil.createTextFile(conversationId, data)
    }

    data class Param(val conversationId: String)

}

