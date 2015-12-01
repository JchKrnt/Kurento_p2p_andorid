package com.sohu.kurento_p2p_andorid.model.bean;


import com.sohu.kurento.bean.IceCandidate;

/**
 * Created by jingbiaowang on 2015/11/12.
 */
public class P2pSocketRequest {
    public enum SocketRequestType {

        userList, register, call, incomingCallResponse, onIceCandidate, stop;

    }

    /**
     * request type.
     */
    private SocketRequestType requestType;

    private String name;

    private String from;

    private String to;

    private String sdp;

    private IceCandidate iceCandidate;

    private boolean callResponse;

    public SocketRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(SocketRequestType requestType) {
        this.requestType = requestType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSdp() {
        return sdp;
    }

    public void setSdp(String sdp) {
        this.sdp = sdp;
    }

    public IceCandidate getIceCandidate() {
        return iceCandidate;
    }

    public void setIceCandidate(IceCandidate iceCandidate) {
        this.iceCandidate = iceCandidate;
    }

    public boolean getCallResponse() {
        return callResponse;
    }

    public void setCallResponse(boolean callResponse) {
        this.callResponse = callResponse;
    }
}

