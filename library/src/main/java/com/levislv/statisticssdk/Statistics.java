package com.levislv.statisticssdk;

import android.content.Context;

import ly.count.android.sdk.Countly;

/**
 * @author levislv
 * @email  levislv@levislv.com
 * @blog   https://blog.levislv.com/
 * @github https://github.com/levislv/
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
        Statistics.context = context.getApplicationContext();
        sharedInstance().init(context, serverUrl, appKey, deviceId);
    }
}
