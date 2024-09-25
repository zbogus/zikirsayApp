package com.zahitbogus.zikirsayapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ZikirCountScreen(navController: NavHostController, zikirName: String, zikirCount: String) {
    var count by remember { mutableStateOf(zikirCount.toInt()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "$zikirName", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(24.dp))

        CircularButton(count = count, onClick = {
            if (count > 0) {
                count--
            }
        })

        Spacer(modifier = Modifier.height(24.dp))

        if (count == 0) {
            Text(text = "Zikir tamamlandı!", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun CircularButton(count: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Color.Green, shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$count", fontSize = 36.sp, color = Color.White)
    }
}
