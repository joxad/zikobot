package com.startogamu.zikobot.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu

import com.joxad.easydatabinding.activity.ActivityBase
import com.startogamu.zikobot.R
import com.startogamu.zikobot.BR
import com.startogamu.zikobot.databinding.HomeActivityBinding

/**
 * Created by Jocelyn on 27/07/2017.
 */

class HomeActivity : ActivityBase<HomeActivityBinding, HomeActivityVM>() {
    companion object {

        fun newInstance(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    override fun data(): Int {
        return BR.vm
    }

    override fun layoutResources(): Int {
        return R.layout.home_activity
    }

    override fun baseActivityVM(binding: HomeActivityBinding?, savedInstanceState: Bundle?): HomeActivityVM {
        return HomeActivityVM(this, binding, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}
