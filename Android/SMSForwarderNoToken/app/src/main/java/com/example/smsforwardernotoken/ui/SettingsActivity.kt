
package com.example.smsforwardernotoken.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smsforwardernotoken.R
import com.example.smsforwardernotoken.prefs.PrefKeys
import com.example.smsforwardernotoken.prefs.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsActivity: AppCompatActivity() {
  override fun onCreate(b: Bundle?) {
    super.onCreate(b)
    setContentView(R.layout.activity_settings)

    val inputNumbers = findViewById<EditText>(R.id.inputNumbers)
    val inputBackend = findViewById<EditText>(R.id.inputBackend)

    lifecycleScope.launch {
      val prefs = dataStore.data.first()
      inputNumbers.setText(prefs[PrefKeys.NUMBERS] ?: "")
      inputBackend.setText(prefs[PrefKeys.BACKEND] ?: "")
    }

    findViewById<Button>(R.id.btnSave).setOnClickListener {
      lifecycleScope.launch {
        dataStore.edit {
          it[PrefKeys.NUMBERS] = inputNumbers.text.toString()
          it[PrefKeys.BACKEND] = inputBackend.text.toString()
        }
        Toast.makeText(this@SettingsActivity, "Enregistré", Toast.LENGTH_SHORT).show()
        finish()
      }
    }
  }
}
