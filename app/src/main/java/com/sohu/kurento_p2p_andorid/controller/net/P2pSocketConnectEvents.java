package com.sohu.kurento_p2p_andorid.controller.net;

import com.sohu.kurento_p2p_andorid.model.bean.BaseP2pSocketResponse;
import com.sohu.kurento_p2p_andorid.model.bean.UserBean;

import java.util.ArrayList;

/**
 * Created by jingbiaowang on 2015/11/13.
 * <p/>
 * WebSocket connection error.
 * <p/>
 * implemented in global application.
 */
public interface P2pSocketConnectEvents {

    public void onOpen();

    public void onClosed(String msg);

    public void onConnectFailed(String msg);

    /**
     * socket 推送用户列表。
     *
     * @param userList
     */
    public void onList(ArrayList<UserBean> userList);

    public void onRemoveUser(UserBean userBean);

    public void onAddUser(UserBean userBean);

    public void onRegister(String name);

    public void inComingCall(String from);

    public void onError(String msg);

    public void onError(BaseP2pSocketResponse responseBean);


}
