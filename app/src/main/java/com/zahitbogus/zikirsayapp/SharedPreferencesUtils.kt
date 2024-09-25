// SharedPreferencesUtils.kt
package com.zahitbogus.zikirsayapp

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.util.Log

private const val PREFERENCES_NAME = "zikir_prefs"
private const val KEY_ZIKIR_LIST = "zikir_list"
private val gson = Gson() // Gson nesnesini global yaparak tekrar tekrar oluşturmayı engelliyoruz

// Zikir verilerini kaydetmek için fonksiyon
fun saveZikirData(context: Context, zikirList: List<Zikir>): Boolean {
    return try {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val json = gson.toJson(zikirList)
        editor.putString(KEY_ZIKIR_LIST, json)
        editor.apply()  // Değişiklikleri kaydet
        true  // İşlem başarılı
    } catch (e: Exception) {
        Log.e("saveZikirData", "Zikir verileri kaydedilemedi: ${e.message}")
        false  // İşlem başarısız
    }
}

// Zikir verilerini yüklemek için fonksiyon
fun loadZikirData(context: Context): List<Zikir> {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    val json = sharedPreferences.getString(KEY_ZIKIR_LIST, null)

    return if (json != null) {
        try {
            val type = object : TypeToken<List<Zikir>>() {}.type
            gson.fromJson<List<Zikir>>(json, type)
        } catch (e: Exception) {
            Log.e("loadZikirData", "Zikir verileri yüklenirken hata oluştu: ${e.message}")
            emptyList() // Hata durumunda boş liste döndür
        }
    } else {
        // Eğer kayıtlı veri yoksa varsayılan zikir listesini döndür
        listOf(
            Zikir(id = "1", name = "Subhanallah", count = 99, pronunciation = null, meaning = null),
            Zikir(id = "2", name = "Elhamdulillah", count = 99, pronunciation = null, meaning = null),
            Zikir(id = "3", name = "Allahuekber", count = 99, pronunciation = null, meaning = null)
        )
    }
}
