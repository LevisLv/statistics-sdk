package com.levislv.statisticssdk.plugin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.levislv.statisticssdk.Statistics;
import com.levislv.statisticssdk.plugin.bean.StatisticsPage;
import com.levislv.statisticssdk.plugin.constant.StatisticsSpConsts;
import com.levislv.statisticssdk.plugin.constant.StatisticsTagConsts;
import com.levislv.statisticssdk.plugin.util.StatisticsSpUtils;
import com.levislv.statisticssdk.util.Utils;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author levislv
 */
public class StatisticsActivityHelper {

    private static final String TAG = StatisticsActivityHelper.class.getSimpleName();
    private static final String TAG_APP = StatisticsActivityHelper.class.getSimpleName() + "_App";
    private static final String TAG_PAGE = StatisticsActivityHelper.class.getSimpleName() + "_Activity";

    private static boolean hasCodeStarted;
    private static int startedCount;
    private static LinkedList<Activity> activityList = new LinkedList<>();
    private static LinkedHashMap<Activity, StatisticsPage> pageMap = new LinkedHashMap<>();

    private static void setPage(Activity activity, String pkgName, int pageId, String pageName, String pageData) {
        try {
            View decorView = activity.getWindow().getDecorView();
            decorView.setTag(StatisticsTagConsts.Page.TAG_KEY_PKG_NAME, pkgName);
            decorView.setTag(StatisticsTagConsts.Page.TAG_KEY_PAGE_ID, pageId);
            decorView.setTag(StatisticsTagConsts.Page.TAG_KEY_PAGE_NAME, pageName);
            decorView.setTag(StatisticsTagConsts.Page.TAG_KEY_PAGE_DATA, pageData);

            if (decorView instanceof ViewGroup) {
                setRootView((ViewGroup) decorView, decorView);
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "setPage", throwable);
        }
    }

    private static void setRootView(ViewGroup viewGroup, View rootView) {
        try {
            if (viewGroup != rootView) {
                viewGroup.setTag(StatisticsTagConsts.Page.TAG_KEY_ROOT_VIEW, rootView);
            }
            for (int index = 0; index < viewGroup.getChildCount(); index++) {
                View view = viewGroup.getChildAt(index);
                if (view instanceof ViewGroup) {
                    setRootView((ViewGroup) view, rootView);
                } else {
                    view.setTag(StatisticsTagConsts.Page.TAG_KEY_ROOT_VIEW, rootView);
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "setRootView", throwable);
        }
    }

    public static void onCreate(Activity activity, String pkgName, int pageId, String pageName, String pageData) {
        setPage(activity, pkgName, pageId, pageName, pageData);

        boolean isLaunchActivity = activityList.isEmpty();

        try {
            if (isLaunchActivity) {
                Map<String, String> segmentation = new HashMap<>(getSegmentation(activity, pkgName, pageId, pageName, pageData));
                // 启动类型
                if (hasCodeStarted) {
                    segmentation.put("startup_type", "hotStart");
                } else {
                    hasCodeStarted = true;
                    segmentation.put("startup_type", "coldStart");
                }
                // 是否首次启动
                segmentation.put("is_first_enter", String.valueOf(StatisticsSpUtils.getInstance().getBoolean(StatisticsSpConsts.SP_BOOLEAN_KEY_IS_APP_FIRST_ENTER, true)));
                // 距离上次启动间隔时间
                long appLastEnterTime = StatisticsSpUtils.getInstance().getLong(StatisticsSpConsts.SP_LONG_KEY_APP_LAST_ENTER_TIME, -1L);
                segmentation.put("from_last_enter_time", String.valueOf(System.currentTimeMillis() - appLastEnterTime));
                Statistics.sharedInstance().recordEvent("common_AppEnter", segmentation, 1);
                Log.e(TAG_APP, "common_AppEnter");
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "onCreate", throwable);
        } finally {
            StatisticsSpUtils.getInstance().put(StatisticsSpConsts.SP_BOOLEAN_KEY_IS_APP_FIRST_ENTER, false);
            StatisticsSpUtils.getInstance().put(StatisticsSpConsts.SP_LONG_KEY_APP_LAST_ENTER_TIME, System.currentTimeMillis());
        }

        setTopActivity(activity, new StatisticsPage(pageId, pageName, pageData));
    }

    public static void onStart(Activity activity, String pkgName, int pageId, String pageName, String pageData) {
        startedCount++;
        if (startedCount == 1) {
            try {
                Map<String, String> segmentation = new HashMap<>(getSegmentation(activity, pkgName, pageId, pageName, pageData));
                Statistics.sharedInstance().recordEvent("common_AppToFg", segmentation, 1);
                Log.e(TAG_APP, "common_AppToFg");
            } catch (Throwable throwable) {
                Log.e(TAG, "onStart", throwable);
            }
        }

        setTopActivity(activity, new StatisticsPage(pageId, pageName, pageData));
    }

    public static void onResume(Activity activity, String pkgName, int pageId, String pageName, String pageData) {
        boolean isEnter = getTopActivity() == activity;
        boolean isLaunchActivity = activityList.size() == 1 || activityList.size() == 2 && activityList.get(0) == activity;

        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(activity, pkgName, pageId, pageName, pageData));
            StatisticsPage lastPage = null;
            if (isEnter) {
                segmentation.put("page_exposure_type", "enter");
                if (!isLaunchActivity) {
                    lastPage = pageMap.get(activityList.get(activityList.size() - 2));
                }
            } else {
                segmentation.put("page_exposure_type", "exit");
                lastPage = pageMap.get(getTopActivity());
            }
            if (lastPage != null) {
                if (lastPage.getId() > 0) {
                    segmentation.put("last_page_id", Utils.getLayoutResEntryName(activity, pkgName, lastPage.getId()));
                }
                segmentation.put("last_page_name", lastPage.getName());
            }
            Statistics.sharedInstance().recordEvent("common_PageEnter", segmentation, 1);
            Log.e(TAG_PAGE, "common_PageEnter, page: " + activity);
        } catch (Throwable throwable) {
            Log.e(TAG, "onResume", throwable);
        }

