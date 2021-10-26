package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.app.utils.FileUtil.isImageType
import com.rapidops.salesmatechatsdk.data.reqmodels.Attachment
import com.rapidops.salesmatechatsdk.data.reqmodels.Blocks
import com.rapidops.salesmatechatsdk.data.reqmodels.SendMessageReq
import com.rapidops.salesmatechatsdk.data.resmodels.UploadFileRes
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import com.rapidops.salesmatechatsdk.domain.models.BlockType
import java.io.File
import javax.inject.Inject


internal class UploadFileUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val conversationDataSource: IConversationDataSource,
    private val sendMessageUseCase: SendMessageUseCase,
) :
    UseCase<UploadFileUseCase.Param, SendMessageReq>() {


    override suspend fun execute(params: Param?): SendMessageReq {
        val uploadFileParam = params!!

        val uploadFileResponse = conversationDataSource.uploadFile(uploadFileParam.file)

        return getNewAttachmentSendMessageReq(uploadFileResponse, uploadFileParam.messageId)

    }

    data class Param(val file: File, val messageId: String)

    private fun getNewAttachmentSendMessageReq(
        response: UploadFileRes,
        messageId: String
    ): SendMessageReq {
        val blocks = Blocks()
        blocks.type = if (response.contentType.isImageType())
            BlockType.IMAGE.value
        else
            BlockType.FILE.value

        val attachment = Attachment()
        attachment.contentType = response.contentType
        attachment.gcpFileName = response.path
        attachment.gcpThumbnailFileName = response.thumbnailPath
        attachment.name = response.fileName
        attachment.thumbnail = response.thumbnailUrl
        blocks.attachment = attachment

        val sendMessageReq = sendMessageUseCase.getNewSendMessageReq(messageId)
        sendMessageReq.blockData.add(blocks)
        return sendMessageReq
    }

}

