package com.startogamu.zikobot.player.alarm

import android.os.Bundle

import com.joxad.easydatabinding.bottomsheet.DialogBottomSheetBase
import com.startogamu.zikobot.BR
import com.startogamu.zikobot.Constants
import com.startogamu.zikobot.R
import com.startogamu.zikobot.databinding.AlarmFragmentBinding

/**
 * Created by Jocelyn on 22/08/2017.
 */

class AlarmBottomFragment : DialogBottomSheetBase<AlarmFragmentBinding, AlarmBottomFragmentVM>() {
    override fun data(): Int {
        return BR.alarmFragmentVM
    }

    override fun layoutResources(): Int {
        return R.layout.alarm_fragment
    }

    override fun baseFragmentVM(binding: AlarmFragmentBinding): AlarmBottomFragmentVM {
        return AlarmBottomFragmentVM(this, binding)
    }

    companion object {


        fun newInstance(id: Long): AlarmBottomFragment {
            val args = Bundle()
            val fragment = AlarmBottomFragment()
            args.putLong(Constants.Extra.PLAYLIST_ID, id)
            fragment.arguments = args
            return fragment
        }
    }


}
