package com.levislv.statisticssdk.plugin.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.levislv.statisticssdk.plugin.bean.StatisticsView;
import com.levislv.statisticssdk.plugin.constant.StatisticsTagConsts;

/**
 * @author levislv
 * @email  levislv@levislv.com
 * @blog   https://blog.levislv.com/
 * @github https://github.com/levislv/
 */
public class StatisticsTouchUtils {
    public static StatisticsView findStatisticsViewByXY(Activity activity, String pkgName, float x, float y) {
        int pageId = 0;
        String pageName = "null";
        View targetView = findViewByXY(activity.getWindow().getDecorView(), x, y);

        // 获取pageId和pageName
        View parentView = targetView;
        while (parentView != null) {
            Object objectPageId = parentView.getTag(StatisticsTagConsts.Page.TAG_KEY_PAGE_ID);
            Object objectPageName = parentView.getTag(StatisticsTagConsts.Page.TAG_KEY_PAGE_NAME);
            if (objectPageId instanceof Integer && objectPageName instanceof String) {
                pageId = (int) objectPageId;
                pageName = String.valueOf(objectPageName);
                break;
            } else {
                ViewParent parent = parentView.getParent();
                if (parent instanceof View) {
                    parentView = (View) parent;
                }
            }
        }

        if (pageId == 0 || "null".equals(pageName)) {
            return null;
        }
        return new StatisticsView(pkgName, pageId, pageName, targetView);
    }

    private static View findViewByXY(View view, float x, float y) {
        View targetView = null;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int index = 0; index < viewGroup.getChildCount(); index++) {
                View childView = viewGroup.getChildAt(index);
                if (!isTouchPointInView(childView, x, y)) {
                    continue;
                }
                targetView = findViewByXY(viewGroup.getChildAt(index), x, y);
                if (targetView != null) {
                    Object objectRootView = targetView.getTag(StatisticsTagConsts.Page.TAG_KEY_ROOT_VIEW);
                    if (objectRootView instanceof View && ((View) objectRootView).getVisibility() == View.VISIBLE) {
                        break;
                    }
                }
            }
        } else {
            if (!isTouchPointInView(view, x, y)) {
                return null;
            }
            return view;
        }
        return targetView;
    }

    private static boolean isTouchPointInView(View view, float x, float y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        return left >= 0 && top >= 0
                && x >= left && x <= right
                && y >= top && y <= bottom;
    }
}
