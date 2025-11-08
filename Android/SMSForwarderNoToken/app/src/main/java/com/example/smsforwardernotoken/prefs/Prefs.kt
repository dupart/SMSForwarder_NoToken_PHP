
package com.example.smsforwardernotoken.prefs

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore("settings")
object PrefKeys {
    val NUMBERS = stringPreferencesKey("numbers") // comma separated
    val BACKEND = stringPreferencesKey("backend")
}
