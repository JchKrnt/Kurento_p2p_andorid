package com.sohu.kurento_p2p_andorid.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jingbiaowang on 2015/11/11.
 * <p/>
 * 用户。
 */
public class UserBean implements Parcelable {

    private String name;

    private String sessionId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.sessionId);
    }

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        this.name = in.readString();
        this.sessionId = in.readString();
    }

    public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}
