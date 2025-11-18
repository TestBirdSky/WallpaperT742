package com.sphara.slide.wallpaper.chowamr

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.sphara.slide.wallpaper.R
import com.sphara.slide.wallpaper.ui.theme.WallpaperTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.graphics.scale
import androidx.core.graphics.createBitmap
import com.sphara.slide.wallpaper.mazdkan.MazdKan.Companion.imageData
import com.sphara.slide.wallpaper.utils.BitDownload.downloadWallpaper
import com.sphara.slide.wallpaper.utils.BitDownload.downloadWallpaper2
import com.sphara.slide.wallpaper.utils.BitDownload.saveToDownloadsFolder

//Frame 保存
class ChoWamr : ComponentActivity() {


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val uri = intent.extras?.getString("uri") ?: ""
        val frame = intent.extras?.getInt("frame") ?: R.drawable.frame_1
        setContent {
            ChoWamrUI(uri, frame)
        }
    }

    @Preview
    @Composable
    fun ChoWamrUI(
        uri: String = "",
        frame: Int = R.drawable.frame_1
    ) {

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri.toUri())
                .build()
        )

        val context = LocalContext.current

        // 保存叠加图片的状态
        val isSaved = remember { mutableStateOf(false) }

        // 自动保存叠加图片
        LaunchedEffect(uri, frame) {
            if (uri.isNotEmpty() && !isSaved.value) {
                downloadWallpaper2(
                    this@ChoWamr, uri, frame
                ) { bitmap ->
                    callBack = {
                        saveToDownloadsFolder(this@ChoWamr, bitmap)
                    }
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                isSaved.value = true
            }
        }

        WallpaperTheme {
            Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->


                ConstraintLayout(
                    modifier = Modifier.Companion
                        .background(Color.Companion.Black)
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {

                    val (view1, view2, view3, view4, view5, view6) = createRefs()

                    Image(
                        painter = painterResource(R.drawable.ic_jbs), "",
                        modifier = Modifier.Companion
                            .constrainAs(view1) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                            .offset(x = (-85).dp, y = (-36).dp)
                    )

                    Box(modifier = Modifier.Companion.constrainAs(view2) {
                        top.linkTo(parent.top, 90.dp + innerPadding.calculateTopPadding())
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }, contentAlignment = Alignment.Companion.Center) {

                        Image(
                            painter = painter,
                            "",
                            modifier = Modifier.Companion
                                .width(250.dp)
                                .height(350.dp)
                        )
                        Image(
                            painter = painterResource(frame),
                            "",
                            modifier = Modifier.Companion
                                .width(300.dp)
                                .height(400.dp)
                        )

                    }

                    Box(
                        modifier = Modifier.Companion
                            .constrainAs(view3) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(view2.bottom, -55.dp)
                            }
                            .size(115.dp)
                            .background(
                                color = Color(0xFF494949),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Companion.Center)
                    {


                        Image(
                            painter = painterResource(R.drawable.ic_success),
                            "",
                            modifier = Modifier.Companion.size(60.dp)
                        )
                    }

                    Text(
                        "Save Successful!",
                        modifier = Modifier.Companion.constrainAs(view4) {
                            top.linkTo(view3.bottom, 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        color = Color.Companion.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(600)
                    )

                    Button(
                        onClick = {
                            (context as Activity).finish()
                        }, modifier = Modifier.Companion
                            .constrainAs(view5) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(view4.bottom, 46.dp)
                            }
                            .height(56.dp)
                            .width(189.dp)
                            .background(
                                color = Color(0xFFF97643),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97643))
                    ) {

                        Text(
                            "Back",
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