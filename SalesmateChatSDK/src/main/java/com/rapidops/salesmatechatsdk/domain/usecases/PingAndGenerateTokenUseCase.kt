package com.rapidops.salesmatechatsdk.domain.usecases

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


internal class PingAndGenerateTokenUseCase @Inject constructor(
    private val generateTokenUseCase: GenerateTokenUseCase,
    private val pingUseCase: PingUseCase
) :
    UseCase<Nothing, Unit>() {

    override suspend fun execute(params: Nothing?) {
        coroutineScope {
            awaitAll(
                async {
                    pingUseCase.execute()
                },
                async {
                    generateTokenUseCase.execute()
                }
            )
        }
    }


}

