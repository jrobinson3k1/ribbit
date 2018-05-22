package com.rebbit.app.mvp

interface Presenter<in V : View> {

    fun bind(view: V)

    fun unBind()
}