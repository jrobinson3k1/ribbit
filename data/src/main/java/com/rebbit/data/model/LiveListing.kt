package com.rebbit.data.model

import android.arch.paging.PagedList
import io.reactivex.Observable

data class LiveListing<T>(
        val pagedList: Observable<PagedList<T>>,
        val networkState: Observable<NetworkState>,
        val refreshState: Observable<NetworkState>,
        val refresh: () -> Unit,
        val retry: () -> Unit)