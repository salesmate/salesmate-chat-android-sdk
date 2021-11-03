package com.rapidops.salesmatechatsdk.app.utils

import android.content.Context
import android.media.MediaPlayer
import com.rapidops.salesmatechatsdk.R

internal object MediaUtil {
    fun playMedia(context: Context, playType: PlayType) {
        val mp = MediaPlayer.create(context, playType.value)
        mp.start()
    }
}

internal enum class PlayType(val value: Int) {
    SEND(R.raw.sound_chat),
    RECEIVE(R.raw.sound_chat),
    FAIL(R.raw.sound_chat)
}