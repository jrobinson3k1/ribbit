package com.rebbit.data.api

import com.nhaarman.mockito_kotlin.*
import com.rebbit.data.model.AccessTokenResponse
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AuthClientTest {

    private lateinit var client: AuthClient

    @Mock
    private lateinit var authApi: AuthApi

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        client = AuthClient(authApi, "clientId")
    }

    @Test
    fun testAccessToken() {
        val accessTokenResponse = mock<AccessTokenResponse>()
        whenever(authApi.getAccessToken(any(), any(), any(), any())).thenReturn(Single.just(accessTokenResponse))

        client.getAccessToken("grantType", "deviceId", "scope")
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(accessTokenResponse)

        verify(authApi).getAccessToken(eq("Basic clientId"), eq("grantType"), eq("deviceId"), eq("scope"))
    }
}