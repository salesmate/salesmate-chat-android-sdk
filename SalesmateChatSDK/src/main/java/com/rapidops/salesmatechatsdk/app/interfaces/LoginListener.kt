package com.rapidops.salesmatechatsdk.app.interfaces

import com.rapidops.salesmatechatsdk.domain.exception.SalesmateException

interface LoginListener {
    fun onLogin()
    fun onError(salesmateException: SalesmateException)
}