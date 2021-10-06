package com.rapidops.salesmatechatsdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class Conversations(

	@field:SerializedName("last_message_date")
	var lastMessageDate: String = "",

	@field:SerializedName("unique_id")
	var uniqueId: String = "",

	@field:SerializedName("session_id")
	var sessionId: String = "",

	@field:SerializedName("contact_id")
	var contactId: String = "",

	@field:SerializedName("verified_id")
	var verifiedId: String = "",

	@field:SerializedName("last_participating_user_id")
	var lastParticipatingUserId: String = "",

	@field:SerializedName("brand_id")
	var brandId: String = "",

	@field:SerializedName("owner_user")
	var ownerUser: String = "",

	@field:SerializedName("contact_has_read")
	var contactHasRead: Boolean = false,

	@field:SerializedName("lastMessageData")
	var lastMessageData: LastMessageData? = null,

	@field:SerializedName("name")
	var name: String = "",

	@field:SerializedName("id")
	var id: String = "",

	@field:SerializedName("created_date")
	var createdDate: String = "",

	@field:SerializedName("email")
	var email: String = "",

	@field:SerializedName("status")
	var status: String = ""
) : BaseModel()