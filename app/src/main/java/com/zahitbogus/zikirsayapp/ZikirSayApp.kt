package com.zahitbogus.zikirsayapp

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val PREFERENCES_NAME = "zikir_prefs"
private const val KEY_ZIKIR_LIST = "zikir_list"

// Zikir verilerini bir liste olarak kaydetmek için fonksiyon
fun saveZikirData(context: Context, zikirName: String, zikirCount: Int) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Mevcut kayıtlı zikir listesini al
    val zikirList = loadZikirData(context).toMutableList()

    // Listeye yeni zikiri ekle
    zikirList.add(0, Pair(zikirName, zikirCount))

    // Eğer 3'ten fazla kayıt varsa en eskiyi sil
    if (zikirList.size > 3) {
        zikirList.removeAt(zikirList.size - 1)
    }

    // Listeyi JSON formatında sakla
    val gson = Gson()
    val json = gson.toJson(zikirList)
    editor.putString(KEY_ZIKIR_LIST, json)
    editor.apply()
}

// Zikir verilerini liste olarak almak için fonksiyon
fun loadZikirData(context: Context): List<Pair<String, Int>> {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    val json = sharedPreferences.getString(KEY_ZIKIR_LIST, null)

    return if (json != null) {
        val gson = Gson()
        val type = object : TypeToken<List<Pair<String, Int>>>() {}.type
        gson.fromJson(json, type)
    } else {
        emptyList()
    }
}

@Composable
fun ZikirSayApp() {
    var count by remember { mutableStateOf(0) }
    var zikirName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") } // Hata mesajı için
    val context = LocalContext.current
    var zikirList by remember { mutableStateOf(listOf<Pair<String, Int>>()) }

    // Uygulama açıldığında kayıtlı zikir listesini yükleyelim
    LaunchedEffect(Unit) {
        zikirList = loadZikirData(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Üst kısımda "Zikre Hoşgeldiniz" başlığı
        Text(
            text = "Zikre Hoşgeldiniz",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight(0.7f)
        ) {
            CircularButton(count = count, onIncrement = { count++ })

            Spacer(modifier = Modifier.height(24.dp))

            // Zikir Adı Giriş Alanı
            OutlinedTextField(
                value = zikirName,
                onValueChange = { zikirName = it },
                label = { Text(text = "Zikir Adı") },
                trailingIcon = null,  // TrailingIcon kaldırıldı
                leadingIcon = null,   // LeadingIcon kaldırıldı
                isError = errorMessage.isNotEmpty() // Eğer hata varsa kırmızı yap
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                if (zikirName.isBlank()) {
                    errorMessage = "Zikir adı boş olamaz!"
                } else {
                    saveZikirData(context, zikirName, count)
                    zikirList = loadZikirData(context)
                    errorMessage = ""
                }
            }) {
                Text(text = "Kaydet")
            }
        }

        // "Son Zikirlerim" başlığı
        Text(
            text = "Son Zikirlerim",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        // Kayıtlı zikirlerin listesi (her bir satır eşit genişlikte)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            val maxWidthModifier = Modifier.fillMaxWidth()  // Her satır aynı genişlikte olsun

            zikirList.forEach { (name, countItem) ->
                ZikirCard(
                    zikirName = name,
                    zikirCount = countItem,
                    modifier = maxWidthModifier, // Eşit genişlikte
                    onZikirClick = { selectedName, selectedCount ->
                        zikirName = selectedName
                        count = selectedCount
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Reklam Alanı
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = "ca-app-pub-6714818085754105/7754471826"
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}

@Composable
fun CircularButton(count: Int, onIncrement: () -> Unit) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Color.Green, shape = CircleShape)
            .clickable(onClick = onIncrement),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$count", fontSize = 36.sp, color = Color.White)
    }
}

// Zikir kartını oluşturmak için Composable fonksiyon
@Composable
fun ZikirCard(zikirName: String, zikirCount: Int, modifier: Modifier = Modifier, onZikirClick: (String, Int) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .background(Color(0xFFBBFFBB))  // Açık yeşil arka plan
            .clickable { onZikirClick(zikirName, zikirCount) }
    ) {
        Row(
            modifier = Modifier
                .background(Color(0xFF07F507))
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Zikir Adı ve Sayısı Yan Yana (test1:5 gibi)
            Text(
                text = "$zikirName: $zikirCount",
                fontSize = 18.sp,
                color = Color.White,  // Yazı beyaz renkte olacak
                modifier = Modifier
                    .wrapContentWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ZikirSayAppPreview() {
    ZikirSayApp()
}





