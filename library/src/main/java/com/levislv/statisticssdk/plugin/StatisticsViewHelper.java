package com.levislv.statisticssdk.plugin;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.levislv.statisticssdk.Statistics;
import com.levislv.statisticssdk.plugin.constant.StatisticsTagConsts;
import com.levislv.statisticssdk.util.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author levislv
 */
public class StatisticsViewHelper {

    private static final String TAG = StatisticsViewHelper.class.getSimpleName();

    public static void onClick(String pkgName, int pageId, String pageName, View view) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, view));
            Statistics.sharedInstance().recordEvent("common_ViewClick", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onClick", throwable);
        }
    }

    public static void onLongClick(String pkgName, int pageId, String pageName, View view) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, view));
            Statistics.sharedInstance().recordEvent("common_ViewLongClick", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onLongClick", throwable);
        }
    }

    public static void onTouch(String pkgName, int pageId, String pageName, View view) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, view));
            Object motionEvent = view.getTag(StatisticsTagConsts.View.OnTouchListener.TAG_KEY_MOTION_EVENT);
            if (motionEvent instanceof MotionEvent) {
                segmentation.put("motion_event", String.valueOf((MotionEvent) motionEvent));
            }
            Statistics.sharedInstance().recordEvent("common_ViewTouch", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onTouch", throwable);
        }
    }

    public static void onFocusChange(String pkgName, int pageId, String pageName, View view) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, view));
            Object hasFocus = view.getTag(StatisticsTagConsts.View.OnFocusChangeListener.TAG_KEY_HAS_FOCUS);
            if (hasFocus instanceof Boolean) {
                segmentation.put("has_focus", String.valueOf((boolean) hasFocus));
            }
            Statistics.sharedInstance().recordEvent("common_ViewFocusChange", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onFocusChange", throwable);
        }
    }

    public static void onEditorAction(String pkgName, int pageId, String pageName, View textView) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, textView));
            Object actionId = textView.getTag(StatisticsTagConsts.TextView.OnEditorActionListener.TAG_KEY_ACTION_ID);
            if (actionId instanceof Integer) {
                segmentation.put("action_id", String.valueOf((int) actionId));
            }
            Object keyEvent = textView.getTag(StatisticsTagConsts.TextView.OnEditorActionListener.TAG_KEY_KEY_EVENT);
            if (keyEvent instanceof KeyEvent) {
                segmentation.put("key_event", String.valueOf((KeyEvent) keyEvent));
            }
            Statistics.sharedInstance().recordEvent("common_TextViewEditorAction", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onEditorAction", throwable);
        }
    }

    public static void onCheckedChanged(String pkgName, int pageId, String pageName, View view) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, view));
            if (view instanceof CompoundButton) {
                Object isChecked = view.getTag(StatisticsTagConsts.CompoundButton.OnCheckedChangeListener.TAG_KEY_IS_CHECKED);
                if (isChecked instanceof Boolean) {
                    segmentation.put("is_checked", String.valueOf((boolean) isChecked));
                }
            } else if (view instanceof RadioGroup) {
                Object checkedId = view.getTag(StatisticsTagConsts.RadioGroup.OnCheckedChangeListener.TAG_KEY_CHECKED_ID);
                if (checkedId instanceof Integer) {
                    segmentation.put("checked_id", String.valueOf((int) checkedId));
                }
            }
            Statistics.sharedInstance().recordEvent("common_CompoundButtonCheckedChanged", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onCheckedChanged", throwable);
        }
    }

    public static void onProgressChanged(String pkgName, int pageId, String pageName, View seekBar) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, seekBar));
            Object progress = seekBar.getTag(StatisticsTagConsts.SeekBar.OnSeekBarChangeListener.TAG_KEY_PROGRESS);
            if (progress instanceof Integer) {
                segmentation.put("progress", String.valueOf((int) progress));
            }
            Object fromUser = seekBar.getTag(StatisticsTagConsts.SeekBar.OnSeekBarChangeListener.TAG_KEY_FROM_USER);
            if (fromUser instanceof Boolean) {
                segmentation.put("from_user", String.valueOf((boolean) fromUser));
            }
            Statistics.sharedInstance().recordEvent("common_SeekBarProgressChanged", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onProgressChanged", throwable);
        }
    }

    public static void onStartTrackingTouch(String pkgName, int pageId, String pageName, View seekBar) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, seekBar));
            Statistics.sharedInstance().recordEvent("common_SeekBarStartTrackingTouch", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onStartTrackingTouch", throwable);
        }
    }

    public static void onStopTrackingTouch(String pkgName, int pageId, String pageName, View seekBar) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, seekBar));
            Statistics.sharedInstance().recordEvent("common_SeekBarStopTrackingTouch", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onStopTrackingTouch", throwable);
        }
    }

    public static void onRatingChanged(String pkgName, int pageId, String pageName, View ratingBar) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, ratingBar));
            Object rating = ratingBar.getTag(StatisticsTagConsts.RatingBar.OnRatingBarChangeListener.TAG_KEY_RATING);
            if (rating instanceof Float) {
                segmentation.put("rating", String.valueOf((float) rating));
            }
            Object fromUser = ratingBar.getTag(StatisticsTagConsts.RatingBar.OnRatingBarChangeListener.TAG_KEY_FROM_USER);
            if (fromUser instanceof Boolean) {
                segmentation.put("from_user", String.valueOf((boolean) fromUser));
            }
            Statistics.sharedInstance().recordEvent("common_RatingBarRatingChanged", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onRatingChanged", throwable);
        }
    }

    public static void onItemClick(String pkgName, int pageId, String pageName, View view) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, view));
            Object parent = view.getTag(StatisticsTagConsts.AdapterView.OnItemClickListener.TAG_KEY_PARENT);
            if (parent instanceof AdapterView) {
                AdapterView adapterView = (AdapterView) parent;
                segmentation.put("parent_id", Utils.getIdResEntryName(adapterView.getContext(), adapterView.getId()));
                segmentation.put("parent_name", String.valueOf(adapterView.getContentDescription()));
            }
            Object itemPosition = view.getTag(StatisticsTagConsts.AdapterView.OnItemClickListener.TAG_KEY_ITEM_POSITION);
            if (itemPosition instanceof Integer) {
                segmentation.put("item_position", String.valueOf((int) itemPosition));
            }
            Object itemId = view.getTag(StatisticsTagConsts.AdapterView.OnItemClickListener.TAG_KEY_ITEM_ID);
            if (itemId instanceof Long) {
                segmentation.put("item_id", String.valueOf((long) itemId));
            }
            Statistics.sharedInstance().recordEvent("common_AdapterViewItemClick", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onItemClick", throwable);
        }
    }

    public static void onItemLongClick(String pkgName, int pageId, String pageName, View view) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, view));
            Object parent = view.getTag(StatisticsTagConsts.AdapterView.OnItemLongClickListener.TAG_KEY_PARENT);
            if (parent instanceof AdapterView) {
                AdapterView adapterView = (AdapterView) parent;
                segmentation.put("parent_id", Utils.getIdResEntryName(adapterView.getContext(), adapterView.getId()));
                segmentation.put("parent_name", String.valueOf(adapterView.getContentDescription()));
            }
            Object itemPosition = view.getTag(StatisticsTagConsts.AdapterView.OnItemLongClickListener.TAG_KEY_ITEM_POSITION);
            if (itemPosition instanceof Integer) {
                segmentation.put("item_position", String.valueOf((int) itemPosition));
            }
            Object itemId = view.getTag(StatisticsTagConsts.AdapterView.OnItemLongClickListener.TAG_KEY_ITEM_ID);
            if (itemId instanceof Long) {
                segmentation.put("item_id", String.valueOf((long) itemId));
            }
            Statistics.sharedInstance().recordEvent("common_AdapterViewItemLongClick", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onItemLongClick", throwable);
        }
    }

    public static void onItemSelected(String pkgName, int pageId, String pageName, View view) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, view));
            Object parent = view.getTag(StatisticsTagConsts.AdapterView.OnItemSelectedListener.TAG_KEY_PARENT);
            if (parent instanceof AdapterView) {
                AdapterView adapterView = (AdapterView) parent;
                segmentation.put("parent_id", Utils.getIdResEntryName(adapterView.getContext(), adapterView.getId()));
                segmentation.put("parent_name", String.valueOf(adapterView.getContentDescription()));
            }
            Object itemPosition = view.getTag(StatisticsTagConsts.AdapterView.OnItemSelectedListener.TAG_KEY_ITEM_POSITION);
            if (itemPosition instanceof Integer) {
                segmentation.put("item_position", String.valueOf((int) itemPosition));
            }
            Object itemId = view.getTag(StatisticsTagConsts.AdapterView.OnItemSelectedListener.TAG_KEY_ITEM_ID);
            if (itemId instanceof Long) {
                segmentation.put("item_id", String.valueOf((long) itemId));
            }
            Statistics.sharedInstance().recordEvent("common_AdapterViewItemSelected", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onItemSelected", throwable);
        }
    }

    public static void onGroupClick(String pkgName, int pageId, String pageName, View view) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, view));
            Object parent = view.getTag(StatisticsTagConsts.ExpandableListView.OnGroupClickListener.TAG_KEY_PARENT);
            if (parent instanceof ExpandableListView) {
                ExpandableListView expandableListView = (ExpandableListView) parent;
                segmentation.put("parent_id", Utils.getIdResEntryName(expandableListView.getContext(), expandableListView.getId()));
                segmentation.put("parent_name", String.valueOf(expandableListView.getContentDescription()));
            }
            Object itemPosition = view.getTag(StatisticsTagConsts.ExpandableListView.OnGroupClickListener.TAG_KEY_ITEM_POSITION);
            if (itemPosition instanceof Integer) {
                segmentation.put("item_position", String.valueOf((int) itemPosition));
            }
            Object itemId = view.getTag(StatisticsTagConsts.ExpandableListView.OnGroupClickListener.TAG_KEY_ITEM_ID);
            if (itemId instanceof Long) {
                segmentation.put("item_id", String.valueOf((long) itemId));
            }
            Statistics.sharedInstance().recordEvent("common_ExpandableListViewGroupClick", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onGroupClick", throwable);
        }
    }

    public static void onChildClick(String pkgName, int pageId, String pageName, View view) {
        try {
            Map<String, String> segmentation = new HashMap<>(getSegmentation(pkgName, pageId, pageName, view));
            Object parent = view.getTag(StatisticsTagConsts.ExpandableListView.OnChildClickListener.TAG_KEY_PARENT);
            if (parent instanceof ExpandableListView) {
                ExpandableListView expandableListView = (ExpandableListView) parent;
                segmentation.put("parent_id", Utils.getIdResEntryName(expandableListView.getContext(), expandableListView.getId()));
                segmentation.put("parent_name", String.valueOf(expandableListView.getContentDescription()));
            }
            Object itemGroupPosition = view.getTag(StatisticsTagConsts.ExpandableListView.OnChildClickListener.TAG_KEY_GROUP_POSITION);
            if (itemGroupPosition instanceof Integer) {
                segmentation.put("item_group_position", String.valueOf((int) itemGroupPosition));
            }
            Object itemChildPosition = view.getTag(StatisticsTagConsts.ExpandableListView.OnChildClickListener.TAG_KEY_CHILD_POSITION);
            if (itemChildPosition instanceof Integer) {
                segmentation.put("item_child_position", String.valueOf((int) itemChildPosition));
            }
            Object itemId = view.getTag(StatisticsTagConsts.ExpandableListView.OnChildClickListener.TAG_KEY_ITEM_ID);
            if (itemId instanceof Long) {
                segmentation.put("item_id", String.valueOf((long) itemId));
            }
            Statistics.sharedInstance().recordEvent("common_ExpandableListViewChildClick", segmentation, 1);
        } catch (Throwable throwable) {
            Log.e(TAG, "onChildClick", throwable);
        }
    }

    /**
     * 封装segmentation
     *
     * @param pkgName  包名
     * @param pageId   页面id
     * @param pageName 页面name
     * @param view     控件
     * @return segmentation
     */
    private static Map<String, String> getSegmentation(String pkgName, int pageId, String pageName, View view) {
        Map<String, String> segmentation = new HashMap<>();
        try {
            // page_id
            if (pageId > 0) {
                segmentation.put("page_id", Utils.getLayoutResEntryName(view.getContext(), pkgName, pageId));
            }
            // page_name
            segmentation.put("page_name", pageName);
            // id
            segmentation.put("id", Utils.getIdResEntryName(view.getContext(), view.getId()));
            // name
            segmentation.put("name", String.valueOf(view.getContentDescription()));
            // type
            segmentation.put("type", view.getClass().getName());
            // location
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            segmentation.put("location", location[0] + "," + location[1] + "|" + (location[0] + view.getWidth()) + "," + (location[1] + view.getHeight()));
            // text
            if (view instanceof TextView) {
                segmentation.put("text", String.valueOf(((TextView) view).getText()));
            }

            // data
            Object data = view.getTag();
            JSONObject jsonObject = null;
            if (data instanceof String) {
                jsonObject = new JSONObject((String) data);
            } else if (data instanceof JSONObject) {
                jsonObject = ((JSONObject) data);
            }
            if (jsonObject != null) {
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    segmentation.put(key, jsonObject.getString(key));
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "getSegmentation", throwable);
        }
        return segmentation;
    }
}
