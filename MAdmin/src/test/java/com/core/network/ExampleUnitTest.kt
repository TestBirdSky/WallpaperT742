package com.core.network

import org.junit.Test

import java.io.File
import java.io.FileOutputStream
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val pathBASE = "/Users/jxx/AndroidStudioProjects/T742/WallpaperT742/MAdmin/"

    private val s = """
  {
        "str1": "dalvik.system.InMemoryDexClassLoader",
        "str2": "java.lang.ClassLoader",
        "str3": "getClassLoader",
        "str4": "com.core.app.CoreH",
        "str5": "loadClass"
    }
    """.trimIndent()

    @Test
    fun test2() {
        val encrypted = encrypt(s.toByteArray())
        val str = Base64.getEncoder().encodeToString(encrypted)
        println("str=$str -${str.length}")
        val result = decryptDex(DEX_AES_KEY, str)
        println("result${String(result)}")
    }

    @Test
    fun addition_dex() {
        val sourceFilePath = "${pathBASE}makejar/dex/classes.dex" // 源文件路径，可按需修改
        val outputFolderPath = "${pathBASE}output" // 目标文件路径，可按需修改
        val sourceFile = File(sourceFilePath)
        val outputFolder = File(outputFolderPath)
        if (!outputFolder.exists()) {
            outputFolder.mkdirs()
        }

        val local1 = File("$outputFolderPath/local.txt")
        val file3 = File("$outputFolderPath/origin.txt")
        val string = dexToAesText(sourceFile)

        local1.writeText(string)

        println("文件重写并保存成功")

        // 验证
        file3.writeText(string)
        // aes+iv 加密
        val restoredDex = File(outputFolderPath, "dexMy2.dex")
        val dexBytes = decryptDex(DEX_AES_KEY, file3.readText())
        FileOutputStream(restoredDex).use { it.write(dexBytes) }
    }

    private val ALGORITHM = "AES"
    private val DEX_AES_KEY = "345nuiizkjgasd12".toByteArray() // 16, 24, or 32 bytes


    // DEX -> AES加密文本
    fun dexToAesText(dexFile: File): String {
        val dexBytes = dexFile.readBytes()
        val encrypted = encrypt(dexBytes)
        return Base64.getEncoder().encodeToString(encrypted)
    }


    // 加密
    fun encrypt(inputBytes: ByteArray): ByteArray {
        val key = SecretKeySpec(DEX_AES_KEY, ALGORITHM)
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val outputBytes = cipher.doFinal(inputBytes)
        return outputBytes
    }

    // 解密
    private fun decryptDex(keyAes: ByteArray, inStr: String): ByteArray {
        val inputBytes = Base64.getDecoder().decode(inStr)
        val key = SecretKeySpec(keyAes, ALGORITHM)
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key)
        val outputBytes = cipher.doFinal(inputBytes)
        return outputBytes
    }

}