package com.sphara.slide.wallpaper.mazdkan

import android.Manifest
import android.app.Activity
import android.app.WallpaperManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.sphara.slide.wallpaper.utils.FavSpUtils
import com.sphara.slide.wallpaper.data.ImageData
import com.sphara.slide.wallpaper.R
import com.sphara.slide.wallpaper.ui.theme.WallpaperTheme
import com.sphara.slide.wallpaper.utils.BitDownload.downloadWallpaper
import com.sphara.slide.wallpaper.utils.BitDownload.saveToDownloadsFolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

//壁纸设置页
class MazdKan : ComponentActivity() {

    private val _image = MutableStateFlow(imageData)
    private val image: StateFlow<ImageData> = _image


    companion object {
        var imageData = ImageData(R.drawable.paper_1, "paper_1")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MazdKanUI()
        }

    }

    //    @Preview()
    @Composable
    fun MazdKanUI() {

        val context = LocalContext.current
        val imageState = image.collectAsState()

        var trigger by remember { mutableIntStateOf(0) }
        var showApply by remember { mutableStateOf(false) }

        val currentTime by remember(trigger) {
            derivedStateOf {
                val now = LocalDateTime.now()
                DateTimeFormatter.ofPattern("HH:mm").format(now)
            }
        }
        val currentDate by remember(trigger) {
            derivedStateOf {
                val today = LocalDate.now()
                val formatter = DateTimeFormatter.ofPattern("MMMM d', 'yyyy", Locale.ENGLISH)
                today.format(formatter)
            }
        }

        LaunchedEffect(Unit) {
            // 只在第一次计算到下一分钟的精确时间
            val now = System.currentTimeMillis()
            val nextMinute = ((now / 60000) + 1) * 60000
            val initialDelay = nextMinute - now

            // 第一次延迟
            delay(initialDelay)
            trigger++

            // 后续固定每分钟触发一次
            while (true) {
                delay(60000)
                trigger++
            }
        }


        WallpaperTheme {
            Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
                Box(modifier = Modifier.Companion.fillMaxSize()) {
                    Image(
                        painter = painterResource(imageState.value.image),
                        "",
                        modifier = Modifier.Companion.fillMaxSize(),
                        contentScale = ContentScale.Companion.FillBounds
                    )

                    Row(
                        modifier = Modifier.Companion
                            .padding(innerPadding)
                            .fillMaxWidth()
                            .padding(vertical = 9.dp, horizontal = 16.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_return),
                            "",
                            modifier = Modifier.Companion.clickable {
                                (context as Activity).finish()
                            })
                        Spacer(modifier = Modifier.Companion.weight(1f))
                        Image(
                            painter = painterResource(
                                if (imageState.value.isFavorite) R.drawable.ic_fav_s else R.drawable.ic_fav_n
                            ),
                            "",
                            modifier = Modifier.Companion.clickable {
                                val bean = imageState.value
                                val fav = !bean.isFavorite
                                _image.update { it.copy(isFavorite = fav) }
                                val list = FavSpUtils.getList("FavList").toMutableList()
                                if (fav) {
                                    if (!list.contains(bean.uuid)) {
                                        list.add(bean.uuid)
                                        FavSpUtils.putList("FavList", list)
                                    }
                                } else {
                                    list.remove(bean.uuid)
                                    FavSpUtils.putList("FavList", list)
                                }
                            }

                        )
                    }

                    Column(
                        modifier = Modifier.Companion
                            .padding(top = 120.dp + innerPadding.calculateTopPadding())
                            .align(Alignment.Companion.TopCenter),
                        horizontalAlignment = Alignment.Companion.CenterHorizontally
                    ) {
                        Text(
                            currentTime,
                            color = Color.Companion.White,
                            fontSize = 60.sp,
                            fontWeight = FontWeight(600)
                        )
                        Text(
                            currentDate,
                            color = Color.Companion.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight(600),
                            modifier = Modifier.Companion.padding(top = 20.dp)
                        )
                    }

                    if (showApply) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    showApply = false
                                }
                        )

                        Column(
                            modifier = Modifier.Companion
                                .align(Alignment.Companion.BottomCenter)
                                .padding(bottom = 16.dp + innerPadding.calculateBottomPadding())
                                .padding(horizontal = 16.dp)
                        ) {

                            Column(
                                modifier = Modifier.Companion
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF19191E)),
                                verticalArrangement = Arrangement.spacedBy(0.dp)

                            ) {
                                val btnModifier = Modifier.Companion
                                    .fillMaxWidth()
                                    .height(52.dp)
                                val btnColor = ButtonDefaults.buttonColors(
                                    containerColor = Color.Companion.Transparent
                                )


                                Button(
                                    onClick = {
                                        setLockScreen(imageData.image)
                                        showApply = false
                                    },
                                    modifier = btnModifier,
                                    colors = btnColor,
                                    shape = RoundedCornerShape(
                                        topStart = 12.dp,
                                        topEnd = 12.dp
                                    ),
                                ) {
                                    Text(
                                        text = "Set as Lock Screen",
                                        color = Color.Companion.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight(600)
                                    )
                                }



                                Spacer(
                                    modifier = Modifier.Companion
                                        .fillMaxWidth()
                                        .height(0.5.dp)
                                        .background(Color(0x24FFFFFF))
                                )

                                Button(
                                    onClick = {
                                        setHomeScreen(imageData.image)
                                        showApply = false
                                    },
                                    modifier = btnModifier,
                                    colors = btnColor,
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                        0.dp
                                    ),
                                ) {
                                    Text(
                                        text = "Set as Home Screen",
                                        color = Color.Companion.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight(600)
                                    )
                                }


                                Spacer(
                                    modifier = Modifier.Companion
                                        .fillMaxWidth()
                                        .height(0.5.dp)
                                        .background(Color(0x24FFFFFF))
                                )


                                Button(
                                    onClick = {
                                        setLockScreen(imageData.image)
                                        setHomeScreen(imageData.image)
                                        showApply = false
                                    },
                                    modifier = btnModifier,
                                    colors = btnColor,
                                    shape = RoundedCornerShape(
                                        bottomStart = 12.dp,
                                        bottomEnd = 12.dp
                                    ),
                                ) {
                                    Text(
                                        text = "Set Both",
                                        color = Color.Companion.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight(600)
                                    )
                                }


                            }


                            Button(
                                onClick = {
                                    showApply = false
                                },
                                modifier = Modifier.Companion
                                    .height(55.dp)
                                    .fillMaxWidth(),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF19191E
                                    )
                                )
                            ) {

                                Text(
                                    "Cancel",
                                    color = Color(0xFF878787),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight(600)
                                )
                            }


                        }

                    } else {
                        Row(
                            modifier = Modifier.Companion
                                .fillMaxWidth()
                                .align(Alignment.Companion.BottomCenter)
                                .padding(bottom = 20.dp + innerPadding.calculateBottomPadding())
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    downloadWallpaper(
                                        this@MazdKan,
                                        imageData.image,
                                    ) { bitmap ->
                                        callBack = {
                                            saveToDownloadsFolder(this@MazdKan, bitmap)
                                        }
                                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    }
//
                                },
                                modifier = Modifier.Companion
                                    .weight(1f)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF97643)
                                ),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_download),
                                    "",
                                    modifier = Modifier.Companion.size(20.dp)
                                )
                                Spacer(Modifier.Companion.width(8.dp))
                                Text(
                                    "Download",
                                    color = Color.Companion.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight(600)
                                )
                            }

                            Button(
                                onClick = {
                                    showApply = true
                                },
                                modifier = Modifier.Companion
                                    .weight(1f)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2A2B2F)
                                ),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_apply),
                                    "",
                                    modifier = Modifier.Companion.size(20.dp)
                                )
                                Spacer(Modifier.Companion.width(8.dp))
                                Text(
                                    "Apply",
                                    color = Color.Companion.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight(600)
                                )
                            }
                        }
                    }

                }
            }
        }
    }


    fun setLockScreen(res: Int) = lifecycleScope.launch {
        try {
            val wallpaperManager = WallpaperManager.getInstance(applicationContext);
            val int = wallpaperManager.setResource(res, WallpaperManager.FLAG_LOCK);
            if (int > 0) {
                Toast.makeText(this@MazdKan, "Set as Lock Screen Success!", Toast.LENGTH_SHORT)
                    .show()
            } else {
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this@MazdKan, "Set as Lock Screen Failed!", Toast.LENGTH_SHORT).show()

        }
    }

    fun setHomeScreen(res: Int) = lifecycleScope.launch {
        try {
            val wallpaperManager = WallpaperManager.getInstance(applicationContext);
            val int = wallpaperManager.setResource(res, WallpaperManager.FLAG_SYSTEM)
            if (int > 0) {
                Toast.makeText(this@MazdKan, "Set as Home Screen Success!", Toast.LENGTH_SHORT)
                    .show()
            } else {
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this@MazdKan, "Set as Home Screen Failed!", Toast.LENGTH_SHORT).show()

        }
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                lifecycleScope.launch {
                    callBack.invoke()
                }
            }
        }
    private var callBack: suspend () -> Unit = {}


}