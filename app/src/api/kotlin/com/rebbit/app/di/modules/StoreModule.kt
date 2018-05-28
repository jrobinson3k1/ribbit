package com.rebbit.app.di.modules

import android.content.Context
import android.preference.PreferenceManager
import com.rebbit.app.di.AppScope
import com.rebbit.app.store.TokenStore
import com.rebbit.app.store.UserStore
import dagger.Module
import dagger.Provides

@Module
class StoreModule {

    @Provides
    @AppScope
    fun providesTokenStore(context: Context): TokenStore = TokenStore(PreferenceManager.getDefaultSharedPreferences(context))

    @Provides
    @AppScope
    fun providesUserStore(context: Context): UserStore = UserStore(PreferenceManager.getDefaultSharedPreferences(context))
}