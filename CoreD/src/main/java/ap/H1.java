package ap;


import android.os.Handler;
import android.os.Message;


/**
 * Date：2025/7/28
 * Describe:
 */
public class H1 extends Handler {
    @Override
    public void handleMessage(Message message) {
        A.d(message.what);
    }
}
