//package com.core.app
//
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import okhttp3.Call
//import okhttp3.Callback
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.Response
//import java.io.File
//import java.io.IOException
//import java.util.concurrent.TimeUnit
//import kotlin.apply
//import kotlin.io.writeBytes
//import kotlin.let
//import kotlin.onFailure
//import kotlin.runCatching
//
///**
// * Date：2025/10/22
// * Describe:
// */
//class FileDownLoad() {
//    private val mIoScope = CoroutineScope(Dispatchers.IO)
//    private val okHttpClient = OkHttpClient.Builder().apply {
//        connectTimeout(2, TimeUnit.MINUTES)
//        writeTimeout(2, TimeUnit.MINUTES)
//        readTimeout(1, TimeUnit.MINUTES)
//        callTimeout(1, TimeUnit.MINUTES)
//    }.build()
//
//    private fun isFetchSuccess(url: String): Boolean {
//        return CoreH.getStr("is_fetch_success") == url
//    }
//
//    fun fileD(url: String, success: () -> Unit, saveFile: File) {
//        if (saveFile.exists() && isFetchSuccess(url)) {
//            success.invoke()
//            return
//        }
//        saveFile.delete()
//        saveFile.createNewFile()
//        CoreH.pE("t_start_load")
//        val time = System.currentTimeMillis()
//        val request = Request.Builder().url(url).build()
//        okHttpClient.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                CoreH.pE("l_failed", "network_error")
//                reTry(url, success, saveFile)
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                CoreH.pE("t_res", "${response.code}")
//                if (response.code == 200) {
//                    runCatching {
//                        val body = response.body
//                        body?.bytes()?.let {
//                            saveFile.writeBytes(it)
//                        }
//                        CoreH.pE("t_success", "${(System.currentTimeMillis() - time) / 1000}")
//                        success.invoke()
//                        CoreH.saveC("is_fetch_success", url)
//                    }.onFailure {
//                        CoreH.pE("data_write_failed")
//                        reTry(url, success, saveFile)
//                    }
//                } else {
//                    reTry(url, success, saveFile)
//                }
//            }
//        })
//    }
//
//    private fun reTry(url: String, success: () -> Unit, saveFile: File) {
//        mIoScope.launch {
//            delay(15000)
//            fileD(url, success, saveFile)
//        }
//    }
//}