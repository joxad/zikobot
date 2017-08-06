package com.startogamu.zikobot.ftu

import android.content.Context
import android.databinding.Bindable
import android.databinding.ObservableBoolean
import android.view.View

import com.joxad.easydatabinding.base.BaseVM
import com.joxad.zikobot.data.module.accounts.ZikoAccount
import com.startogamu.zikobot.R

/**
 * Created by Jocelyn on 06/08/2017.
 */

abstract class AAccountVM
/***

 * @param context
 * *
 * @param model
 */
(context: Context, model: ZikoAccount) : BaseVM<ZikoAccount>(context, model) {

    lateinit var show: ObservableBoolean
    lateinit var loading: ObservableBoolean

    override fun onCreate() {
        show = ObservableBoolean(false)
        loading = ObservableBoolean(false)
    }

    abstract fun onClick(view: View)

    abstract fun deleteClick(view: View)

    @get:Bindable
    abstract val title: String

    val buttonText: String
        @Bindable
        get() = context.getString(R.string.connect)

    @get:Bindable
    abstract val icon: Int

    @get:Bindable
    abstract val userName: String?

    @get:Bindable
    abstract val userImage: String?


    @get:Bindable
    abstract val color: Int


}
