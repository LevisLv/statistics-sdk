package com.levislv.statisticssdk.util;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * @author levislv
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    private static final String STATISTICS_R_CLASS_NAME = "%s.StatisticsR";

    public static String getIdResEntryName(Context context, int id) {
        try {
            return context.getResources().getResourceEntryName(id);
        } catch (Throwable throwable) {
            Log.e(TAG, "getIdResEntryName", throwable);
        }
        return String.valueOf(id);
    }

    public static String getLayoutResEntryName(Context context, String pkgName, int id) {
        try {
            if (context.getPackageName().equals(pkgName)) {
                return context.getResources().getResourceEntryName(id);
            } else {
                try {
                    Class[] classes = Class.forName(String.format(STATISTICS_R_CLASS_NAME, pkgName)).getDeclaredClasses();
                    for (Class cls : classes) {
                        if ("layout".equals(cls.getSimpleName())) {
                            Object layout = cls.newInstance();
                            Field[] fields = cls.getDeclaredFields();
                            for (Field field : fields) {
                                if (field.getInt(layout) == id) {
                                    return field.getName();
                                }
                            }
                            break;
                        }
                    }
                } catch (Throwable throwable) {
                    Log.e(TAG, "getLayoutResEntryName, pkgName not equals", throwable);
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "getLayoutResEntryName", throwable);
        }
        return String.valueOf(id);
    }
}
