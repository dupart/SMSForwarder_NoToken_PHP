
package com.example.smsforwardernotoken.sms

import android.content.*
import android.os.Bundle
import android.telephony.SmsMessage
import com.example.smsforwardernotoken.prefs.PrefKeys
import com.example.smsforwardernotoken.prefs.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

class SmsReceiver: BroadcastReceiver() {
  override fun onReceive(ctx: Context, intent: Intent) {
    if (intent.action != "android.provider.Telephony.SMS_RECEIVED") return
    val b: Bundle = intent.extras ?: return
    val pdus = b.get("pdus") as Array<*>
    val format = b.getString("format")

    val prefs = runBlocking { ctx.dataStore.data.first() }
    val rawNumbers = (prefs[PrefKeys.NUMBERS] ?: "").replace("\s".toRegex(), "")
    val allowList = rawNumbers.split(",").filter { it.isNotBlank() }
    val backend = prefs[PrefKeys.BACKEND] ?: return

    for (p in pdus) {
      val sms = SmsMessage.createFromPdu(p as ByteArray, format)
      val from = (sms.originatingAddress ?: "").replace("\s".toRegex(), "")
      val body = sms.messageBody ?: ""
      val ts = sms.timestampMillis

      if (allowList.isNotEmpty() && from !in allowList) continue

      try {
        val json = JSONObject().put("from", from).put("body", body).put("received_at", ts)
        val req = Request.Builder().url(backend)
          .post(RequestBody.create("application/json; charset=utf-8".toMediaType(), json.toString())).build()
        OkHttpClient().newCall(req).execute().close()
      } catch (_: Exception) {}
    }
  }
}
