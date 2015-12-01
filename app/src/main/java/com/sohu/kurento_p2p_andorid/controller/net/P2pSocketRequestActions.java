package com.sohu.kurento_p2p_andorid.controller.net;

import com.sohu.kurento.bean.IceCandidate;

/**
 * Created by jingbiaowang on 2015/11/13.
 * <p/>
 * web socket request actions.
 */
public interface P2pSocketRequestActions {

    public void userList();

    public void register(String name);

    public void call(String caller, String callee, String sdp);

    public void inComingCallResponse(String caller, String sdp, boolean callAble);

    public void onIceCandidate(IceCandidate candidate);

    public void stop();
}
