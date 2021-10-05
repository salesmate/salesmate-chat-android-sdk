package com.rapidops.salesmatechatsdk.domain.exception

internal class Error(var apiCode: Int, var name: String, var message: String, var httpCode: Int = 0)