@file:Suppress("unused")

package sun.mercy.mvpsuns.demo.app.utils

import android.graphics.*
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.View
import io.rong.imlib.model.UserInfo
import java.io.*
import java.util.*


/**
 * @author sun
 * @date 2018/2/11
 * RongGenerate
 */
object RongGenerate {

    private var SAVEADDRESS = Environment.getExternalStorageDirectory().absolutePath + "/suns/temp"
    private const val SCHEMA = "file://"


    fun generateDefaultAvatar(username: String?, userId: String): String {

        var s: String? = null
        if (username.isNullOrEmpty().not()) {
            s = username?.get(0).toString()
        }
        if (s == null) {
            s = "A"
        }
        val color = getColorRGB(userId)
        val string = getAllFirstLetter(username)
        createDir(SAVEADDRESS)
        val f = File(SAVEADDRESS, "${string}_$userId")
        if (f.exists()) {
            return SCHEMA + f.path
        }
        val paint = Paint()
        paint.color = Color.WHITE
        paint.textSize = 220f
        paint.isAntiAlias = true
        val width = 480
        val height = 480
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.parseColor(color))
        val rect = Rect()
        paint.getTextBounds(s, 0, s.length, rect)
        val fm = paint.fontMetrics
        val textLeft = ((width - paint.measureText(s)) / 2).toInt()
        val textTop = (height - width / 2 + Math.abs(fm.ascent) / 2 - 25).toInt()
        canvas.drawText(s, textLeft.toFloat(), textTop.toFloat(), paint)
        return saveBitmap(bitmap, "${string}_$userId")
    }

    fun generateDefaultAvatar(userInfo: UserInfo?): String? {
        return if (userInfo == null)
            null
        else
            generateDefaultAvatar(userInfo.name, userInfo.userId)
    }

    private fun createDir(saveAddress: String) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val destDir = File(saveAddress)
            if (!destDir.exists()) {
                destDir.mkdirs()
            }
        }
    }

    private fun saveBitmap(bm: Bitmap, imageUrlName: String): String {
        val f = File(SAVEADDRESS, imageUrlName)
        try {
            val out = FileOutputStream(f)
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return SCHEMA + f.path
    }

    private fun getColorRGB(userId: String): String {
        val portraitColors = arrayOf("#e97ffb", "#00b8d4", "#82b2ff", "#f3db73", "#f0857c")
        if (TextUtils.isEmpty(userId)) {
            return portraitColors[0]
        }
        val i = getAscii(userId[0]) % 5

        return portraitColors[i]
    }


    private fun getAscii(cn: Char): Int {
        val bytes = cn.toString().toByteArray()
        return when {
            bytes.size == 1 -> //单字节字符
                bytes[0].toInt()
            bytes.size == 2 -> { //双字节字符
                val heightByte = 256 + bytes[0]
                val lowByte = 256 + bytes[1]
                256 * heightByte + lowByte - 256 * 256
            }
            else -> 0 //错误
        }
    }

    /**
     * 生成 view 的截图
     *
     * @param view
     * @return
     */
    fun takeScreenShot(view: View): Bitmap {
        return view.let {
            it.isDrawingCacheEnabled = true
            it.buildDrawingCache()
            it.drawingCache
        }
    }


    private val li_SecPosValue = intArrayOf(1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590)
    private val lc_FirstLetter = arrayOf("a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "w", "x", "y", "z")

    /**
     * 取得给定汉字串的首字母串,即声母串
     *
     * @param word 给定汉字串
     * @return 声母串
     */
    private fun getAllFirstLetter(word: String?): String {
        if (word == null || word.trim().isEmpty()) {
            return ""
        }

        var str = ""
        for (i in 0 until word.length) {
            str += getFirstLetter(word.substring(i, i + 1))
        }

        return str
    }

    /**
     * 取得给定汉字的首字母,即声母
     *
     * @param chinese 给定的汉字
     * @return 给定汉字的声母
     */
    private fun getFirstLetter(chinese: String?): String {
        if (chinese == null || chinese.trim().isEmpty()) {
            return ""
        }
        var newChinese = conversionStr(chinese, "GB2312", "ISO8859-1")

        if (newChinese.length > 1) {
            // 判断是不是汉字
            var liSectorCode = newChinese[0].toInt() // 汉字区码
            var liPositionCode = newChinese[1].toInt() // 汉字位码
            liSectorCode -= 160
            liPositionCode -= 160
            val liSecPosCode = liSectorCode * 100 + liPositionCode // 汉字区位码
            if (liSecPosCode in 1601..5589) {
                for (i in 0..22) {
                    if (liSecPosCode >= li_SecPosValue[i] && liSecPosCode < li_SecPosValue[i + 1]) {
                        newChinese = lc_FirstLetter[i]
                        break
                    }
                }
            } else {
                // 非汉字字符,如图形符号或ASCII码
                newChinese = conversionStr(newChinese, "ISO8859-1", "GB2312").substring(0, 1)
            }
        }

        return newChinese
    }

    /**
     * 字符串编码转换
     *
     * @param str           要转换编码的字符串
     * @param charsetName   原来的编码
     * @param toCharsetName 转换后的编码
     * @return 经过编码转换后的字符串
     */
    private fun conversionStr(str: String, charsetName: String, toCharsetName: String): String {

        return String(str.toByteArray(charset(charsetName)), charset(toCharsetName))
    }

    private fun generateRandomCharacter(): String {
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val length = alphabet.length
        val random = Random()
        return alphabet[random.nextInt(length)].toString()
    }

}