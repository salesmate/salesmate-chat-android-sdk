package com.rapidops.salesmatechatsdk.domain.exception


internal class SalesmateChatException(var kind: Kind, var error: Error? = null) : Exception() {

    enum class Kind {
        NETWORK,
        REST_API,
        UNEXPECTED
    }


}