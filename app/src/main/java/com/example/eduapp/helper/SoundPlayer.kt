package com.example.eduapp.helper

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.eduapp.R

class SoundPlayer(context: Context) {

    private val pool: SoundPool = SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val correctId = pool.load(context, R.raw.correct, 1)
    private val wrongId = pool.load(context, R.raw.wrong, 1)

    fun playCorrect(enabled: Boolean, volume: Float) = play(correctId, enabled, volume)

    fun playWrong(enabled: Boolean, volume: Float) = play(wrongId, enabled, volume)

    private fun play(id: Int, enabled: Boolean, volume: Float) {
        if (!enabled) return
        val level = volume.coerceIn(0.1f, 1f)
        pool.play(id, level, level, 1, 0, 1f)
    }

    fun release() = pool.release()
}
