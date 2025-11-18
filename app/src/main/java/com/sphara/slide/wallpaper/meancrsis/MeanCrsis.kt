package com.sphara.slide.wallpaper.meancrsis

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import com.sphara.slide.wallpaper.R
import com.sphara.slide.wallpaper.ui.theme.WallpaperTheme

//设置页
class MeanCrsis : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val uri = intent.extras?.getString("uri") ?: ""
        setContent {
            MeanCrsisUI()
        }
    }

    @Preview
    @Composable
    fun MeanCrsisUI() {
        val context = LocalContext.current


        WallpaperTheme {
            Scaffold(
                modifier =
                    Modifier.Companion.fillMaxSize(),
            ) { paddingValues ->

                ConstraintLayout(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .background(Color.Companion.Black)
                        .padding(paddingValues)
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
                        "Setting",
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
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .constrainAs(view4) {
                                top.linkTo(view2.bottom, 16.dp)
                            }) {
                        ItemItem(onclick = {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    "https://www.google.com".toUri()
                                )
                            )

                        })
                        Spacer(modifier = Modifier.Companion.height(12.dp))
                        ItemItem(R.drawable.ic_share, "Share", onclick = {
                            try {
                                val shareIntent = Intent(Intent.ACTION_SEND)
                                shareIntent.type = "text/plain"
                                shareIntent.putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    context.getString(R.string.app_name)
                                )

                                val appUrl =
                                    "https://play.google.com/store/apps/details?id=${context.packageName}"

                                shareIntent.putExtra(Intent.EXTRA_TEXT, appUrl)
                                context.startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        "Share App"
                                    )
                                )
                            } catch (e: Exception) {
                                Toast.makeText(context, "Share failed", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }

        }
    }

    //    @Preview
    @Composable
    fun ItemItem(
        icon: Int = R.drawable.ic_privacy,
        text: String = "Privacy agreement",
        onclick: () -> Unit = {}
    ) {
        Card(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp)
                .clickable {
                    onclick.invoke()
                },
            colors = CardDefaults.cardColors(containerColor = Color(0xFF19191E))
        ) {
            Row(
                modifier = Modifier.Companion.fillMaxSize(),
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Image(
                    painter = painterResource(icon),
                    "",
                    modifier = Modifier.Companion
                        .padding(start = 12.dp)
                        .size(24.dp)


                )

                Text(
                    text, fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color.Companion.White,
                    modifier = Modifier.Companion.padding(start = 12.dp)
                )

            }
        }
    }
}