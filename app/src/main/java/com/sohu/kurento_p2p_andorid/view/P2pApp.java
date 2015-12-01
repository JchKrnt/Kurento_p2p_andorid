package com.sohu.kurento_p2p_andorid.view;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.sohu.kurento.util.LogCat;
import com.sohu.kurento_p2p_andorid.R;
import com.sohu.kurento_p2p_andorid.controller.net.P2pSocketClient;
import com.sohu.kurento_p2p_andorid.controller.net.P2pSocketConnectEvents;
import com.sohu.kurento_p2p_andorid.controller.net.UserResponseBean;
import com.sohu.kurento_p2p_andorid.controller.sharePref.SharePrefUtil;
import com.sohu.kurento_p2p_andorid.model.bean.BaseP2pSocketResponse;
import com.sohu.kurento_p2p_andorid.model.bean.UserBean;
import com.sohu.kurento_p2p_andorid.util.Constants;

import java.util.ArrayList;

/**
 * Created by jingbiaowang on 2015/11/10.
 */
public class P2pApp extends Application {

    private UserBean user;

    @Override
    public void onCreate() {
        super.onCreate();
        wsConnect();
    }

    /**
     *
     */
    public void wsConnect() {

        P2pSocketClient p2pSocketClient = P2pSocketClient.newInstance();
        p2pSocketClient.connect(SharePrefUtil.getWebSocketUrl(getApplicationContext(), R.xml
                .preferences), connectEvents);

    }

    /**
     * webSocket 链接事件监听。
     * <p>
     * Control and monitor webSocket state int the global application.
     */
    private P2pSocketConnectEvents connectEvents = new P2pSocketConnectEvents() {
        @Override
        public void onOpen() {
            LogCat.i("webSocket opened...");
            String connectUrl = "webSocket opened...";
            Toast.makeText(getApplicationContext(), connectUrl, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClosed(String msg) {
            String closeMsg = "webSocket closed..." + msg;
            LogCat.i("webSocket closed ..." + msg);

            Toast.makeText(getApplicationContext(), closeMsg, Toast.LENGTH_SHORT).show();

            clearUser();
        }

        @Override
        public void onConnectFailed(String msg) {

            String errorMsg = "webSocket connected failed ... " + msg;
            LogCat.e(errorMsg);
            Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
        }


        /**
         * socket 推送用户列表。
         *
         * @param userList
         */
        @Override
        public void onList(ArrayList<UserBean> userList) {

            Intent intent = new Intent(Constants.USER_RECEIVER_FILTER);
            UserResponseBean userOnresponse = new UserResponseBean();
            userOnresponse.setType(UserResponseBean.UserResponseType.onList);
            userOnresponse.setListData(userList);
            intent.putExtra("data", userOnresponse);

            sendBroadcast(intent);
        }

        @Override
        public void onRemoveUser(UserBean userBean) {
            Intent intent = new Intent(Constants.USER_RECEIVER_FILTER);
            UserResponseBean userOnresponse = new UserResponseBean();
            userOnresponse.setType(UserResponseBean.UserResponseType.removeUser);
            userOnresponse.setUserBean(userBean);
            intent.putExtra("data", userOnresponse);

            sendBroadcast(intent);
        }

        @Override
        public void onAddUser(UserBean userBean) {
            Intent intent = new Intent(Constants.USER_RECEIVER_FILTER);
            UserResponseBean userOnresponse = new UserResponseBean();
            userOnresponse.setType(UserResponseBean.UserResponseType.addUser);
            userOnresponse.setUserBean(userBean);
            intent.putExtra("data", userOnresponse);

            sendBroadcast(intent);
        }

        @Override
        public void onRegister(String name) {
            Intent intent = new Intent(Constants.USER_RECEIVER_FILTER);
            UserResponseBean userOnresponse = new UserResponseBean();
            userOnresponse.setType(UserResponseBean.UserResponseType.register);
            userOnresponse.setName(name);
            intent.putExtra("data", userOnresponse);

            sendBroadcast(intent);
        }

        @Override
        public void inComingCall(String from) {

            Intent intent = new Intent(Constants.USER_RECEIVER_FILTER);
            UserResponseBean userResponse = new UserResponseBean();
            userResponse.setName(from);
            userResponse.setType(UserResponseBean.UserResponseType.inComingCall);
            intent.putExtra("data", userResponse);
            sendBroadcast(intent);
        }

        @Override
        public void onError(String msg) {
            Intent intent = new Intent(Constants.USER_RECEIVER_FILTER);
            UserResponseBean userResponse = new UserResponseBean();
            userResponse.setType(UserResponseBean.UserResponseType.error);
            intent.putExtra("data", userResponse);
            sendBroadcast(intent);
        }

        @Override
        public void onError(BaseP2pSocketResponse responseBean) {

            if (responseBean.getResponseType() == BaseP2pSocketResponse.P2pSocketResponseType.registerResponse && responseBean.getResponseCode() == BaseP2pSocketResponse.ResponseCode.ERR_EXIST) {

                onRegister(responseBean.getUserBean().getName());
                Toast.makeText(getApplicationContext(), responseBean.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }
    };

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    private void clearUser() {
        user = null;
        Intent intent = new Intent(Constants.USER_RECEIVER_FILTER);
        UserResponseBean userOnresponse = new UserResponseBean();
        userOnresponse.setType(UserResponseBean.UserResponseType.clear);
        intent.putExtra("data", userOnresponse);
        sendBroadcast(intent);
    }

}
