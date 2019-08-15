package com.levislv.statisticssdk.plugin.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author levislv
 * @email  levislv@levislv.com
 * @blog   https://blog.levislv.com/
 * @github https://github.com/levislv/
 */
public class StatisticsPage implements Parcelable {
    public static final Creator<StatisticsPage> CREATOR = new Creator<StatisticsPage>() {
        @Override
        public StatisticsPage createFromParcel(Parcel in) {
            return new StatisticsPage(in);
        }

        @Override
        public StatisticsPage[] newArray(int size) {
            return new StatisticsPage[size];
        }
    };
    private int id;
    private String name;
    private String data;

    public StatisticsPage() {
    }

    public StatisticsPage(int id, String name, String data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    protected StatisticsPage(Parcel in) {
        id = in.readInt();
        name = in.readString();
        data = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StatisticsPage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(data);
    }
}
