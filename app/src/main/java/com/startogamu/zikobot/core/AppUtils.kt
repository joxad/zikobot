package com.startogamu.zikobot.core

import android.support.v7.app.AppCompatActivity
import android.view.View
import com.joxad.zikobot.data.db.model.ZikoArtist
import com.startogamu.zikobot.databinding.ToolbarDetailActivityBinding
import com.viethoa.RecyclerViewFastScroller
import com.viethoa.models.AlphabetItem

/**
 * Created by Jocelyn on 15/08/2017.
 */

object AppUtils {
    fun initToolbar(activity: AppCompatActivity, toolbarDetailBinding: ToolbarDetailActivityBinding) {
        activity.setSupportActionBar(toolbarDetailBinding.toolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar!!.setHomeButtonEnabled(true)
        activity.supportActionBar!!.setDisplayShowTitleEnabled(true)
        toolbarDetailBinding.toolbar.setNavigationOnClickListener({ activity.onBackPressed() })
        toolbarDetailBinding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val alpha: Float? = Math.abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
            toolbarDetailBinding.alphaToolbar = alpha
        }
    }

    fun animateAlpha(view: View) {
        view.animate().setStartDelay(500).alpha(1f).setDuration(300)
                .withStartAction {
                    view.alpha = 0f
                    view.visibility = View.VISIBLE
                }
                .withEndAction({ view.visibility = View.VISIBLE }).start()
    }

    fun setupAlphabet(fastScroller: RecyclerViewFastScroller, it: MutableList<String>) {
        val mAlphabetItems = arrayListOf<AlphabetItem>()
        val strAlphabets = arrayListOf<String>()
        for (name in it) {
            val word = name.substring(0, 1)
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word)
                mAlphabetItems.add(AlphabetItem(strAlphabets.size, word, false))
            }
        }

        fastScroller.setUpAlphabet(mAlphabetItems)
    }
}
