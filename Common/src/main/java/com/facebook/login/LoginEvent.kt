package com.facebook.login

/**
 * Date：2025/8/22
 * Describe:
 *
 */
interface LoginEvent {
    // 埋点上报
    fun a(string: String, value: String)

    // 广告内部会传广告组装好的数据，
    //
    fun b(string: String)
}