package com.rapidops.salesmatechatsdk.domain.usecases

import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.domain.datasources.INameDataSource
import javax.inject.Inject


class GetDataUseCase @Inject constructor(private val nameDataSource: INameDataSource) :
    UseCase<String, JsonElement>() {


    override suspend fun execute(params: String?): JsonElement {
        return nameDataSource.getData()
    }


}

