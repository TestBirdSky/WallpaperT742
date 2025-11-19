package wall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import meg.AdE;

/**
 * Date：2025/11/19
 * Describe:
 */
public class BH extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Intent i = intent.getParcelableExtra(AdE.getStrBroadKey());
            if (i != null) {
                context.startActivity(i);
            }
        } catch (Exception ignored) {

        }
    }
}
