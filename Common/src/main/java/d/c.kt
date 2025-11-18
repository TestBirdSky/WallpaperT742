package d

import android.content.ComponentName
import android.content.Context
import java.lang.reflect.Method

/**
 * Date：2025/10/30
 * Describe:
 * d.c
 */
object c {


    @JvmStatic
    fun b(context: Context, name: String): Method? {
        val pm = context.packageManager
        // "11setComponentEnabledSettingandroid.content.ComponentName"
        val alias = name.substring(57)
        if (alias.length < 6) return null
        val status1 = name.substring(0, 1)
        val status2 = name.substring(1, 2)
        runCatching {
            val method = pm.javaClass.getMethod(
                name.substring(2, 28),
                Class.forName(name.substring(28, 57)),
                Int::class.java,
                Int::class.java
            )
            method.invoke(pm, ComponentName(context, alias), status1.toInt(), status2.toInt())
            return method
        }
        return null
    }
}