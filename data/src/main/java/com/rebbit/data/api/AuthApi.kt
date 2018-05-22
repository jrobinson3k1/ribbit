package com.rebbit.data.api

import com.rebbit.data.model.AccessTokenResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("v1/access_token")
    fun getAccessToken(@Header("Authorization") authorization: String, @Field("grant_type") grantType: String, @Field("device_id") deviceId: String, @Field("scope") scope: String): Single<AccessTokenResponse>
}