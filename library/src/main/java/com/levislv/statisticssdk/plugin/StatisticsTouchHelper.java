package com.levislv.statisticssdk.plugin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.levislv.statisticssdk.Statistics;
import com.levislv.statisticssdk.plugin.bean.StatisticsView;
import com.levislv.statisticssdk.plugin.util.StatisticsTouchUtils;
import com.levislv.statisticssdk.util.Utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author LevisLv
 * @email  levislv@levislv.com
 * @blog   https://blog.levislv.com/
 * @book   https://book.levislv.com/
 * @github https://github.com/LevisLv/
 */
public class StatisticsTouchHelper {

    private static final String TAG = StatisticsTouchHelper.class.getSimpleName();

    public static void dispatchTouchEvent(Activity activity, String pkgName, MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                StatisticsView statisticsView = StatisticsTouchUtils.findStatisticsViewByXY(activity, pkgName, ev.getRawX(), ev.getRawY());
                if (statisticsView != null) {
                    Map<String, String> segmentation = new HashMap<>(getSegmentation(statisticsView));
//                    segmentation.put("language", String.valueOf(getLanguage()));
//                    segmentation.put("country", String.valueOf(getCountry()));
//                    segmentation.put("os", String.valueOf(getOS()));
//                    segmentation.put("os_version", String.valueOf(getOSVersion()));
//                    segmentation.put("operator", String.valueOf(getOperator()));
//                    segmentation.put("network_type", String.valueOf(getNetworkType()));
                    segmentation.put("app_version", String.valueOf(getAppVersion()));
                    segmentation.put("screen_size", getScreenSize());
                    segmentation.put("coordinate", ev.getRawX() + "," + ev.getRawY());
                    Statistics.sharedInstance().recordEvent("common_HeatMap", segmentation, 1);
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "dispatchTouchEvent", throwable);
        }
    }

    /**
     * 封装segmentation
     *
     * @param statisticsView statisticsView
     * @return segmentation
     */
    private static Map<String, String> getSegmentation(StatisticsView statisticsView) {
        Map<String, String> segmentation = new HashMap<>();
        try {
            String pkgName = statisticsView.getPkgName();
            int pageId = statisticsView.getPageId();
            String pageName = statisticsView.getPageName();
            View view = statisticsView.getView();
            // page_id
            if (!TextUtils.isEmpty(pkgName) && pageId > 0) {
                segmentation.put("page_id", Utils.getLayoutResEntryName(view.getContext(), pkgName, pageId));
            }
            // page_name
            segmentation.put("page_name", pageName);
            // location
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            segmentation.put("location", location[0] + "," + location[1] + "|" + (location[0] + view.getWidth()) + "," + (location[1] + view.getHeight()));
        } catch (Throwable throwable) {
            Log.e(TAG, "getSegmentation", throwable);
        }
        return segmentation;
    }

    private static String getLanguage() {
        try {
            return Locale.getDefault().getLanguage();
        } catch (Throwable throwable) {
            Log.e(TAG, "getLanguage", throwable);
        }
        return "null";
    }

    private static String getCountry() {
        try {
            return Locale.getDefault().getCountry();
        } catch (Throwable throwable) {
            Log.e(TAG, "getCountry", throwable);
        }
        return "null";
    }

    private static String getOS() {
        return "Android";
    }

    private static String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    private static String getOperator() {
        try {
            final TelephonyManager telephonyManager = (TelephonyManager) Statistics.context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getNetworkOperatorName();
        } catch (Throwable throwable) {
            Log.e(TAG, "getOperator", throwable);
        }
        return "null";
    }

    private static String getNetworkType() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) Statistics.context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String subtypeName = networkInfo.getSubtypeName();
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return "2G";
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return "3G";
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return "4G";
                    default:
                        if ("TD-SCDMA".equalsIgnoreCase(subtypeName)
                                || "WCDMA".equalsIgnoreCase(subtypeName)
                                || "CDMA2000".equalsIgnoreCase(subtypeName)) {
                            return "3G";
                        } else {
                            return subtypeName;
                        }
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "getNetworkType", throwable);
        }
        return "null";
    }

    private static String getAppVersion() {
        try {
            return Statistics.context.getPackageManager().getPackageInfo(Statistics.context.getPackageName(), 0).versionName;
        } catch (Throwable throwable) {
            Log.e(TAG, "getAppVersion", throwable);
        }
        return "null";
    }

    private static String getScreenSize() {
        WindowManager windowManager = (WindowManager) Statistics.context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return Statistics.context.getResources().getDisplayMetrics().widthPixels + "," + Statistics.context.getResources().getDisplayMetrics().heightPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowManager.getDefaultDisplay().getRealSize(point);
        } else {
            windowManager.getDefaultDisplay().getSize(point);
        }
        return point.x + "," + point.y;
    }
}
