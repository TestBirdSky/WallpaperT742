package com.sphara.slide.wallpaper.tadycib

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.sphara.slide.wallpaper.utils.FavSpUtils
import com.sphara.slide.wallpaper.data.ImageData
import com.sphara.slide.wallpaper.R
import com.sphara.slide.wallpaper.cureppate.CurepPate
import com.sphara.slide.wallpaper.mazdkan.MazdKan
import com.sphara.slide.wallpaper.ui.theme.WallpaperTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//壁纸页
class TadyCib : ComponentActivity() {


    private val _wallpaperList = MutableStateFlow(
        listOf(
            ImageData(R.drawable.paper_1, "paper_1"),
            ImageData(R.drawable.paper_2, "paper_2"),
            ImageData(R.drawable.paper_3, "paper_3"),
            ImageData(R.drawable.paper_4, "paper_4"),
            ImageData(R.drawable.paper_5, "paper_5"),
            ImageData(R.drawable.paper_6, "paper_6"),
            ImageData(R.drawable.paper_7, "paper_7"),
            ImageData(R.drawable.paper_8, "paper_8"),
            ImageData(R.drawable.paper_9, "paper_9"),
            ImageData(R.drawable.paper_10, "paper_10"),
            ImageData(R.drawable.paper_11, "paper_11"),
            ImageData(R.drawable.paper_12, "paper_12"),
            ImageData(R.drawable.paper_13, "paper_13"),
            ImageData(R.drawable.paper_14, "paper_14"),
            ImageData(R.drawable.paper_15, "paper_15"),
            ImageData(R.drawable.paper_16, "paper_16"),
            ImageData(R.drawable.paper_17, "paper_17"),
            ImageData(R.drawable.paper_18, "paper_18"),
            ImageData(R.drawable.paper_19, "paper_19"),
            ImageData(R.drawable.paper_20, "paper_20"),
        ).shuffled()
    )

    private val wallpaperList: StateFlow<List<ImageData>> = _wallpaperList

    private var from = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FavSpUtils.init(this)
        from = intent.extras?.getString("from") ?: ""
        setContent {
            TadyCibUI()
        }


    }

    private fun refresh() {
        lifecycleScope.launch {
            val list = FavSpUtils.getList("FavList")
            val newList = _wallpaperList.value.map { imageData ->
                val flag = list.contains(imageData.uuid)
                imageData.copy(isFavorite = flag)
            }
            _wallpaperList.update { newList }
        }
    }

    override fun onStart() {
        super.onStart()
        refresh()
    }

        @Preview
    @Composable
    fun TadyCibUI() {
        val context = LocalContext.current
        var tabIndx by remember { mutableIntStateOf(0) }

        //collectAsState() 内部已经做了记忆化处理，所以外层的 remember 是多余的。
        val allListState by wallpaperList.collectAsState()

        val itemList = if (tabIndx == 0) allListState else allListState.filter { it.isFavorite }

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
                                if (from.isNotEmpty()) {
                                    context.startActivity(
                                        Intent(context, CurepPate::class.java)
                                    )
                                }
                                (context as Activity).finish()
                            },
                    )
                    Text(
                        "Wallpaper",
                        color = Color.Companion.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier.Companion.constrainAs(view3) {
                            top.linkTo(view2.top)
                            bottom.linkTo(view2.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })

                    Row(
                        modifier = Modifier.Companion
                            .constrainAs(view4) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(view2.bottom, 12.dp)
                            }
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextButton(
                            onClick = {
                                tabIndx = 0
                            }, modifier = Modifier.Companion
                                .weight(1f)
                                .height(58.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (tabIndx == 0) Color(0xFFF97643) else Color(0xFF2A2B2F)
                                ),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "All", color =
                                    if (tabIndx == 0) Color.Companion.White else Color(0xFF878787),
                                fontSize = 16.sp,
                                fontWeight = FontWeight(600)
                            )
                        }

                        TextButton(
                            onClick = {
                                tabIndx = 1
                            }, modifier = Modifier.Companion
                                .weight(1f)
                                .height(58.dp)
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                                .background(
                                    if (tabIndx == 1) Color(0xFFF97643) else Color(0xFF2A2B2F)
                                ),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "Favorite", color =
                                    if (tabIndx == 1) Color.Companion.White else Color(0xFF878787),
                                fontSize = 16.sp,
                                fontWeight = FontWeight(600)
                            )
                        }
                    }


//                    Box(
//                        modifier = Modifier.Companion
//                            .constrainAs(view5) {
//                                top.linkTo(view4.bottom)
//                            }
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp)
//                            .padding(top = 16.dp, bottom = 8.dp)
//                            .height(68.dp)
//                    ) {
//                        Image(
//                            painter = painterResource(R.drawable.bg_naozhong),
//                            "",
//                            modifier = Modifier.Companion.fillMaxSize(),
//                            contentScale = ContentScale.Companion.FillBounds
//                        )
//                        Row(
//                            verticalAlignment = Alignment.Companion.CenterVertically,
//                            modifier = Modifier.Companion
//                                .fillMaxSize()
//                                .clickable {
//                                    context.startActivity(Intent(context, PapilMos::class.java))
//                                }
//                        ) {
//
//                            Image(
//                                painter = painterResource(R.drawable.ic_naozhong),
//                                "", modifier = Modifier.Companion.padding(start = 12.dp)
//                            )
//                            Text(
//                                "Scheduled Change",
//                                color = Color.Companion.White,
//                                modifier = Modifier.Companion.padding(start = 12.dp)
//                            )
//
//                        }
//                    }


                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.Companion
                            .constrainAs(view6) {
                                top.linkTo(view4.bottom)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.Companion.fillToConstraints
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)

                    ) {

                        items(
                            itemList,
                            key = { it.image }) { item ->
                            AsyncImage(
                                model = ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(item.image)
                                    .size(Size(180, 300))
                                    .build(),
                                "",
                                modifier = Modifier.Companion
                                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                                    .height(240.dp)
                                    .clickable {
                                        MazdKan.Companion.imageData = item
                                        context.startActivity(Intent(context, MazdKan::class.java))
                                    },
                                contentScale = ContentScale.Companion.Crop
                            )
                        }
                    }

                }
            }
        }
    }
}