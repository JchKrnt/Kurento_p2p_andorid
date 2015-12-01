package com.sohu.kurento_p2p_andorid.model.bean;

import com.sohu.kurento.bean.IceCandidate;

import java.util.ArrayList;

/**
 * Created by jingbiaowang on 2015/11/11.
 */
public class BaseP2pSocketResponse {

    /**
     * response error code.
     */
    public enum ResponseCode {

        OK(1), ERR_EXIST(2), ERR_NULL(3), ERR_REJECT(4), ERR_OTHER(20), ERR_BUSY(21), S_CONNECT_ERRO(22);
        int value;

        ResponseCode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 网络请求类型。
     */
    public enum P2pSocketResponseType {

        removeUser(52), addUser(53), onList(54), registerResponse(55), callResponse(56),
        incomingCall(57), startCommunication(58), stopCommunication(59), iceCandidate(60);

        int value;

        P2pSocketResponseType(int codeValue) {
            this.value = codeValue;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 网络请求类型。
     */
    private P2pSocketResponseType responseType;
    /**
     * onList response data.
     */
    private ArrayList<UserBean> listData;
    /**
     * register response data.
     */
    private UserBean userBean;

    /**
     * callResonse, startCommunication data.
     */
    private String sdpAnswer;
    /**
     * iceCandiate data.
     */
    private IceCandidate iceCandidate;
    /**
     * onError code. 0 not error.
     */
    private ResponseCode responseCode;

    /**
     * onError, onMessage.
     */
    private String message;
    /**
     * caller name.
     */
    private String from;
    /**
     * callee name.
     */
    private String to;


    public P2pSocketResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(P2pSocketResponseType responseType) {
        this.responseType = responseType;
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

    public String getSdpAnswer() {
        return sdpAnswer;
    }

    public void setSdpAnswer(String sdpAnswer) {
        this.sdpAnswer = sdpAnswer;
    }

    public IceCandidate getIceCandidate() {
        return iceCandidate;
    }

    public void setIceCandidate(IceCandidate iceCandidate) {
        this.iceCandidate = iceCandidate;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}

