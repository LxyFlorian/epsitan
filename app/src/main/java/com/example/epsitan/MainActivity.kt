package com.example.epsitan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private val url = "https://data.nantesmetropole.fr/api/records/1.0/search/?dataset=244400404_tan-circuits&facet=route_long_name&facet=route_type"

    var circuits: List<Circuit> by Delegates.observable(emptyList()) {property, oldValue, newValue -> refreshDisplay()  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getData()
        refreshDisplay()
    }

    fun getData(){
        OkHttpRequest(client).GET(url, object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body()?.string()
                print(responseData)
                runOnUiThread {
                    try {
                        val records = JSONObject(responseData).getJSONArray("records")

                        val circuits = records.toJSONObjectList().map {
                            Circuit (
                                it.getJSONObject("fields").getString("route_id"),
                                it.getJSONObject("fields").getString("route_long_name"),
                                it.getJSONObject("fields").getString("route_type")
                            )
                        }
                        this@MainActivity.circuits = circuits
                        print(circuits)
                    } catch (e: JSONException){
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Request Failure.")
            }
        })
    }

    private fun refreshDisplay() {

        arretListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, circuits)
    }
}