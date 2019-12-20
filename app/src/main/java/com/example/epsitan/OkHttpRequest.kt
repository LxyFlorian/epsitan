package com.example.epsitan

import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject

class OkHttpRequest(client: OkHttpClient) { // Small class to use okHttp quicker
    private var client = OkHttpClient()

    init {
        this.client = client
    }

    fun GET(url: String, callback: Callback): Call {
        val request = Request.Builder()
            .url(url)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    companion object {
        val JSON = MediaType.parse("application/json; charset=utf-8")
    }
}

fun JSONArray.toJSONObjectList(): List<JSONObject> { // Usage of extension
    var buffer = emptyList<JSONObject>()
    for(i in 0 until this.length()) {
        buffer += this.getJSONObject(i)
    }
    return buffer
}