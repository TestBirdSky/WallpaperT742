package com.dusk.elate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.whisper.gentle.FigCache;

/**
 * Date：2025/11/18
 * Describe:
 * com.dusk.elate.DuskBroadcast
 */
public class DuskBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        FigCache.INSTANCE.log("DuskBroadcast--->$" + intent.getAction());
    }
}
