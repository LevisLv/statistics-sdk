package com.levislv.statisticssdk.plugin;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.levislv.statisticssdk.Statistics;
import com.levislv.statisticssdk.plugin.constant.StatisticsTagConsts;
import com.levislv.statisticssdk.util.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author levislv
 * @email  levislv@levislv.com
 * @blog   https://blog.levislv.com/
 * @github https://github.com/levislv/
 */
public class StatisticsFragmentHelper {

    private static final String TAG = StatisticsFragmentHelper.class.getSimpleName();
    private static final String TAG_PAGE = StatisticsFragmentHelper.class.getSimpleName() + "_Fragment";

    private static void setPage(Fragment fragment, String pkgName, int pageId, String pageName, String pageData) {
        try {
            View view = fragment.getView();
            if (view != null) {
                view.setTag(StatisticsTagConsts.Page.TAG_KEY_PKG_NAME, pkgName);
                view.setTag(StatisticsTagConsts.Page.TAG_KEY_PAGE_ID, pageId);
                view.setTag(StatisticsTagConsts.Page.TAG_KEY_PAGE_NAME, pageName);
                view.setTag(StatisticsTagConsts.Page.TAG_KEY_PAGE_DATA, pageData);

                if (view instanceof ViewGroup) {
                    setRootView((ViewGroup) view, view);
                }
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

    public static void onViewCreated(Fragment fragment, String pkgName, int pageId, String pageName, String pageData) {
        try {
            setPage(fragment, pkgName, pageId, pageName, pageData);
        } catch (Throwable throwable) {
            Log.e(TAG, "onViewCreated", throwable);
        }
    }

    public static void onResume(Fragment fragment, String pkgName, int pageId, String pageName, String pageData) {
        try {
            Fragment parentFragment = fragment.getParentFragment();
            if (parentFragment == null) {
                if (fragment.getUserVisibleHint() && !fragment.isHidden()) {
                    onPageEnterOrExit(fragment, pkgName, true);
                    if (fragment.getView() != null) {
                        fragment.getView().setTag(StatisticsTagConsts.Page.TAG_KEY_FOREGROUND, true);
                    }
                }
            } else {
                if (fragment.getUserVisibleHint() && !fragment.isHidden()
                        && parentFragment.getUserVisibleHint() && !parentFragment.isHidden()) {
                    onPageEnterOrExit(fragment, pkgName, true);
                    if (fragment.getView() != null) {
                        fragment.getView().setTag(StatisticsTagConsts.Page.TAG_KEY_FOREGROUND, true);
                    }
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "onResume", throwable);
        }
    }

    public static void onPause(Fragment fragment, String pkgName, int pageId, String pageName, String pageData) {
        try {
            Fragment parentFragment = fragment.getParentFragment();
            if (parentFragment == null) {
                if (fragment.getUserVisibleHint() && !fragment.isHidden()) {
                    onPageEnterOrExit(fragment, pkgName, false);
                    if (fragment.getView() != null) {
                        fragment.getView().setTag(StatisticsTagConsts.Page.TAG_KEY_FOREGROUND, false);
                    }
                }
            } else {
                if (fragment.getUserVisibleHint() && !fragment.isHidden()
                        && parentFragment.getUserVisibleHint() && !parentFragment.isHidden()) {
                    onPageEnterOrExit(fragment, pkgName, false);
                    if (fragment.getView() != null) {
                        fragment.getView().setTag(StatisticsTagConsts.Page.TAG_KEY_FOREGROUND, false);
                    }
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "onPause", throwable);
        }
    }

    public static void onDestroy(Fragment fragment, String pkgName, int pageId, String pageName, String pageData) {
        try {
            if (fragment.getView() != null) {
                Object tag = fragment.getView().getTag(StatisticsTagConsts.Page.TAG_KEY_FOREGROUND);
                if (tag instanceof Boolean && (boolean) tag) {
                    onPageEnterOrExit(fragment, pkgName, false);
                    fragment.getView().setTag(StatisticsTagConsts.Page.TAG_KEY_FOREGROUND, false);
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "onDestroy", throwable);
        }
    }

    public static void setUserVisibleHint(Fragment fragment, String pkgName, int pageId, String pageName, String pageData) {
        try {
            Fragment parentFragment = fragment.getParentFragment();
            if (parentFragment == null) {
                if (fragment.getUserVisibleHint()) {
                    if (fragment.isResumed() && !fragment.isHidden()) {
                        onPageEnterOrExit(fragment, pkgName, true);
                        if (fragment.getView() != null) {
                            fragment.getView().setTag(StatisticsTagConsts.Page.TAG_KEY_FOREGROUND, true);
                        }
                    }
                } else {
                    if (fragment.isResumed() && !fragment.isHidden()) {
                        onPageEnterOrExit(fragment, pkgName, false);
                        if (fragment.getView() != null) {
                            fragment.getView().setTag(StatisticsTagConsts.Page.TAG_KEY_FOREGROUND, false);
                        }
                    }
                }
            } else {
                if (fragment.getUserVisibleHint()) {
                    if (fragment.isResumed() && !fragment.isHidden()
                            && parentFragment.getUserVisibleHint() && parentFragment.isResumed() && !parentFragment.isHidden()) {
                        onPageEnterOrExit(fragment, pkgName, true);
                        if (fragment.getView() != null) {
                            fragment.getView().setTag(StatisticsTagConsts.Page.TAG_KEY_FOREGROUND, true);
                        }
                    }
                } else {
                    if (fragment.isResumed() && !fragment.isHidden()
                            && parentFragment.getUserVisibleHint() && parentFragment.isResumed() && !parentFragment.isHidden()) {
                        onPageEnterOrExit(fragment, pkgName, false);
                        if (fragment.getView() != null) {
                            fragment.getView().setTag(StatisticsTagConsts.Page.TAG_KEY_FOREGROUND, false);
                        }
                    }
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "setUserVisibleHint", throwable);
        }
    }

    public static void onHiddenChanged(Fragment fragment, String pkgName, int pageId, String pageName, String data) {
        try {
            Fragment parentFragment = fragment.getParentFragment();
            if (parentFragment == null) {
                if (!fragment.isHidden()) {
                    if (fragment.isResumed()) {
                        if (fragment.getUserVisibleHint()) {
                            onPageEnterOrExit(fragment, pkgName, true);
                            if (fragment.getView() != null) {
                                fragment.getView().setTag(StatisticsTagConsts.Page.TAG_KEY_FOREGROUND, true);
                            }
                        }
                    }
                } else {
                    if (fragment.isResumed()) {
                        if (fragment.getUserVisibleHint()) {
                            onPageEnterOrExit(fragment, pkgName, false);
                            if (fragment.getView() != null) {
                                fragment.getView().setTag(StatisticsTagConsts.Page.TAG_KEY_FOREGROUND, false);
                            }
                        }
                    }
                }
            } else {
                if (!fragment.isHidden()) {
                    if (fragment.isResumed() && fragment.getUserVisibleHint()
                            && parentFragment.isResumed() && parentFragment.getUserVisibleHint() && !parentFragment.isHidden()) {
                        onPageEnterOrExit(fragment, pkgName, true);
                        if (fragment.getView() != null) {
                            fragment.getView().setTag(StatisticsTagConsts.Page.TAG_KEY_FOREGROUND, true);
                        }
                    }
                } else {
                    if (fragment.isResumed() && fragment.getUserVisibleHint()
                            && parentFragment.isResumed() && parentFragment.getUserVisibleHint() && !parentFragment.isHidden()) {
                        onPageEnterOrExit(fragment, pkgName, false);
                        if (fragment.getView() != null) {
                            fragment.getView().setTag(StatisticsTagConsts.Page.TAG_KEY_FOREGROUND, false);
                        }
                    }
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "onHiddenChanged", throwable);
        }
    }

    private static void onPageEnterOrExit(Fragment fragment, String pkgName, boolean enter) {
        try {
            View view = fragment.getView();
            int pageId = (int) view.getTag(StatisticsTagConsts.Page.TAG_KEY_PAGE_ID);
            String pageName = String.valueOf(view.getTag(StatisticsTagConsts.Page.TAG_KEY_PAGE_NAME));
            String pageData = String.valueOf(view.getTag(StatisticsTagConsts.Page.TAG_KEY_PAGE_DATA));
            Map<String, String> segmentation = new HashMap<>(getSegmentation(fragment, pkgName, pageId, pageName, pageData));
            if (enter) {
                Statistics.sharedInstance().recordEvent("common_PageEnter", segmentation, 1);
                Log.e(TAG_PAGE, "common_PageEnter, page: " + fragment);
            } else {
                Statistics.sharedInstance().recordEvent("common_PageExit", segmentation, 1);
                Log.e(TAG_PAGE, "common_PageExit, page: " + fragment);
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "onPageEnterOrExit", throwable);
        }
    }

    /**
     * 封装segmentation
     *
     * @param fragment 页面
     * @param pkgName  包名
     * @param pageId   页面id
     * @param pageName 页面name
     * @param pageData 页面data
     * @return segmentation
     */
    private static Map<String, String> getSegmentation(Fragment fragment, String pkgName, int pageId, String pageName, String pageData) {
        Map<String, String> segmentation = new HashMap<>();
        try {
            // page_id
            Activity activity = fragment.getActivity();
            if (activity != null && pageId > 0) {
                segmentation.put("page_id", Utils.getLayoutResEntryName(activity, pkgName, pageId));
            }
            // page_name
            segmentation.put("page_name", pageName);
            // page_type
            if (activity != null) {
                segmentation.put("page_type", fragment.getClass().getName().substring(activity.getPackageName().length()));
            }
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
