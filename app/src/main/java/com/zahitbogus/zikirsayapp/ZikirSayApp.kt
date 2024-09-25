// ZikirSayApp.kt
package com.zahitbogus.zikirsayapp

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun ZikirSayApp(navController: NavHostController) {
    val context = LocalContext.current
    val zikirList = remember { mutableStateListOf<Zikir>() }

    // Uygulama başlarken kayıtlı verileri yükle
    LaunchedEffect(Unit) {
        zikirList.addAll(loadZikirData(context))
    }

    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(
                navController = navController,
                zikirList = zikirList,  // Bu parametre MainScreen'e geçiyor
                onZikirListUpdated = { updatedList ->
                    // Zikir listesi güncelleniyor ve shared preferences kaydediliyor
                    saveZikirData(context, updatedList)
                }
            )
        }
        composable("add_zikir_screen") {
            AddZikirScreen(navController = navController) { newZikir ->
                zikirList.add(newZikir)
                saveZikirData(context, zikirList)
            }
        }
        composable("zikir_screen/{zikirName}/{zikirCount}") { backStackEntry ->
            val zikirName = backStackEntry.arguments?.getString("zikirName") ?: ""
            val zikirCount = backStackEntry.arguments?.getString("zikirCount")?.toIntOrNull() ?: 99
            ZikirCountScreen(navController, zikirName, zikirCount.toString())
        }
    }
}
