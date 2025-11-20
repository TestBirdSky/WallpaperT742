package com.sphara.slide.wallpaper.cureppate

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.sphara.slide.wallpaper.meancrsis.MeanCrsis
//import com.sphara.slide.wallpaper.papilmos.PapilMos
import com.sphara.slide.wallpaper.patycate.PatyCate
import com.sphara.slide.wallpaper.R
import com.sphara.slide.wallpaper.tadycib.TadyCib
import com.sphara.slide.wallpaper.ui.theme.WallpaperTheme
import java.util.jar.Manifest

//主页
class CurepPate : ComponentActivity() {

    private var imageCallBack: (uri: String) -> Unit = {}
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageCallBack.invoke(it.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurepPateUI()
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            requestPermissions(
//                arrayOf(
//                    android.Manifest.permission.POST_NOTIFICATIONS
//                ), 1
//            )
//        }
    }

        @Preview
    @Composable
    fun CurepPateUI() {
        val context = LocalContext.current

        WallpaperTheme {
            Scaffold(
                modifier = Modifier.Companion
                    .fillMaxSize()
            ) { innerPadding ->

                ConstraintLayout(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .background(Color.Companion.Black)
                        .padding(innerPadding)
                ) {

                    val (view1, view2, view3, view4) = createRefs()

                    Image(
                        painter = painterResource(R.drawable.ic_jbs), "",
                        modifier = Modifier.Companion
                            .constrainAs(view1) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                            .offset(x = (-27).dp, y = (-36).dp)
                    )

                    Text(
                        stringResource(R.string.app_name),
                        modifier = Modifier.Companion
                            .constrainAs(view2) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                            .padding(vertical = 15.dp)
                            .padding(start = 20.dp),
                        color = Color.Companion.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight(700)
                    )

                    Image(
                        painter = painterResource(R.drawable.ic_ssss), "",
                        modifier = Modifier.Companion
                            .size(37.dp)
                            .clickable {

                                context.startActivity(Intent(context, MeanCrsis::class.java))
                            }
                            .constrainAs(view3) {
                                top.linkTo(view2.top)
                                bottom.linkTo(view2.bottom)
                                end.linkTo(parent.end, 20.dp)
                            },
                    )


                    Column(modifier = Modifier.Companion.constrainAs(view4) {
                        top.linkTo(view2.bottom, 16.dp)
                    }) {
                        Row(
                            modifier = Modifier.Companion.padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            MyCardItem(
                                modifier = Modifier.Companion
                                    .clickable {
                                        context.startActivity(Intent(context, TadyCib::class.java))
                                    }
                                    .weight(1f))
                            MyCardItem(
                                modifier = Modifier.Companion
                                    .clickable {
                                        imageCallBack = { uri ->
                                            context.startActivity(
                                                Intent(
                                                    context,
                                                    PatyCate::class.java
                                                ).apply {
                                                    putExtra("uri", uri)
                                                })
                                        }
                                        pickImage.launch("image/*")
                                    }
                                    .weight(1f),
                                R.drawable.ig_2,
                                "Photo Frame",
                                "Get more Photo\nframe"
                            )
                        }

                        Spacer(modifier = Modifier.Companion.height(15.dp))

//                        Row(
//                            modifier = Modifier.Companion.padding(horizontal = 20.dp),
//                            horizontalArrangement = Arrangement.spacedBy(16.dp)
//                        ) {
//                            MyCardItem(
//                                modifier = Modifier.Companion
//                                    .clickable {
//                                        startActivity(Intent(context, PapilMos::class.java))
//                                    }
//                                    .weight(1f),
//                                R.drawable.ig_3,
//                                "Scheduled\nChange",
//                                "Schedule a wallpaper change"
//                            )
//                            Spacer(modifier = Modifier.Companion.weight(1f))
//                        }

                    }

                }

            }
        }
    }

    //    @Preview
    @Composable
    private fun MyCardItem(
        modifier: Modifier = Modifier.Companion,
        img: Int = R.drawable.ig_1,
        text: String = "Wallpapers",
        text2: String = "Browse cool\nwallpaper for free"
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2E33))
        ) {

            Column {
                Image(
                    painter = painterResource(img),
                    "",
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .height(175.dp),
                    contentScale = ContentScale.Companion.FillWidth
                )
                Text(
                    text, modifier = Modifier.Companion
                        .padding(top = 16.dp, start = 12.dp),
                    color = Color.Companion.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight(600)

                )
                Text(
                    text2, modifier = Modifier.Companion
                        .padding(top = 2.5.dp, start = 12.dp, bottom = 15.dp),
                    color = Color(0xff878787),
                    fontSize = 13.sp,
                    fontWeight = FontWeight(400),
                    lineHeight = 13.sp
                )

            }

        }
    }
}