package com.sphara.slide.wallpaper.papilmos

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import com.sphara.slide.wallpaper.utils.FavSpUtils
import com.sphara.slide.wallpaper.R
import com.sphara.slide.wallpaper.data.ScheData
import com.sphara.slide.wallpaper.data.ScheData.Companion.toBean
import com.sphara.slide.wallpaper.data.ScheData.Companion.toJson
import com.sphara.slide.wallpaper.ui.theme.WallpaperTheme
import com.sphara.slide.wallpaper.utils.AlarmUtils.scheduleDailyAlarm
import com.sphara.slide.wallpaper.utils.canScheduleExactAlarms
import com.sphara.slide.wallpaper.utils.scheduleRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.UUID

//定时提醒
class PapilMos : ComponentActivity() {

    private val _scheData = MutableStateFlow<List<ScheData>>(emptyList())
    private val scheData = _scheData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        FavSpUtils.init(this)
        setContent {
            PapilMosUI()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val list = FavSpUtils.getList("Schedule").map { JSONObject(it).toBean() }
            _scheData.update { list }
        }

        if (canScheduleExactAlarms(this).not()) {
            scheduleRequest(this)
        }
    }

    @Preview
    @Composable
    fun PapilMosUI() {

        val context = LocalContext.current
        val itemList = scheData.collectAsState()
        var showDialog by remember { mutableStateOf(false) }
        var addScheme by remember { mutableStateOf(ScheData()) }

        WallpaperTheme {
            Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->

                ConstraintLayout(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .background(Color.Companion.Black)
                        .padding(innerPadding)
                ) {

                    val (view1, view2, view3, view4, view5) = createRefs()

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
                        "Scheduled Change",
                        color = Color.Companion.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier.Companion.constrainAs(view3) {
                            top.linkTo(view2.top)
                            bottom.linkTo(view2.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })


                    if (itemList.value.isEmpty()) {
                        Column(modifier = Modifier.Companion.constrainAs(view4) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)

                        }, horizontalAlignment = Alignment.Companion.CenterHorizontally) {
                            Image(
                                painter = painterResource(R.drawable.ic_12_15_10),
                                ""
                            )

                            Text(
                                "Set a time, and we’ll remind you to change your favorite wallpaper when it’s time.",
                                modifier = Modifier.Companion
                                    .padding(horizontal = 26.dp)
                                    .padding(top = 10.dp),
                                color = Color.Companion.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight(500),
                                textAlign = TextAlign.Companion.Center

                            )

                            Button(
                                onClick = {
                                    showDialog = true
                                }, modifier = Modifier.Companion
                                    .padding(top = 28.dp)
                                    .background(
                                        color = Color(0xFFF97643),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor =
                                        Color(0xFFF97643)
                                )
                            ) {
                                Text(
                                    "+ Add Time",
                                    color = Color.Companion.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight(600)
                                )
                            }

                        }
                    } else {

                        Column(
                            modifier = Modifier.Companion.constrainAs(view5) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(view2.bottom, 20.dp)
                            },
                            horizontalAlignment = Alignment.Companion.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {

                            Button(
                                onClick = {
                                    showDialog = true
                                }, modifier = Modifier.Companion
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 12.dp)
                                    .background(
                                        color = Color(0xFF2A2B2F),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                            8.dp
                                        )
                                    ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor =
                                        Color(0xFF2A2B2F)
                                )
                            ) {
                                Text(
                                    "+ Add Time",
                                    color = Color(0xFF878787),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight(600)
                                )
                            }

                            LazyColumn {
                                items(itemList.value, key = { it.uuid }) {
                                    ItemTime(it)
                                }
                            }
                        }

                    }


                }

                if (showDialog) {

                    Dialog(
                        onDismissRequest = { showDialog = false },
                        // 禁用平台默认宽度约束
                        properties = DialogProperties(
                            usePlatformDefaultWidth = false,
                            dismissOnClickOutside = true
                        ),
                    ) {
                        Column(
                            modifier = Modifier.Companion
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        showDialog = false
                                    }
                            )

                            Box(
                                modifier = Modifier.Companion
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .background(
                                        color = Color(0xFF2A2B2F),
                                        shape = RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp
                                        )
                                    )
                            ) {

                                Column(
                                    modifier = Modifier.Companion
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Text(
                                        "Add Time",
                                        color = Color.Companion.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight(600),
                                        modifier = Modifier.Companion.padding(top = 24.dp)
                                    )

                                    Text(
                                        "Select these options below:",
                                        color = Color((0xFF878787)),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(600)
                                    )

                                    Row(
                                        modifier = Modifier.Companion
                                            .padding(top = 25.dp)
                                            .align(Alignment.Companion.CenterHorizontally)
                                    ) {
                                        Column(horizontalAlignment = Alignment.Companion.CenterHorizontally) {
                                            Text(
                                                "Hour",
                                                color = Color(0xFF878787),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight(500)
                                            )
                                            Image(
                                                painter = painterResource(R.drawable.ic_s), "",
                                                modifier = Modifier.Companion
                                                    .padding(top = 20.dp)
                                                    .size(24.dp)
                                                    .clickable {
                                                        var h = addScheme.timeH
                                                        if (h == 23) {
                                                            h = 0
                                                        } else {
                                                            h = h + 1
                                                        }
                                                        addScheme = addScheme.copy(timeH = h)
                                                    }
                                            )
                                            Text(
                                                addScheme.formatterH(),
                                                color = Color.Companion.White,
                                                fontWeight = FontWeight(600),
                                                fontSize = 44.sp,
                                                modifier = Modifier.Companion
                                                    .padding(vertical = 8.dp)
                                            )
                                            Image(
                                                painter = painterResource(R.drawable.ic_x), "",
                                                modifier = Modifier.Companion
                                                    .size(24.dp)
                                                    .clickable {
                                                        var h = addScheme.timeH
                                                        if (h == 0) {
                                                            h = 23
                                                        } else {
                                                            h = h - 1
                                                        }
                                                        addScheme = addScheme.copy(timeH = h)
                                                    }
                                            )
                                        }
                                        Text(
                                            ":",
                                            modifier = Modifier.Companion
                                                .padding(horizontal = 25.dp)
                                                .align(Alignment.Companion.CenterVertically),
                                            fontSize = 44.sp,
                                            fontWeight = FontWeight(600),
                                            color = Color.Companion.White
                                        )
                                        Column(horizontalAlignment = Alignment.Companion.CenterHorizontally) {
                                            Text(
                                                "MINUTE",
                                                color = Color(0xFF878787),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight(500)
                                            )
                                            Image(
                                                painter = painterResource(R.drawable.ic_s), "",
                                                modifier = Modifier.Companion
                                                    .padding(top = 20.dp)
                                                    .size(24.dp)
                                                    .clickable {
                                                        var m = addScheme.timeM
                                                        if (m == 59) {
                                                            m = 0
                                                        } else {
                                                            m = m + 1
                                                        }
                                                        addScheme = addScheme.copy(timeM = m)
                                                    }
                                            )
                                            Text(
                                                addScheme.formatterM(),
                                                color = Color.Companion.White,
                                                fontWeight = FontWeight(600),
                                                fontSize = 44.sp,
                                                modifier = Modifier.Companion.padding(vertical = 8.dp)
                                            )
                                            Image(
                                                painter = painterResource(R.drawable.ic_x), "",
                                                modifier = Modifier.Companion
                                                    .size(24.dp)
                                                    .clickable {
                                                        var m = addScheme.timeM
                                                        if (m == 0) {
                                                            m = 59
                                                        } else {
                                                            m = m - 1
                                                        }
                                                        addScheme = addScheme.copy(timeM = m)
                                                    }
                                            )
                                        }

                                    }

                                    Text(
                                        "Cycle",
                                        color = Color.Companion.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight(600),
                                        modifier = Modifier.Companion.padding(top = 24.dp)
                                    )

                                    Spacer(Modifier.Companion.height(15.dp))

                                    Button(
                                        onClick = {
                                            addScheme = addScheme.copy(isOnce = false)
                                        },
                                        modifier = Modifier.Companion
                                            .fillMaxWidth()
                                            .height(55.dp)
                                            .background(
                                                color = Color(0xFF3B3D42),
                                                shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                                    8.dp
                                                )
                                            ),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(
                                                0xFF3B3D42
                                            )
                                        ),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                            8.dp
                                        ),
                                        contentPadding = PaddingValues(0.dp)

                                    ) {
                                        Text(
                                            "Every Day",
                                            color = Color.Companion.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight(600),
                                            modifier = Modifier.Companion
                                                .padding(start = 16.dp, end = 12.dp)
                                                .weight(1f)

                                        )
                                        Image(
                                            painter = painterResource(if (addScheme.isOnce) R.drawable.ic_select_n else R.drawable.ic_select_s),
                                            "",
                                            modifier = Modifier.Companion.size(30.dp)
                                        )
                                    }

                                    Spacer(Modifier.Companion.height(16.dp))

                                    Button(
                                        onClick = {
                                            addScheme = addScheme.copy(isOnce = true)
                                        },
                                        modifier = Modifier.Companion
                                            .fillMaxWidth()
                                            .height(55.dp)
                                            .background(
                                                color = Color(0xFF3B3D42),
                                                shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                                    8.dp
                                                )
                                            ),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(
                                                0xFF3B3D42
                                            )
                                        ),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                            8.dp
                                        ),
                                        contentPadding = PaddingValues(0.dp)

                                    ) {
                                        Text(
                                            "Once",
                                            color = Color.Companion.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight(600),
                                            modifier = Modifier.Companion
                                                .padding(start = 16.dp, end = 12.dp)
                                                .weight(1f)
                                        )
                                        Image(
                                            painter = painterResource(if (addScheme.isOnce) R.drawable.ic_select_s else R.drawable.ic_select_n),
                                            "",
                                            modifier = Modifier.Companion.size(30.dp)
                                        )
                                    }


                                    Button(
                                        onClick = {
                                            val list = _scheData.value.toMutableList()
                                            val copy =
                                                addScheme.copy(uuid = UUID.randomUUID().toString())
                                            list.add(copy)
                                            _scheData.update { list }
                                            FavSpUtils.putList("Schedule", list.map { it.toJson() })
                                            showDialog = false

                                            if (canScheduleExactAlarms(this@PapilMos).not()) {
                                                scheduleRequest(this@PapilMos)
                                            }

                                            scheduleDailyAlarm(
                                                this@PapilMos,
                                                copy.getRequestCode(),
                                                copy.timeH,
                                                copy.timeM
                                            )

                                        }, modifier = Modifier.Companion
                                            .align(Alignment.Companion.CenterHorizontally)
                                            .padding(top = 16.dp)
                                            .background(
                                                color = Color(0xFFF97643),
                                                shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                                    12.dp
                                                )
                                            ),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor =
                                                Color(0xFFF97643)
                                        )
                                    ) {
                                        Text(
                                            "+ Add Time",
                                            color = Color.Companion.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight(600)
                                        )
                                    }

                                    Spacer(Modifier.Companion.height(20.dp))

                                }

                            }

                        }
                    }

                }

            }
        }
    }

    @OptIn(ExperimentalWearMaterialApi::class)
    @Composable
    fun ItemTime(
        scheData: ScheData = ScheData()
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val density = LocalDensity.current

        // 可滚动区域：删除按钮55.dp + 间距16.dp = 71.dp
        val maxScrollValuePx = with(density) { 71.dp.toPx() }

        // 使用相对阈值，基于最大滚动值的百分比
        val thresholdPx = maxScrollValuePx * 0.5f // 50%作为阈值

        val scrollState = rememberScrollState()

        // 记录当前的吸附状态
        var isRevealed by remember { mutableStateOf(false) }

        // 记录上一次滚动是否在进行中
        var wasScrollInProgress by remember { mutableStateOf(false) }

        // 自定义动画时间
        val animationDuration = 150 // 增加到200ms让动画更平滑

        // 使用 LaunchedEffect 监听滚动状态变化
        LaunchedEffect(scrollState.isScrollInProgress) {
            val isScrolling = scrollState.isScrollInProgress

            if (wasScrollInProgress && !isScrolling) {
                // 滚动刚刚停止，执行吸附逻辑
                val currentScroll = scrollState.value
                val shouldReveal = currentScroll > thresholdPx

                // 只有当状态确实需要改变时才执行动画
                if (shouldReveal != isRevealed) {
                    isRevealed = shouldReveal
                    val targetValue = if (shouldReveal) maxScrollValuePx.toInt() else 0

                    // 使用更平滑的动画
                    scrollState.animateScrollTo(
                        targetValue,
                        animationSpec = tween(
                            durationMillis = animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    )
                } else if (shouldReveal && currentScroll < maxScrollValuePx - 10) {
                    // 如果应该显示但没有完全显示，确保完全显示
                    scrollState.animateScrollTo(
                        maxScrollValuePx.toInt(),
                        animationSpec = tween(
                            durationMillis = animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    )
                } else if (!shouldReveal && currentScroll > 10) {
                    // 如果应该隐藏但没有完全隐藏，确保完全隐藏
                    scrollState.animateScrollTo(
                        0,
                        animationSpec = tween(
                            durationMillis = animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            }

            wasScrollInProgress = isScrolling
        }

        Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .horizontalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier.Companion
                        .width(screenWidth - 32.dp + 16.dp + 55.dp)
                        .height(55.dp),
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Text(
                        text = scheData.formatterTime(),
                        color = Color.Companion.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700)
                    )

                    Text(
                        text = if (scheData.isOnce) "Once" else "Every Day",
                        color = Color(0xFF878787),
                        fontSize = 14.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier.Companion.weight(1f),
                        textAlign = TextAlign.Companion.Center
                    )

                    Spacer(modifier = Modifier.Companion.width(8.dp))

                    Image(
                        painter = painterResource(
                            if (scheData.isOpen)
                                R.drawable.ic_sw_s
                            else
                                R.drawable.ic_sw_n
                        ),
                        contentDescription = "Schedule type indicator",
                        modifier = Modifier.Companion
                            .padding(end = 16.dp)
                            .width(44.dp)
                            .height(24.dp)
                            .clickable {

                                val list = _scheData.value.map { bean ->
                                    if (bean.uuid == scheData.uuid) {
                                        bean.copy(isOpen = !scheData.isOpen)
                                    } else {
                                        bean
                                    }
                                }.toMutableList()

                                _scheData.update { list }

                                FavSpUtils.putList("Schedule", list.map { it.toJson() })
                            },
                        contentScale = ContentScale.Companion.Fit,
                    )

                    // 删除按钮
                    Button(
                        onClick = {
                            val list = _scheData.value.toMutableList()
                            list.remove(scheData)
                            _scheData.update { list }

                            FavSpUtils.putList("Schedule", list.map { it.toJson() })

                        },
                        modifier = Modifier.Companion
                            .size(55.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF3D3D)
                        ),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_remove),
                            contentDescription = "Delete",
                            modifier = Modifier.Companion.size(20.dp),
                            contentScale = ContentScale.Companion.Fit
                        )
                    }
                }
            }
        }
    }

}