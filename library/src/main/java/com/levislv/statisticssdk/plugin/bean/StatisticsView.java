package com.levislv.statisticssdk.plugin.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * @author LevisLv
 * @email  levislv@levislv.com
 * @blog   https://blog.levislv.com/
 * @book   https://book.levislv.com/
 * @github https://github.com/LevisLv/
 */
public class StatisticsView implements Parcelable {
    public static final Creator<StatisticsView> CREATOR = new Creator<StatisticsView>() {
        @Override
        public StatisticsView createFromParcel(Parcel in) {
            return new StatisticsView(in);
        }

        @Override
        public StatisticsView[] newArray(int size) {
            return new StatisticsView[size];
        }
    };
    private String pkgName;
    private int pageId;
    private String pageName;
    private View view;

    public StatisticsView() {
    }

    public StatisticsView(String pkgName, int pageId, String pageName, View view) {
        this.pkgName = pkgName;
        this.pageId = pageId;
        this.pageName = pageName;
        this.view = view;
    }

    protected StatisticsView(Parcel in) {
        pkgName = in.readString();
        pageId = in.readInt();
        pageName = in.readString();
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public String toString() {
        return "StatisticsView{" +
                "pkgName='" + pkgName + '\'' +
                ", pageId=" + pageId +
                ", pageName='" + pageName + '\'' +
                ", view=" + view +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pkgName);
        dest.writeInt(pageId);
        dest.writeString(pageName);
    }
}
