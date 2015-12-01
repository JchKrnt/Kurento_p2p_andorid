package com.sohu.kurento_p2p_andorid.controller.net;

import android.os.Parcel;
import android.os.Parcelable;

import com.sohu.kurento_p2p_andorid.model.bean.UserBean;

import java.util.ArrayList;

/**
 * Created by jingbiaowang on 2015/11/18.
 */
public class UserResponseBean implements Parcelable {

    public enum UserResponseType {

        register, onList, addUser, removeUser, inComingCall, clear, error;
    }

    private UserResponseType responseType;

    private String name;

    private UserBean userBean;

    private String msg;

    private ArrayList<UserBean> listData;


    public UserResponseType getType() {
        return responseType;
    }

    public void setType(UserResponseType responseType) {
        this.responseType = responseType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<UserBean> getListData() {
        return listData;
    }

    public void setListData(ArrayList<UserBean> listData) {
        this.listData = listData;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.responseType == null ? -1 : this.responseType.ordinal());
        dest.writeString(this.name);
        dest.writeParcelable(this.userBean, 0);
        dest.writeString(this.msg);
        dest.writeTypedList(listData);
    }

    public UserResponseBean() {
    }

    protected UserResponseBean(Parcel in) {
        int tmpResponseType = in.readInt();
        this.responseType = tmpResponseType == -1 ? null : UserResponseType.values()
                [tmpResponseType];
        this.name = in.readString();
        this.userBean = in.readParcelable(UserBean.class.getClassLoader());
        this.msg = in.readString();
        this.listData = in.createTypedArrayList(UserBean.CREATOR);
    }

    public static final Parcelable.Creator<UserResponseBean> CREATOR = new Parcelable
            .Creator<UserResponseBean>() {
        public UserResponseBean createFromParcel(Parcel source) {
            return new UserResponseBean(source);
        }

        public UserResponseBean[] newArray(int size) {
            return new UserResponseBean[size];
        }
    };
}
