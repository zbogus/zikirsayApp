package com.zahitbogus.zikirsayapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // AdMob başlatma
        MobileAds.initialize(this) {}

        setContent {
            ZikirSayApp()
        }
    }
}
