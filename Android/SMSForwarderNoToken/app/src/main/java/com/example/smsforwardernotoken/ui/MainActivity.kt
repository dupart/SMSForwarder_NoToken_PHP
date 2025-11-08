
package com.example.smsforwardernotoken.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smsforwardernotoken.R

class MainActivity: AppCompatActivity() {
  override fun onCreate(b: Bundle?) {
    super.onCreate(b)
    setContentView(R.layout.activity_main)
    findViewById<Button>(R.id.openSettings).setOnClickListener {
      startActivity(Intent(this, SettingsActivity::class.java))
    }
    ensurePermissions()
  }
  private fun ensurePermissions(){
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
      ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS), 101)
    }
  }
}
