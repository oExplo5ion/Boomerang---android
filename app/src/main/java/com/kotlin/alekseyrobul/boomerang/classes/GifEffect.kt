package com.kotlin.alekseyrobul.boomerang.classes

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.kotlin.alekseyrobul.boomerang.helpers.AnimatedGifEncoder
import com.kotlin.alekseyrobul.boomerang.helpers.FileUtilitty
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URI

class GifEffect {
    companion object {
        @JvmStatic
        fun gifFromVideo(context: Context, uri: Uri, result:(Uri?) -> Unit) {
            // get images
            val media = MediaMetadataRetriever()
            media.setDataSource(context, uri)

            var imgs = arrayListOf<Bitmap>()
            var i:Long = 1000000 // frame time in milliseconds
            var seconds = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt() / 1000

            if (seconds <= 1 ) { return }

            while (seconds > 1) {
                val img = media.getFrameAtTime(i)
                imgs.add(img)
                i += 1000000
                seconds -= 1
            }

            val byteArrayOutputStream = ByteArrayOutputStream()
            val gifEncoder = AnimatedGifEncoder()
            gifEncoder.start(byteArrayOutputStream)
            gifEncoder.setRepeat(0)
            for (i in imgs) {
                gifEncoder.addFrame(i)
            }
            gifEncoder.finish()
            val byteArray = byteArrayOutputStream.toByteArray()

            val folderFile = FileUtilitty.externalMediaFolder(context)
            val gifFile = File(folderFile.absolutePath + File.separator + "giff_effect.gif")

            val outs = FileOutputStream(gifFile.absolutePath)
            outs.write(byteArray)
            outs.close()

            if (gifFile.exists()) {
                result(Uri.parse(gifFile.toURI().toString()))
            } else {
                result(null)
            }
        }
    }
}



















