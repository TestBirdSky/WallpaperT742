package com.sphara.slide.wallpaper.patycate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.sphara.slide.wallpaper.R
import com.sphara.slide.wallpaper.chowamr.ChoWamr
import com.sphara.slide.wallpaper.data.ImageFrame
import com.sphara.slide.wallpaper.ui.theme.WallpaperTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

//Frame 壁纸
class PatyCate : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val uri = intent.extras?.getString("uri") ?: ""
        setContent {
            PatyCateUI(uri)
        }
    }

    private val _frameList = MutableStateFlow(
        listOf(
            ImageFrame(R.drawable.ic_none, "None"),
            ImageFrame(R.drawable.frame_1, "Frame 1"),
            ImageFrame(R.drawable.frame_2, "Frame 2"),
            ImageFrame(R.drawable.frame_3, "Frame 3"),
            ImageFrame(R.drawable.frame_4, "Frame 4"),
            ImageFrame(R.drawable.frame_5, "Frame 5"),
            ImageFrame(R.drawable.frame_6, "Frame 6"),
            ImageFrame(R.drawable.frame_7, "Frame 7"),
            ImageFrame(R.drawable.frame_8, "Frame 8"),
            ImageFrame(R.drawable.frame_9, "Frame 9"),
            ImageFrame(R.drawable.frame_10, "Frame 10"),
            ImageFrame(R.drawable.frame_11, "Frame 11"),
            ImageFrame(R.drawable.frame_12, "Frame 12"),
            ImageFrame(R.drawable.frame_13, "Frame 13"),
            ImageFrame(R.drawable.frame_14, "Frame 14"),
            ImageFrame(R.drawable.frame_15, "Frame 15"),
            ImageFrame(R.drawable.frame_16, "Frame 16"),
        )

    )
    private val frameList = _frameList


    @Preview
    @Composable
    fun PatyCateUI(uri: String = "") {

        val context = LocalContext.current
        val frameState by frameList.collectAsState()
        val selectedFrame = remember(frameState) {
            frameState.find { it.isSelect } ?: ImageFrame(
                R.drawable.ic_none,
                "None"
            )
        }
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri.toUri())
                .build()
        )


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
                            .offset(x = (-27).dp, y = (-36).dp)
                    )

                    Image(
                        painter = painterResource(R.drawable.ic_return),
                        "",
                        modifier = Modifier.Companion
                            .constrainAs(view2) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start, 16.dp)
                            }
                            .padding(vertical = 9.dp)
                            .clickable {
                                (context as Activity).finish()
                            },
                    )

                    Text(
                        "Frame Craft",
                        color = Color.Companion.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier.Companion.constrainAs(view3) {
                            top.linkTo(view2.top)
                            bottom.linkTo(view2.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })

                    Column(
                        modifier =
                            Modifier.Companion
                                .fillMaxWidth()
                                .constrainAs(view4) {
                                    top.linkTo(view2.bottom)
                                    bottom.linkTo(parent.bottom)
                                    height = Dimension.Companion.fillToConstraints
                                },
                        horizontalAlignment = Alignment.Companion.CenterHorizontally
                    ) {

                        if (selectedFrame.frame == R.drawable.ic_none) {
                            Box(
                                modifier = Modifier.Companion
                                    .padding(horizontal = 16.dp)
                                    .padding(
                                        top = 16.dp,
                                        bottom = 8.dp
                                    )
                                    .height(68.dp)
                            ) {

                                Image(
                                    painter = painterResource(R.drawable.bg_naozhong),
                                    "",
                                    modifier = Modifier.Companion.fillMaxSize(),
                                    contentScale = ContentScale.Companion.FillBounds
                                )

//                                Row(
//                                    verticalAlignment = Alignment.Companion.CenterVertically,
//                                    modifier = Modifier.Companion
//                                        .fillMaxSize()
//                                        .clickable {
//                                            context.startActivity(
//                                                Intent(
//                                                    context,
//                                                    PapilMos::class.java
//                                                )
//                                            )
//                                        }
//                                ) {
//
//                                    Image(
//                                        painter = painterResource(R.drawable.ic_naozhong),
//                                        "", modifier = Modifier.Companion.padding(start = 12.dp)
//                                    )
//
//                                    Text(
//                                        "Scheduled Change",
//                                        color = Color.Companion.White,
//                                        modifier = Modifier.Companion.padding(start = 12.dp)
//                                    )
//
//                                }
                            }
                        }

                        Box(
                            modifier = Modifier.Companion
                                .weight(1f)
                                .wrapContentWidth()
                                .verticalScroll(rememberScrollState()),
                            contentAlignment = Alignment.Companion.Center
                        ) {


                            Image(
                                painter = painter,
                                "",
                                modifier = Modifier.Companion
                                    .width(250.dp)
                                    .height(350.dp)
                                    .padding(bottom = 20.dp),
                                contentScale = ContentScale.Companion.Crop
                            )


                            if (selectedFrame.frame != R.drawable.ic_none) {
                                Image(
                                    painter = painterResource(selectedFrame.frame),
                                    "",
                                    modifier = Modifier.Companion
                                        .width(300.dp)
                                        .height(400.dp)
                                        .padding(bottom = 20.dp),
                                    contentScale = ContentScale.Companion.Fit
                                )
                            }
                        }


                        LazyRow(
                            modifier = Modifier.Companion
                                .padding(horizontal = 16.dp),
                            horizontalArrangement =
                                Arrangement.spacedBy(16.dp)
                        ) {
                            items(frameState, key = { it.title }) { item ->
                                Column(
                                    modifier = Modifier.Companion
                                        .width(72.dp)
                                        .clickable {
                                            val temp = frameState.map { bean ->
                                                bean.copy(isSelect = bean.title == item.title)
                                            }.toMutableList()

                                            _frameList.update { temp }
                                        },
                                    horizontalAlignment = Alignment.Companion.CenterHorizontally,
                                ) {
                                    Box(
                                        modifier = Modifier.Companion
                                            .size(72.dp)
                                            .background(
                                                color = Color(0xFF2A2B2F),
                                                shape = RoundedCornerShape(6.dp)
                                            )
                                            .then(
                                                if (item.isSelect) {
                                                    Modifier.Companion.border(
                                                        width = 1.dp,
                                                        color = Color(0xFFF97643),
                                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                                            8.dp
                                                        )
                                                    )
                                                } else {
                                                    Modifier.Companion
                                                }
                                            )
                                    ) {
                                        Image(
                                            painter = painterResource(item.frame),
                                            "",
                                            modifier = Modifier.Companion
                                                .size(if (item.frame == R.drawable.ic_none) 32.dp else 72.dp)
                                                .align(Alignment.Companion.Center)
                                        )

                                    }

                                    Text(
                                        text = item.title,
                                        color = if (item.isSelect)
                                            Color.Companion.White
                                        else
                                            Color(0xFF878787),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight(600)
                                    )

                                }
                            }
                        }


                        if (selectedFrame.frame != R.drawable.ic_none) {
                            Row(
                                modifier = Modifier.Companion
                                    .fillMaxWidth()
                                    .padding(top = 20.dp)
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {


                                Button(
                                    onClick = {
                                        val temp = frameState.map { bean ->
                                            bean.copy(isSelect = bean.frame == R.drawable.ic_none)
                                        }.toMutableList()
                                        _frameList.update { temp }
                                    },
                                    modifier = Modifier.Companion
                                        .weight(1f)
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2A2B2F)
                                    ),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                                ) {

                                    Text(
                                        "Cancel",
                                        color = Color.Companion.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight(600)
                                    )
                                }

                                Button(
                                    onClick = {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                ChoWamr::class.java
                                            ).apply {
                                                putExtra("uri", uri)
                                                putExtra("frame", selectedFrame.frame)
                                            })
                                        (context as Activity).finish()
                                    },
                                    modifier = Modifier.Companion
                                        .weight(1f)
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF97643)
                                    ),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                                ) {

                                    Text(
                                        "Select",
                                        color = Color.Companion.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight(600)
                                    )
                                }

                            }

                        }

                        Spacer(
                            modifier = Modifier.Companion.height(
                                24.dp + innerPadding.calculateBottomPadding()
                            )
                        )

                    }

                }
            }
        }
    }
}