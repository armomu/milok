package com.milok.app.utils

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ToastUtils {
    fun show(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
}

object DateUtils {
    private const val DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss"

    fun formatTimestamp(
        timestamp: Long,
        pattern: String = DEFAULT_FORMAT
    ): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun now(pattern: String = DEFAULT_FORMAT): String {
        return formatTimestamp(System.currentTimeMillis(), pattern)
    }
}

object StringUtils {
    fun String?.orEmpty(default: String = ""): String = if (isNullOrBlank()) default else this!!

    fun String.truncate(maxLength: Int, suffix: String = "..."): String {
        return if (length > maxLength) take(maxLength) + suffix else this
    }

    fun String.isEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun String.isPhoneNumber(): Boolean {
        return matches(Regex("^1[3-9]\\d{9}$"))
    }
}

object NumberUtils {
    fun formatCount(count: Int): String = when {
        count >= 100_000_000 -> "${count / 100_000_000}亿"
        count >= 10_000 -> "${count / 10_000}万"
        else -> count.toString()
    }

    fun Int.clamp(min: Int, max: Int): Int = maxOf(min, minOf(max, this))
}

object NetworkUtils {
    fun isNetworkError(throwable: Throwable?): Boolean {
        return throwable is java.net.UnknownHostException ||
                throwable is java.net.SocketTimeoutException ||
                throwable is java.io.IOException
    }
}
