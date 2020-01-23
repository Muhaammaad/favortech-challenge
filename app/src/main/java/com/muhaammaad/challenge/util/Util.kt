package  com.muhaammaad.challenge.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

object Util {

    fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    @JvmStatic
    fun getBitmapWithByteArray(thumnail: String): Bitmap {
        val thumbnailBytes = convertStringToBytes(thumnail)
        return BitmapFactory.decodeByteArray(thumbnailBytes, 0, thumbnailBytes.size)
    }

    private fun convertStringToBytes(thumbnail: String): ByteArray {
        return Base64Coder.decode(thumbnail)
    }

    @JvmStatic
    fun getBitmapBytes(bmp: Bitmap?): String {
        val stream = ByteArrayOutputStream()
        var result = ""
        if (bmp != null) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 20, stream)
            val byteArray = stream.toByteArray()
            result = String(Base64Coder.encode(byteArray))
//            val result1 = String(byteArray, Charset.forName("UTF-8"))
//            Log.e("Downloaded Image", "normal bytes length: " + byteArray.size + " , String length: " + result1.length)
            try {
                stream.flush()
                stream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return result
    }
}