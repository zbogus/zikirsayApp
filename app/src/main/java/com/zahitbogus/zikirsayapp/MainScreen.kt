package com.zahitbogus.zikirsayapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.snapshots.SnapshotStateList

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    zikirList: SnapshotStateList<Zikir>,  // zikirList dışarıdan alınır
    onZikirListUpdated: (List<Zikir>) -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Zikir Listesi") },
                actions = {
                    IconButton(onClick = { navController.navigate("add_zikir_screen") }) {
                        Icon(Icons.Default.Add, contentDescription = "Zikir Ekle")
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp)
        ) {
            if (zikirList.isNotEmpty()) {
                zikirList.forEachIndexed { index, zikir ->
                    ZikirCard(
                        zikir = zikir,
                        onClick = {
                            // Zikir sayma ekranına git
                            navController.navigate("zikir_screen/${zikir.name}/${zikir.count}")
                        },
                        onDelete = {
                            // Silme işlemi butona tıklandığında yapılır
                            coroutineScope.launch {
                                withContext(Dispatchers.Default) {
                                    zikirList.removeAt(index) // Zikir'i listeden kaldırıyoruz
                                }
                                // UI'yi güncellemek için state'i yeniliyoruz
                                withContext(Dispatchers.Main) {
                                    onZikirListUpdated(zikirList)
                                }
                            }
                        }
                    )
                }
            } else {
                Text(text = "Zikir listesi boş", style = MaterialTheme.typography.body1)
            }
        }
    }
}
