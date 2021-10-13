package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.User
import javax.inject.Inject

internal class GetUserFromUserIdUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource
) : UseCase<String, User>() {
    override suspend fun execute(params: String?): User {
        val workspaceData = appSettingsDataSource.pingRes.workspaceData
        return if (params.isNullOrEmpty()) {
            User(id = null, firstName = workspaceData?.name ?: "")
        } else {
            appSettingsDataSource.pingRes.users.find { it.id == params } ?: run {
                User(id = null, firstName = workspaceData?.name ?: "")
            }
        }
    }
}