package com.rapidops.salesmatechatsdk.app.interfaces

import com.rapidops.salesmatechatsdk.domain.exception.SalesmateException

interface UpdateListener {
    fun onUpdate()
    fun onError(salesmateException: SalesmateException)
}