package com.example.jingbiaowang.simplep2p.model;

import android.os.Bundle;

import com.google.gson.Gson;
import com.sohu.kurento.bean.IceCandidate;
import com.sohu.kurento.netClient.WebSocketChannel;
import com.sohu.kurento.util.LooperExecutor;

import static com.example.jingbiaowang.simplep2p.model.SocketResonseEvents.ResponseId.callResponse;

/**
 * Created by jingbiaowang on 2015/11/25.
 */
public class P2pSocketClient implements SocketRequestEvents, WebSocketChannel.WebSocketEvents {

    private LooperExecutor executor;

    private static P2pSocketClient instance;

    private Gson gson = new Gson();

    private WebSocketChannel channel;

    private SocketResonseEvents.P2PSocketConnectEvents connectEvents;

    private SocketResonseEvents.P2pSockeInternalEvents internalEvents;

    private P2pSocketClient() {
        executor = new LooperExecutor();
        channel = new WebSocketChannel();
        channel.setExecutor(executor);
        executor.requestStart();
    }

    public static P2pSocketClient newInstance() {

        if (instance == null) {

            instance = new P2pSocketClient();

        }

        return instance;
    }

    public void connect(String socketUrl) {

        channel.connect(socketUrl, this);
    }

    @Override
    public void register(String name) {

        final RequestBean requestBean = new RequestBean();
        requestBean.setName(name);
        requestBean.setId(RequestType.register);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                channel.sendMsg(gson.toJson(requestBean));
            }
        });

    }

    @Override
    public void call(String toName, String sdp, String from) {

        final RequestBean requestBean = new RequestBean();
        requestBean.setId(RequestType.call);
        requestBean.setTo(toName);
        requestBean.setSdpOffer(sdp);
        requestBean.setFrom(from);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                channel.sendMsg(gson.toJson(requestBean));
            }
        });

    }

    @Override
    public void incomingCallResponse(String fromName, String sdpOffer, boolean callAble) {

        final RequestBean requestBean = new RequestBean();
        requestBean.setId(RequestType.incomingCallResponse);
        requestBean.setFrom(fromName);
        requestBean.setSdpOffer(sdpOffer);
        if (callAble)
            requestBean.setCallResponse("accept");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                channel.sendMsg(gson.toJson(requestBean));
            }
        });

    }

    @Override
    public void onIceCandidate(IceCandidate candidate) {

        final RequestBean requestBean = new RequestBean();
        requestBean.setId(RequestType.onIceCandidate);
        requestBean.setCandidate(candidate);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                channel.sendMsg(gson.toJson(requestBean));
            }
        });

    }

    @Override
    public void stop() {

        final RequestBean requestBean = new RequestBean();
        requestBean.setId(RequestType.stop);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                channel.sendMsg(gson.toJson(requestBean));
            }
        });
    }

    //socket callBack events

    @Override
    public void onError(String e) {
        connectEvents.onError(e);
    }

    @Override
    public void onConnected() {
        connectEvents.onConnected();
    }

    @Override
    public void onClosed(String msg) {
        connectEvents.onSocketClosed();
    }

    @Override
    public void onMessage(String msg) {

        ResponseBean response = gson.fromJson(msg, ResponseBean.class);

        switch (SocketResonseEvents.ResponseId.valueOf(response.getId())) {
            case registerResponse: {

                connectEvents.onRegister(response.getResponse());

                break;
            }

            case incomingCall: {

                connectEvents.incomingCall(response.getFrom());

                break;
            }

            case callResponse: {

                int responseCode = response.getMsgCode();

                if (responseCode == 1) {
                    internalEvents.onCallResponseOk(response.getSdpAnswer());
                } else {
                    internalEvents.onCallResponeError(response.getMessage(), response.getMsgCode());
                }

                break;
            }

            case startCommunication: {

                internalEvents.startCommunication(response.getSdpAnswer(), response.getMsgCode());

                break;
            }

            case iceCandidate: {

                internalEvents.onIceCandidateResponse(response.getCandidate());
                break;
            }

            case stopCommunication: {

                internalEvents.stopCommunication();

                break;
            }
        }


    }


    public SocketResonseEvents.P2pSockeInternalEvents getInternalEvents() {
        return internalEvents;
    }

    public void setInternalEvents(SocketResonseEvents.P2pSockeInternalEvents internalEvents) {
        this.internalEvents = internalEvents;
    }

    public SocketResonseEvents.P2PSocketConnectEvents getConnectEvents() {
        return connectEvents;
    }

    public void setConnectEvents(SocketResonseEvents.P2PSocketConnectEvents connectEvents) {
        this.connectEvents = connectEvents;
    }
}


