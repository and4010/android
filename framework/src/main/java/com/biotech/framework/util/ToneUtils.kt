package com.biotech.framework.util

import android.content.Context
import android.media.*
import android.os.Build
import androidx.annotation.RequiresApi

class ToneUtils{
    companion object{

        var audioTrack : AudioTrack? = null

        fun playSound(frequency: Int){
            try {
                val minBufferSize = AudioTrack.getMinBufferSize(
                    frequency,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                val player = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(44100)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                            .build()
                    )
                    .setBufferSizeInBytes(minBufferSize)
                    .build()
                player.play()
                Thread.sleep(800)
                player.stop()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        fun playSound(context: Context?, resId: Int) {
            val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            val soundPool = SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(5)
                .build()
            val soundId = soundPool.load(context, resId, 1)
            soundPool.setOnLoadCompleteListener { p0: SoundPool?, p1: Int, p2: Int ->
                if (p2 == 0) soundPool.play(
                    soundId,
                    1f,
                    1f,
                    0,
                    0,
                    1.0f
                )
            }
        }

        fun createAudioTrack(): AudioTrack {
            if (audioTrack == null) {
                val minBufferSize = AudioTrack.getMinBufferSize(
                    44100,
                    AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(44100)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                            .build()
                    )
                    .setBufferSizeInBytes(minBufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()
            }
            return audioTrack!!
        }

        fun releaseAudioTrack() {
            if (audioTrack != null) {
                audioTrack!!.stop()
                audioTrack!!.release()
            }
        }

        private fun generateTone(freqHz: Double, durationMs: Int): ShortArray {
            val count = (44100.0 * 2.0 * (durationMs / 1000.0)).toInt() and 1.inv()
            val samples = ShortArray(count)
            val size = count * (java.lang.Short.SIZE / 8)
            //        Log.d(TAG, freqHz + "Hz for " + durationMs + "ms = " + count + " samples at 44.1Khz 2ch = " + size + " bytes");
            var i = 0
            while (i < count) {
                val sample = (Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF * .75).toShort()
                samples[i] = sample
                samples[i + 1] = sample
                i += 2
            }
            return samples
        }

        @Synchronized
        private fun play(track: AudioTrack, samples: ShortArray) {
            track.play()
            track.write(samples, 0, samples.size)
        }

        @Synchronized
        fun playError() {
            try {
                val audioTrack = createAudioTrack()
                play(audioTrack, generateTone(500.0, 400))
                Thread.sleep(200)
                play(audioTrack, generateTone(500.0, 200))
                Thread.sleep(200)
                play(audioTrack, generateTone(500.0, 200))
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }

        @Synchronized
        fun playNormal() {
            try {
                val audioTrack = createAudioTrack()
                play(audioTrack, generateTone(300.0, 200))
                audioTrack.flush()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }

        @Synchronized
        fun playWarning() {
            try {
                val audioTrack = createAudioTrack()
                play(audioTrack, generateTone(500.0, 200))
                audioTrack.flush()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }

        @Synchronized
        fun playNumberKey() {
            try {
                val audioTrack = createAudioTrack()
                play(audioTrack, generateTone(500.0, 100))
                audioTrack.flush()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }

        @Synchronized
        fun playAphaKey() {
            try {
                val audioTrack = createAudioTrack()
                play(audioTrack, generateTone(500.0, 100))
                audioTrack.flush()
                Thread.sleep(50)
                play(audioTrack, generateTone(500.0, 100))
                audioTrack.flush()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }


    }
}

