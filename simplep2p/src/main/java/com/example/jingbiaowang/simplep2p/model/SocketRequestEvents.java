package com.example.jingbiaowang.simplep2p.model;

import com.sohu.kurento.bean.IceCandidate;

/**
 * Created by jingbiaowang on 2015/11/25.
 */
public interface SocketRequestEvents {

    enum RequestType {
        register, call, incomingCallResponse, onIceCandidate, stop
    }

    public void register(String name);

    public void call(String toName, String sdp, String from);

    /**
     * @param fromName
     * @param sdpOffer
     * @param callAble 是否是接听呼叫。
     */
    public void incomingCallResponse(String fromName, String sdpOffer, boolean callAble);

    public void onIceCandidate(IceCandidate candidate);

    public void stop();
}
