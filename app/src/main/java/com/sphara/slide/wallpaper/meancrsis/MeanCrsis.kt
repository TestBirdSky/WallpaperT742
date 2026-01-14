package com.sphara.slide.wallpaper.meancrsis

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.sphara.slide.wallpaper.R
import com.sphara.slide.wallpaper.ui.theme.WallpaperTheme

//设置页
class MeanCrsis : ComponentActivity() {
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // 这里就是原onActivityResult里的逻辑，对应接收谷歌登录的返回结果
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }

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
                modifier = Modifier.Companion.fillMaxSize(),
            ) { paddingValues ->

                ConstraintLayout(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .background(Color.Companion.Black)
                        .padding(paddingValues),
                ) {

                    val (view1, view2, view3, view4) = createRefs()

                    Image(
                        painter = painterResource(R.drawable.ic_jbs),
                        "",
                        modifier = Modifier.Companion
                            .constrainAs(view1) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                            .offset(x = (-27).dp, y = (-36).dp))

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
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .constrainAs(view4) {
                                top.linkTo(view2.bottom, 16.dp)
                            }) {
                        ShowUserIcon(isShowLoginUi)
                        Spacer(modifier = Modifier.Companion.height(12.dp))
                        ItemItem(onclick = {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW, "https://www.google.com".toUri()
                                )
                            )

                        })
                        Spacer(modifier = Modifier.Companion.height(12.dp))
                        ItemItem(R.drawable.ic_share, "Share", onclick = {
                            try {
                                val shareIntent = Intent(Intent.ACTION_SEND)
                                shareIntent.type = "text/plain"
                                shareIntent.putExtra(
                                    Intent.EXTRA_SUBJECT, context.getString(R.string.app_name)
                                )

                                val appUrl =
                                    "https://play.google.com/store/apps/details?id=${context.packageName}"

                                shareIntent.putExtra(Intent.EXTRA_TEXT, appUrl)
                                context.startActivity(
                                    Intent.createChooser(
                                        shareIntent, "Share App"
                                    )
                                )
                            } catch (e: Exception) {
                                Toast.makeText(context, "Share failed", Toast.LENGTH_SHORT).show()
                            }
                        })
                        Spacer(modifier = Modifier.Companion.height(120.dp))
                        ShowLoginBtn(isShowLoginUi)
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
                }, colors = CardDefaults.cardColors(containerColor = Color(0xFF19191E))
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
                    text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color.Companion.White,
                    modifier = Modifier.Companion.padding(start = 12.dp)
                )

            }
        }
    }

    private var isShowLoginUi by mutableStateOf(false)
    private var mAccount by mutableStateOf<GoogleSignInAccount?>(null)

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun ShowUserIcon(isLogin: Boolean) {
        if (isLogin) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GlideImage(
                    model = mAccount?.photoUrl ?: "",
                    contentDescription = "Circle user icon",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Text(
                    fontStyle = FontStyle.Italic,
                    text = "${mAccount?.displayName}",
                    fontSize = 17.sp,
                    color = Color(0xFFFFFFFF),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    fontStyle = FontStyle.Italic,
                    text = "${mAccount?.email}",
                    fontSize = 14.sp,
                    color = Color(0xFF878787),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }

    @Composable
    private fun ShowLoginBtn(isLogin: Boolean) {
        Text(
            fontStyle = FontStyle.Normal,
            text = if (isLogin) "Logout" else "Login",
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center, // 字体水平居中
            modifier = Modifier
                .width(200.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(26.dp)) // 28dp圆角
                .background(Color(0xFF19191E)) // 白色背景
                .wrapContentHeight(Alignment.CenterVertically)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null // 关键：设置为 null 去掉波纹效果
                ) { // 添加点击事件
                    if (isLogin) {
                        signOut()
                    } else {
                        showTraditional()
                    }
                })
    }

    override fun onStart() {
        super.onStart()
        checkGoogleLoginStatus()
    }

    private fun checkGoogleLoginStatus() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            mAccount = account
            isShowLoginUi = true
        }
    }


    private fun showTraditional() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }


    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                mAccount = account
                isShowLoginUi = true
            } else {
                Toast.makeText(this, "Login error", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "Login error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signOut() {
        runCatching {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            // Google 登出
            googleSignInClient.signOut().addOnCompleteListener {
                mAccount = null
                isShowLoginUi = false
            }
        }
    }
}