// Zikir.kt
package com.zahitbogus.zikirsayapp

data class Zikir(
    val id: String = java.util.UUID.randomUUID().toString(),  // Benzersiz ID otomatik olarak atanıyor
    val name: String,                                         // Zikir adı (zorunlu)
    val count: Int = 99,                                      // Zikir adeti (zorunlu ve varsayılan 99)
    val pronunciation: String?,                               // Zikir okunuşu (opsiyonel)
    val meaning: String?                                      // Zikir meali (opsiyonel)
)