        setTopActivity(activity, new StatisticsPage(pageId, pageName, pageData));
    }

    public static void onPause(Activity activity, String pkgName, int pageId, String pageName, String pageData) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(activity, pkgName, pageId, pageName, pageData));
            Statistics.sharedInstance().recordEvent("common_PageExit", segmentation, 1);
            Log.e(TAG_PAGE, "common_PageExit, page: " + activity);
        } catch (Throwable throwable) {
            Log.e(TAG, "onPause", throwable);
        }
    }

    public static void onStop(Activity activity, String pkgName, int pageId, String pageName, String pageData) {
        startedCount--;
        if (startedCount == 0) {
            try {
                Map<String, String> segmentation = new HashMap<>(getSegmentation(activity, pkgName, pageId, pageName, pageData));
                Statistics.sharedInstance().recordEvent("common_AppToBg", segmentation, 1);
                Log.e(TAG_APP, "common_AppToBg");
            } catch (Throwable throwable) {
                Log.e(TAG, "onStop", throwable);
            }
        }
    }

    public static void onDestroy(Activity activity, String pkgName, int pageId, String pageName, String pageData) {
        try {
            boolean isLaunchActivity = activityList.size() == 1;
            if (isLaunchActivity) {
                Map<String, String> segmentation = new HashMap<>(getSegmentation(activity, pkgName, pageId, pageName, pageData));
                Statistics.sharedInstance().recordEvent("common_AppExit", segmentation, 1);
                Log.e(TAG_APP, "common_AppExit");
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "onDestroy", throwable);
        }

        activityList.remove(activity);
        pageMap.remove(activity);
    }

    /**
     * 设置栈顶activity
     *
     * @param activity 栈顶activity
     * @param page     栈顶page
     */
    private static void setTopActivity(Activity activity, StatisticsPage page) {
        if (activityList.contains(activity)) {
            if (!activityList.getLast().equals(activity)) {
                activityList.remove(activity);
                activityList.addLast(activity);
            }
        } else {
            activityList.addLast(activity);
        }

        if (!pageMap.containsKey(activity)) {
            pageMap.put(activity, page);
        }
    }

    /**
     * 获取栈顶activity
     *
     * @return 栈顶activity
     */
    private static Activity getTopActivity() {
        final Activity topActivity = activityList.getLast();
        if (topActivity != null) {
            return topActivity;
        }
        // using reflect to get top activity
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            if (activities == null) {
                return null;
            }
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    // setTopActivity(activity);
                    return activity;
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "getTopActivity", throwable);
        }
        return null;
    }

    /**
     * 封装segmentation
     *
     * @param activity 页面
     * @param pkgName  包名
     * @param pageId   页面id
     * @param pageName 页面name
     * @param pageData 页面data
     * @return segmentation
     */
    private static Map<String, String> getSegmentation(Activity activity, String pkgName, int pageId, String pageName, String pageData) {
        Map<String, String> segmentation = new HashMap<>();
        try {
            // page_id
            if (pageId > 0) {
                segmentation.put("page_id", Utils.getLayoutResEntryName(activity, pkgName, pageId));
            }
            // page_name
            segmentation.put("page_name", pageName);
            // page_type
            segmentation.put("page_type", activity.getClass().getName().substring(activity.getPackageName().length()));
            // data
            JSONObject jsonObject = new JSONObject(pageData);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                segmentation.put(key, jsonObject.getString(key));
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "getSegmentation", throwable);
        }
        return segmentation;
    }
}
