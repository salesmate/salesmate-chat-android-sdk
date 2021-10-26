package com.rapidops.salesmatechatsdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class LastMessageData(

	@field:SerializedName("unique_id")
	var uniqueId: String = "",

	@field:SerializedName("user_id")
	var userId: String = "",

	@field:SerializedName("conversation_id")
	var conversationId: String = "",

	@field:SerializedName("message_type")
	var messageType: String = "",

	@field:SerializedName("id")
	var id: String = "",

	@field:SerializedName("contact_id")
	var contactId: String = "",

	@field:SerializedName("verified_id")
	var verifiedId: String = "",

	@field:SerializedName("message_summary")
	var messageSummary: String = ""
) : BaseModel()