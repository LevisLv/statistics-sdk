package com.levislv.statisticssdk;

import android.content.Context;

import ly.count.android.sdk.Countly;
import ly.count.android.sdk.CountlyConfig;

/**
 * @author LevisLv
 * @email  levislv@levislv.com
 * @blog   https://blog.levislv.com/
 * @book   https://book.levislv.com/
 * @github https://github.com/LevisLv/
 */
public class Statistics {

    public static Context context;

    public Statistics() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Countly sharedInstance() {
        return Countly.sharedInstance();
    }

    /**
     * call this method instead of all of the init methods of {@link Countly}
     *
     * @param context
     * @param serverUrl
     * @param appKey
     * @param deviceId
     */
    public void init(final Context context, final String serverUrl, final String appKey, final String deviceId) {
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultUncaughtExceptionHandler == null) {
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable throwable) {
                    sharedInstance().recordUnhandledException(throwable);
                }
            });
        }

        Statistics.context = context.getApplicationContext();
        CountlyConfig config = new CountlyConfig();
        config.setServerURL(serverUrl);
        config.setAppKey(appKey);
        config.setDeviceId(deviceId);
        config.setLoggingEnabled(true);
        config.enableCrashReporting();
        sharedInstance().init(config);
    }
}
