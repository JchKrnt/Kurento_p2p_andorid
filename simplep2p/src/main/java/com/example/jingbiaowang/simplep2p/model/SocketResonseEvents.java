package com.example.jingbiaowang.simplep2p.model;

import com.sohu.kurento.bean.IceCandidate;

/**
 * Created by jingbiaowang on 2015/11/25.
 */
public class SocketResonseEvents {


    enum ResponseId {
        registerResponse, incomingCall, callResponse, startCommunication, iceCandidate, stopCommunication
    }

    public interface P2PSocketConnectEvents {
        public void onConnected();

        public void onRegister(String msg);

        public void incomingCall(String from);

        public void onError(String msg);

        public void onSocketClosed();
    }


    public interface P2pSockeInternalEvents {

        public void onCallResponeError(String response, int msgCode);

        public void onCallResponseOk(String sdpAnwser);

        public void startCommunication(String sdpAnwser, int msgCode);

        public void onIceCandidateResponse(IceCandidate candidate);

        public void stopCommunication();

        public void onError(String msg);

    }


}
