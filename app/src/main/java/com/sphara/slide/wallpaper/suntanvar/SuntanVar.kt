package com.sphara.slide.wallpaper.suntanvar

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sphara.slide.wallpaper.R
import com.sphara.slide.wallpaper.cureppate.CurepPate
import com.sphara.slide.wallpaper.ui.theme.WallpaperTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//启动页
class SuntanVar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplashUI()
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                delay(2500)
                startActivity(Intent(this@SuntanVar, CurepPate::class.java))
                finish()
            }
        }
    }

    //    @Preview(showBackground = true)
    @Composable
    fun SplashUI() {
        WallpaperTheme {
            Scaffold(
                modifier = Modifier.Companion
                    .fillMaxSize()
            ) { innerPadding ->
                Column(
                    modifier = Modifier.Companion
                        .background(Color.Companion.Black)
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_wall_frame),
                        "",
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .padding(top = 50.dp, start = 16.dp, end = 8.dp),
                        contentScale = ContentScale.Companion.FillWidth
                    )
                    Text(
                        "Ape NFTs is the world’s first and largest NFT\nmarketplace",
                        modifier = Modifier.Companion
                            .align(Alignment.Companion.Start)
                            .padding(start = 16.dp, top = 8.dp),
                        color = Color(0xFF878787),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.Companion.weight(1f))
                    Box {
                        Image(painter = painterResource(R.drawable.ic_jxfs_xrkr), "")
                        LinearProgressIndicator(
                            modifier = Modifier.Companion
                                .align(Alignment.Companion.BottomCenter)
                                .padding(bottom = 50.dp),
                            color = Color(0xFFF97643)
                        )
                    }
                }
            }
        }
    }
}