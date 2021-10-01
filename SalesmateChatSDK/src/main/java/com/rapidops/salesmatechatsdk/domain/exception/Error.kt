package com.rapidops.salesmatechatsdk.domain.exception

class Error(var apiCode: Int, var name: String, var message: String, var httpCode: Int = 0)