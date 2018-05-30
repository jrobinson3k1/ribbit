package com.rebbit.app.util.glide

import android.graphics.*
import android.os.Build
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


class CircleOutlineTransformation(val color: Int) : BitmapTransformation() {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        return circleCropOutline(pool, toTransform, color, outWidth, outHeight)
    }

    companion object {
        private const val VERSION = 1
        private const val ID = "com.rebbit.app.util.glide.CircleOutlineTransformation.$VERSION"
        private const val CIRCLE_CROP_PAINT_FLAGS = Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG

        private val ID_BYTES = ID.toByteArray(Key.CHARSET)
        private val CIRCLE_CROP_SHAPE_PAINT = Paint(CIRCLE_CROP_PAINT_FLAGS)
        private val CIRCLE_CROP_BITMAP_PAINT = Paint(CIRCLE_CROP_PAINT_FLAGS).apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN) }

        // Modified from TransformationUtils.circleCrop(...)
        fun circleCropOutline(pool: BitmapPool, inBitmap: Bitmap, color: Int, destWidth: Int, destHeight: Int): Bitmap {
            val destMinEdge = Math.min(destWidth, destHeight)
            val radius = destMinEdge / 2f

            val srcWidth = inBitmap.width
            val srcHeight = inBitmap.height

            val scaleX = destMinEdge / srcWidth.toFloat()
            val scaleY = destMinEdge / srcHeight.toFloat()
            val maxScale = Math.max(scaleX, scaleY)

            val scaledWidth = maxScale * srcWidth
            val scaledHeight = maxScale * srcHeight
            val left = (destMinEdge - scaledWidth) / 2f
            val top = (destMinEdge - scaledHeight) / 2f

            val destRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

            // Alpha is required for this transformation.
            val toTransform = getAlphaSafeBitmap(pool, inBitmap)

            val outConfig = getAlphaSafeConfig(inBitmap)
            val result = pool.get(destMinEdge, destMinEdge, outConfig)
            result.setHasAlpha(true)

            val canvas = Canvas(result)

            // Draw a circle
            canvas.drawCircle(radius, radius, radius, CIRCLE_CROP_SHAPE_PAINT)

            // Draw the bitmap in the circle
            canvas.drawBitmap(toTransform, null, destRect, CIRCLE_CROP_BITMAP_PAINT)

            // Draw the outline
            val paint = Paint().apply { isAntiAlias = true }
            paint.style = Paint.Style.STROKE
            paint.color = color
            paint.strokeWidth = 12F
            canvas.drawCircle(radius, radius, radius - paint.strokeWidth / 2, paint)

            canvas.setBitmap(null)

            if (toTransform != inBitmap) {
                pool.put(toTransform)
            }

            return result
        }

        private fun getAlphaSafeBitmap(
                pool: BitmapPool, maybeAlphaSafe: Bitmap): Bitmap {
            val safeConfig = getAlphaSafeConfig(maybeAlphaSafe)
            if (safeConfig == maybeAlphaSafe.config) {
                return maybeAlphaSafe
            }

            val argbBitmap = pool.get(maybeAlphaSafe.width, maybeAlphaSafe.height, safeConfig)
            Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0f /*left*/, 0f /*top*/, null /*paint*/)

            // We now own this Bitmap. It's our responsibility to replace it in the pool outside this method
            // when we're finished with it.
            return argbBitmap
        }

        private fun getAlphaSafeConfig(inBitmap: Bitmap): Bitmap.Config {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Avoid short circuiting the sdk check.
                if (Bitmap.Config.RGBA_F16 == inBitmap.config) {
                    return Bitmap.Config.RGBA_F16
                }
            }

            return Bitmap.Config.ARGB_8888
        }
    }
}