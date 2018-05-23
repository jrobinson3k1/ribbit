package com.rebbit.app.di.modules

import com.github.salomonbrys.kotson.registerTypeAdapter
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.rebbit.app.di.AppScope
import com.rebbit.app.store.TokenStore
import com.rebbit.data.api.AuthApi
import com.rebbit.data.api.AuthClient
import com.rebbit.data.api.ListingApi
import com.rebbit.data.api.ListingClient
import com.rebbit.data.model.Link
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Named

@Module(includes = [
    StoreModule::class,
    TokenModule::class
])
class ApiModule {

    @Provides
    @Named("io_scheduler")
    fun providesIoScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Named("ui_scheduler")
    fun providesUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }).build()

    @Provides
    fun providesGsonBuilder(): GsonBuilder = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)

    @Provides
    @AppScope
    @Named("OAuth")
    fun providesOAuthRetrofit(authClient: AuthClient, client: OkHttpClient, gsonBuilder: GsonBuilder, tokenStore: TokenStore): Retrofit {
        val clientBuilder = client.newBuilder()
                .addInterceptor {
                    val request = it.request().newBuilder().apply {
                        if (tokenStore.isExpired()) retrieveToken(authClient, tokenStore)
                        tokenStore.accessToken?.let { addAuthHeader(it) }
                    }.build()
                    it.proceed(request)
                }
                .authenticator { _, response ->
                    if (response.responseCount() >= 2) {
                        Timber.d("Failed to retrieve access token")
                        return@authenticator null
                    }

                    Timber.d("Got 401, retrieving access token")
                    val accessToken = retrieveToken(authClient, tokenStore)
                    return@authenticator response.request().newBuilder()
                            .addAuthHeader(accessToken)
                            .build()
                }

        return Retrofit.Builder()
                .baseUrl("https://oauth.reddit.com/")
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder
                        .registerTypeAdapter<Int> {
                            deserialize { it.json.asIntOrNull }
                        }
                        .registerTypeAdapter<Link.PostHint> {
                            deserialize { Link.PostHint.fromValue(it.json.asString) }
                        }
                        .create()))
                .build()
    }

    @Provides
    @AppScope
    @Named("Api")
    fun providesApiRetrofit(client: OkHttpClient, gsonBuilder: GsonBuilder): Retrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com/api/")
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .build()

    @Provides
    @AppScope
    fun providesListingClient(@Named("OAuth") retrofit: Retrofit): ListingClient = ListingClient(retrofit.create(ListingApi::class.java))

    @Provides
    @AppScope
    fun providesAuthClient(@Named("Api") retrofit: Retrofit, @Named("token") token: String): AuthClient = AuthClient(retrofit.create(AuthApi::class.java), token)

    private fun retrieveToken(authClient: AuthClient, tokenStore: TokenStore): String {
        authClient.getAccessToken("https://oauth.reddit.com/grants/installed_client", tokenStore.uuid, "read").blockingGet().let {
            tokenStore.putAccessToken(it.accessToken, it.expiresIn)
            return it.accessToken
        }
    }

    private fun Request.Builder.addAuthHeader(accessToken: String) = addHeader("Authorization", "bearer $accessToken")

    private fun Response.responseCount(): Int {
        var result = 1
        var priorResponse = priorResponse()
        while (priorResponse != null) {
            result++
            priorResponse = priorResponse.priorResponse()
        }

        return result
    }

    private val JsonElement.asIntOrNull: Int?
        get() {
            return try {
                asInt
            } catch (e: Exception) {
                null
            }
        }
}
