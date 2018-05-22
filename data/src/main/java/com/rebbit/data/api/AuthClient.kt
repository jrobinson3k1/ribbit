package com.rebbit.data.api

class AuthClient(private val authApi: AuthApi, private val clientId: String) {

    fun getAccessToken(grantType: String, deviceId: String, scope: String) = authApi.getAccessToken("Basic $clientId", grantType, deviceId, scope)
}