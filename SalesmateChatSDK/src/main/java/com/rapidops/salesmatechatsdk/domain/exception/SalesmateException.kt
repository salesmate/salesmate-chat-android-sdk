package com.rapidops.salesmatechatsdk.domain.exception

class SalesmateException(
    val errorCode: String,
    val errorMessage: String,
    exception: Exception? = null
) :
    Exception(exception) {

    companion object {
        const val EXCEPTION_USER_ID = "1001"
        const val EXCEPTION_NO_INTERNET = "1002"
        const val EXCEPTION_UN_EXPECTED_ERROR = "1003"

        val EmptyUserIdException = SalesmateException(
            EXCEPTION_USER_ID,
            "Please enter Valid user_id, blank user_id not allowed"
        )

        val NoInternetException = SalesmateException(
            EXCEPTION_NO_INTERNET,
            "No network connection. Please try again"
        )

        val UnExpectedError = SalesmateException(
            EXCEPTION_UN_EXPECTED_ERROR,
            "Something went wrong. Please try again later."
        )
    }

}