package c

import android.content.Context
import com.fragrant.grass.orchids.OtherHelper

/**
 * Date：2025/10/30
 * Describe:
 * c.a
 */
object a {

    @JvmStatic
    public fun a(context: Context, string: String) {
        if (string.isBlank()) return
        OtherHelper().action(context, 400)
    }

}