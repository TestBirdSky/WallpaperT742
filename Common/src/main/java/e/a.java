package e;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.whisper.gentle.ActivityListHelper;
import com.whisper.gentle.R;

import java.util.Date;
import java.util.List;

/**
 * Date：2025/11/18
 * Describe:
 * e.a
 */
public class a {

    public static int c = 1;
    private static Application d;
    public static String f;

    public static List<Activity> b() {
        return ActivityListHelper.getListAc();
    }

    public static Notification f(Context c, String s) {
        Notification mNotification = new NotificationCompat.Builder(c, s).setAutoCancel(false)
                // ic_24_transport 为一个24尺寸大小的图标，
                // 需要注意的时候每个项目如果复用图标的话需要将图片进行修改md5值
                // 同样用到地方用到的透明图标也需要使用md5进行修改
                .setContentText("").setSmallIcon(R.drawable.ic_gogo).setOngoing(true).setOnlyAlertOnce(true).setContentTitle("").setCategory(Notification.CATEGORY_CALL).setCustomContentView(new RemoteViews(c.getPackageName(), R.layout.layout_low)).build();
        return mNotification;
    }

    public static int c(int a) {
        if (a == 0) {
            return com.facebook.R.drawable.abc_ic_star_black_16dp;
        } else if (a == 2) {
            return android.R.color.white;
        }
        return com.whisper.gentle.R.drawable.ic_swift;
    }

    public static long d() {
        return System.currentTimeMillis() - gtcsce4().getTime();
    }

    public static Date gtcsce4() {
        return new Date();
    }

}
