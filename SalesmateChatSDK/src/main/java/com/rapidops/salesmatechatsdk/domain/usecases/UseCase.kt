package com.rapidops.salesmatechatsdk.domain.usecases

abstract class UseCase<INPUT_TYPE, OUTPUT_TYPE> {
    abstract suspend fun execute(params: INPUT_TYPE? = null): OUTPUT_TYPE
}