package com.vashishth.invoice.utils

import android.content.Context
import com.vashishth.invoice.model.hsnCodeItem
import com.vashishth.invoice.model.unit
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

fun unitsLoad(context: Context,unitsList : ArrayList<unit>) {
    if (unitsList.isEmpty()) {
        try {
            val obj = JSONObject(getJSONFromAssets(context, "units_list.json")!!)
            val UnitsArray = obj.getJSONArray("UNITS")

            for (i in 0 until UnitsArray.length()) {
                val Unit1 = UnitsArray.getString(i)
                unitsList.add(unit(Unit1))
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

fun hsnLoad(context: Context,hsnList : ArrayList<hsnCodeItem>) {
    if (hsnList.isEmpty()) {
        try {

            val obj = JSONObject(getJSONFromAssets(context, "hsn.json")!!)
            val HsnArray = obj.getJSONArray("HSN")

            for (i in 0 until HsnArray.length()) {
                val HSNCODE1 = HsnArray.getJSONObject(i)
                val hsncode = HSNCODE1.getString("HSN Code")
                val hsnDescription = HSNCODE1.getString("HSN Description")
                val hsnDetails =
                    hsnCodeItem(hsncode, hsnDescription)
                hsnList.add(hsnDetails)
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

private fun getJSONFromAssets(context: Context, fileName: String): String? {
    var json: String? = null
    val charset: Charset = Charsets.UTF_8
    try {
        val myUsersJSONFile = context.assets.open(fileName)
        val size = myUsersJSONFile.available()
        val buffer = ByteArray(size)
        myUsersJSONFile.read(buffer)
        myUsersJSONFile.close()
        json = String(buffer, charset)
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }
    return json
}