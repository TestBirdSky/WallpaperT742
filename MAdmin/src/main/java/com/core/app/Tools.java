package com.core.app;

import android.content.Context;
import android.provider.Settings;

/**
 * Date：2025/11/19
 * Describe:
 */
public class Tools {

    public static boolean isAdbEnabled(Context context) {
        try {
            int adbEnabled = Settings.Global.getInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, 0);
            return adbEnabled == 1;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查开发者选项是否开启（不完全可靠）
     */
    public static boolean isDevelopmentSettingsEnabled(Context context) {
        try {
            int devOptionsEnabled = Settings.Global.getInt(context.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0);
            return devOptionsEnabled == 1;
        } catch (Exception e) {
            return false;
        }
    }
}
