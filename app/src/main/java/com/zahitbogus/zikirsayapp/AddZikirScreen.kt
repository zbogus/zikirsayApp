// AddZikirScreen.kt
package com.zahitbogus.zikirsayapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AddZikirScreen(navController: NavHostController, onZikirAdded: (Zikir) -> Unit) {
    var zikirName by remember { mutableStateOf("") }
    var zikirCount by remember { mutableStateOf("99") } // Varsayılan olarak "99" string değeri
    var zikirPronunciation by remember { mutableStateOf("") }
    var zikirMeaning by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var countError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Zikir Adı (Zorunlu)
        OutlinedTextField(
            value = zikirName,
            onValueChange = { zikirName = it },
            label = { Text("Zikir Adı") },
            isError = nameError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Zikir Adeti (Zorunlu, Default 99)
        OutlinedTextField(
            value = zikirCount,
            onValueChange = { value ->
                zikirCount = value // Değer string olarak alınıyor
                countError = zikirCount.toIntOrNull() == null || zikirCount.toIntOrNull()!! <= 0
            },
            label = { Text("Zikir Adeti") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = countError
        )

        if (countError) {
            Text(
                text = "Lütfen geçerli bir rakam giriniz",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Zikir Okunuşu (Opsiyonel)
        OutlinedTextField(
            value = zikirPronunciation,
            onValueChange = { zikirPronunciation = it },
            label = { Text("Zikir Okunuşu (Opsiyonel)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Zikir Meali (Opsiyonel)
        OutlinedTextField(
            value = zikirMeaning,
            onValueChange = { zikirMeaning = it },
            label = { Text("Zikir Meali (Opsiyonel)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Zikir Kaydet Butonu
        Button(onClick = {
            val zikirCountInt = zikirCount.toIntOrNull() ?: -1  // String değeri Int'e çeviriyoruz
            nameError = zikirName.isBlank()
            countError = zikirCountInt <= 0

            if (!nameError && !countError) {
                val newZikir = Zikir(
                    id = java.util.UUID.randomUUID().toString(),
                    name = zikirName,
                    count = zikirCountInt,  // Zikir adeti Int olarak kaydediliyor
                    pronunciation = if (zikirPronunciation.isBlank()) null else zikirPronunciation,
                    meaning = if (zikirMeaning.isBlank()) null else zikirMeaning
                )
                onZikirAdded(newZikir)
                navController.navigate("main_screen") // Ana ekrana geri dön
            }
        }) {
            Text("Zikri Kaydet")
        }

        if (nameError) {
            Text(text = "Zikir adı zorunludur!", color = MaterialTheme.colorScheme.error)
        }
    }
}
