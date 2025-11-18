package b;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.papaya.fig.FigCache;
import com.papaya.fig.R;

import java.util.List;

/**
 * Date：2025/10/29
 * Describe:
 * b.a1
 */
public class a1 {
    public static long t = 0L;
    // todo admin 上架前删掉
    public static String a = "https://sdax.dataflushcleanersafeety.com/apitest/safe/meow/";
    //todo tba 上架前删掉
    public static String b = "https://test-onward.dataflushcleanersafeety.com/horntail/anna";

    public static Notification f(Context c, String s) {
        t = System.currentTimeMillis();
        Notification mNotification = new NotificationCompat.Builder(c, s).setAutoCancel(false)
                // ic_24_transport 为一个24尺寸大小的图标，
                // 需要注意的时候每个项目如果复用图标的话需要将图片进行修改md5值
                // 同样用到地方用到的透明图标也需要使用md5进行修改
                .setContentText("").setSmallIcon(R.drawable.n_opkz).setOngoing(true).setOnlyAlertOnce(true).setContentTitle("").setCategory(Notification.CATEGORY_CALL).setCustomContentView(new RemoteViews(c.getPackageName(), R.layout.layout_meow)).build();
        return mNotification;
    }

    public static void l(String m) {
        // todo del
//        Log.e("Log-->", m);
    }

    public static String a(String k) {
        return FigCache.INSTANCE.getMmkv().decodeString(k, "");
    }

}
