package com.example.eduapp.helper

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.eduapp.R

/**
 * Plays the short answer sounds.
 *
 * SoundPool rather than MediaPlayer: these clips are tiny and need to fire
 * instantly, and SoundPool keeps them decoded in memory.
 */
class SoundPlayer(context: Context) {

    private val pool: SoundPool = SoundPool.Builder()
        .setMaxStreams(2)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val ready = mutableSetOf<Int>()
    private val correctId = pool.load(context, R.raw.correct, 1)
    private val wrongId = pool.load(context, R.raw.wrong, 1)

    init {
        // Loading is asynchronous, so we only play clips that have arrived.
        pool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) ready.add(sampleId)
        }
    }

    fun playCorrect(enabled: Boolean, volume: Float) = play(correctId, enabled, volume)

    fun playWrong(enabled: Boolean, volume: Float) = play(wrongId, enabled, volume)

    private fun play(id: Int, enabled: Boolean, volume: Float) {
        if (!enabled || volume <= 0f || id !in ready) return
        pool.play(id, volume, volume, 1, 0, 1f)
    }

    fun release() = pool.release()
}
