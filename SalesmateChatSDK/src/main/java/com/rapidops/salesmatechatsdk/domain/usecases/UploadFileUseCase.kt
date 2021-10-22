package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.data.resmodels.UploadFileRes
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import java.io.File
import javax.inject.Inject


internal class UploadFileUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val conversationDataSource: IConversationDataSource,
) :
    UseCase<UploadFileUseCase.Param, UploadFileRes>() {


    override suspend fun execute(params: Param?): UploadFileRes {
        val uploadFileParam = params!!


        return conversationDataSource.uploadFile(uploadFileParam.file)

    }

    data class Param(val file: File)

}

