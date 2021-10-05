package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class SecuritySettings(
    @SerializedName("can_upload_attachment")
    var canUploadAttachment: Boolean = false,
    @SerializedName("trusted_domains")
    var trustedDomains: Any? = null
) : BaseModel()