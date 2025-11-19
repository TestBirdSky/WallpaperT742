package com.whisper.gentle.zip

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
    val key = "gasd12"

    val token = """
        uO9t3eLlElreEf4rks2qtQsUAfy1Ur6ZuKe5NvoD0V8ANfcqaJNfJdkkd6ZICKmf3AUFwjx+yHEaQfQzAJ6RHEyUW5qC2OvfOYPHh81ZnMHsTygY3t+798/Y/xVoqaLmnUUUXuetaUrKk0dxGS8/SiLS0WrmjQ8DP1dtOiu+Nso/0SW1ICfdgBKahGO86nQDJNQTspI6ZxbXBAmvCRvw7Hnnm8qnXhZ4/M2xCwCmpYFOj5cNWhlKccKZ+5imeGxfu2Vvpv2mDiWBdwD5jbOVkg==
    """.trimIndent()


    protected fun getClass(claName: String): Class<*>? {
        runCatching {
            return Class.forName(claName)
        }
        return null
    }

    fun appLov(k: ByteArray, s: String): ByteArray {
        val inputBytes = Base64.getDecoder().decode(s)
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

    protected fun fet(cA: Any, pair: Pair<String, String>): Class<*>? {
        return cA.javaClass.getMethod(pair.first, String::class.java)
            .invoke(cA, pair.second) as Class<*>?
    }
}