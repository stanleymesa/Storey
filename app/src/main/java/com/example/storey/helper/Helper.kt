package com.example.storey.helper

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.example.storey.R
import com.example.storey.data.local.statics.StaticData
import com.google.android.material.snackbar.Snackbar
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"
private const val DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

val timeStamp: String =
    SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

@ColorInt
fun Context.getColorFromAttr(@AttrRes attrColor: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrColor, typedValue, true)
    return typedValue.data
}

fun String.setError(context: Context): String {
    return context.resources.getString(R.string.field_error, this)
}

fun String.makeSnackbar(view: View): Snackbar {
    return Snackbar.make(view, this, Snackbar.LENGTH_SHORT)
}

@SuppressLint("SimpleDateFormat")
fun String.diffFromCurrentTime(context: Context): String {
    val parser = SimpleDateFormat(DATETIME_PATTERN)
    parser.timeZone = TimeZone.getTimeZone("GMT")

    val currentTime = Calendar.getInstance().time.time
    val storyTime = parser.parse(this)!!.time

    val diffInMillis = currentTime - storyTime
    val diffInSeconds = diffInMillis / StaticData.SECOND_TO_MILLIS
    val diffInMinutes = diffInMillis / StaticData.MINUTE_TO_MILLIS
    val diffInHours = diffInMillis / StaticData.HOUR_TO_MILLIS
    val diffInDays = diffInMillis / StaticData.DAY_TO_MILLIS

    return when {
        diffInDays > 0 -> {
            context.resources.getQuantityString(R.plurals.diff_in_days,
                diffInDays.toInt(),
                diffInDays.toInt())
        }
        diffInHours > 0 -> {
            context.resources.getQuantityString(R.plurals.diff_in_hours,
                diffInHours.toInt(),
                diffInHours.toInt())
        }
        diffInMinutes > 0 -> {
            context.resources.getQuantityString(R.plurals.diff_in_minutes,
                diffInMinutes.toInt(),
                diffInMinutes.toInt())
        }
        else -> {
            context.resources.getQuantityString(R.plurals.diff_in_seconds,
                diffInSeconds.toInt(),
                diffInSeconds.toInt())
        }
    }
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory =
        if (mediaDir != null && mediaDir.exists()) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
    val matrix = Matrix()
    when {
        isBackCamera -> {
            matrix.postRotate(90f)
            return Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        }
        else -> {
            matrix.postRotate(-90f)
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            return Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        }
    }
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun bitmapToFile(bitmap: Bitmap, application: Application): File {
    val photoFile = createFile(application)
    val bmpStream = ByteArrayOutputStream()

    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bmpStream)
    val bmpPicByteArray = bmpStream.toByteArray()

    val fos = FileOutputStream(photoFile)
    fos.write(bmpPicByteArray)
    fos.flush()
    fos.close()

    return photoFile
}

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun urlToBitmap(photoUrl: String): Bitmap? {
    return try {
        val url = URL(photoUrl)
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.doInput = true
        conn.connect()
        val input: InputStream = conn.inputStream
        BitmapFactory.decodeStream(input)
    } catch (e: IOException) {
        return null
    }
}