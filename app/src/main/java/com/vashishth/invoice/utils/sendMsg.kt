package com.vashishth.invoice.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity


fun sendSMS(phoneNumber:String,msg:String,context: Context){
    var smsManager = context.getSystemService(SmsManager::class.java)
    val parts = smsManager.divideMessage(msg)
    smsManager.sendMultipartTextMessage(phoneNumber,null,parts,null,null)

    openWhatsApp(context,msg,phoneNumber)
    Toast.makeText(context, "SMS Sent Successfully", Toast.LENGTH_SHORT).show()
//    try {
//        // Check if whatsapp is installed
//        context?.packageManager?.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/+91$phoneNumber"))
//        startActivity(context,intent,null)
//    } catch (e: PackageManager.NameNotFoundException) {
//        Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT).show()
//    }
}

fun openWhatsApp(context:Context,msg: String,phoneNumber: String) {
    try {
        val text = msg // Replace with your message.
        val toNumber ="91$phoneNumber"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("http://api.whatsapp.com/send?phone=$toNumber&text=$text")
        startActivity(context,intent,null)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}



