package com.sohu.kurento_p2p_andorid.controller.net;

import android.util.Log;

import com.google.gson.Gson;
import com.sohu.kurento.bean.IceCandidate;
import com.sohu.kurento.netClient.WebSocketChannel;
import com.sohu.kurento.util.LogCat;
import com.sohu.kurento.util.LooperExecutor;
import com.sohu.kurento_p2p_andorid.model.bean.BaseP2pSocketResponse;
import com.sohu.kurento_p2p_andorid.model.bean.P2pSocketRequest;

import java.io.Closeable;
import java.io.IOException;

/**
 * <p/>
 * p2p websocket network class.
 */
public class P2pSocketClient implements WebSocketChannel.WebSocketEvents, Closeable,
        P2pSocketRequestActions {


    private static P2pSocketClient instance;
    private static WebSocketChannel socketChannel;
    Gson gson = new Gson();

    private P2pSocketResponseEvents pSocketEvents;

    private P2pSocketConnectEvents connectEvents;


    private LooperExecutor executor;

    private P2pSocketClient() {
        executor = new LooperExecutor();
        socketChannel = new WebSocketChannel();
        socketChannel.setExecutor(executor);
    }

    public static P2pSocketClient newInstance() {

        if (instance == null) {
            instance = new P2pSocketClient();
        }

        return instance;

    }

    public void connect(final String url, P2pSocketConnectEvents connectEvents) {

        executor.requestStart();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                socketChannel.connect(url, P2pSocketClient.this);
            }
        });

        this.connectEvents = connectEvents;
    }


    @Override
    public void onError(String e) {
        if (pSocketEvents != null)
            pSocketEvents.onError(BaseP2pSocketResponse.ResponseCode.S_CONNECT_ERRO, e);

        if (connectEvents != null)
            connectEvents.onConnectFailed(e);
    }

    @Override
    public void onConnected() {
        if (pSocketEvents != null)
            pSocketEvents.onOpened();
        if (connectEvents != null)
            connectEvents.onOpen();
    }

    public boolean isOPened() {
        return socketChannel.getWsc().isConnected();
    }

    @Override
    public void onMessage(String msg) {

        BaseP2pSocketResponse response = gson.fromJson(msg, BaseP2pSocketResponse.class);
        LogCat.debug("on message method -- : " + response.getResponseType().name());
        switch (response.getResponseType()) {

            case registerResponse: {
                if (connectEvents != null) {
                    if (response.getResponseCode() == BaseP2pSocketResponse.ResponseCode.OK) {
                        connectEvents.onRegister(response.getUserBean().getName());
                        LogCat.debug("register response name : " + response.getUserBean().getName());
                    } else if (response.getResponseCode() == BaseP2pSocketResponse.ResponseCode.ERR_EXIST) {
                        connectEvents.onError(response);
                    } else {
                        connectEvents.onError(response.getMessage());
                    }
                }


                break;
            }

            case onList: {
                if (connectEvents != null)

                    connectEvents.onList(response.getListData());
                LogCat.debug("onList response : " + response.getListData() == null ? "null" :
                        response.getListData().toString());

                break;
            }

            case addUser: {
                if (connectEvents != null)

                    connectEvents.onAddUser(response.getUserBean());
                LogCat.debug("addUser response--- user :　" + response.getUserBean());

                break;
            }

            case removeUser: {
                LogCat.debug("remove user response --- user : " + response.getUserBean());
                if (connectEvents != null)

                    connectEvents.onRemoveUser(response.getUserBean());

                break;
            }

            case incomingCall: {
                LogCat.v("--incomingCall case--from" + response.getFrom());
                if (connectEvents != null)

                    connectEvents.inComingCall(response.getFrom());

                break;

            }


            case callResponse: {
                LogCat.v("--callResponse case-- sdpAnswer: " + response.getSdpAnswer());
                if (pSocketEvents != null)

                    switch (response.getResponseCode()) {

                        case OK: {

                            pSocketEvents.onRemoteSdp(response.getSdpAnswer());

                            break;
                        }
                        case ERR_BUSY: {
                            pSocketEvents.onError(response.getResponseCode(), response.getMessage());
                            LogCat.v("--callResponse case--from" + response.getMessage());

                            break;
                        }
                        case ERR_REJECT: {
                            pSocketEvents.onError(response.getResponseCode(), response.getMessage());
                            LogCat.v("--callResponse case--from" + response.getMessage());
                            break;
                        }

                        case ERR_NULL: {
                            pSocketEvents.onError(response.getResponseCode(), response.getMessage());
                            LogCat.v("--callResponse case--from" + response.getMessage());
                            break;
                        }

                    }
                break;
            }

            case startCommunication: {
                LogCat.v("--startCommunication case--from" + response.getSdpAnswer());
                if (connectEvents != null)

                    pSocketEvents.onRemoteSdp(response.getSdpAnswer());

                break;
            }

            case iceCandidate: {
                LogCat.v("--iceCandidate case--from " + response.getIceCandidate().candidate);
                if (connectEvents != null)

                    pSocketEvents.onIceCandidate(response.getIceCandidate());


                break;
            }

            case stopCommunication: {
                LogCat.v("--stopCommunication case--from" + response.getMessage());
                if (connectEvents != null)

                    if (response.getResponseCode() != BaseP2pSocketResponse.ResponseCode.OK) {
                        pSocketEvents.onError(response.getResponseCode(), response.getMessage());
                    } else {
                        pSocketEvents.onClose(response.getMessage(), response.getResponseCode());
                    }

                break;
            }


            default:

        }
    }

    @Override
    public void onClosed(String msg) {

        if (pSocketEvents != null)
            pSocketEvents.onClose(msg, BaseP2pSocketResponse.ResponseCode.ERR_OTHER);
        if (connectEvents != null)
            connectEvents.onClosed(msg);
    }

    /**
     * @throws IOException
     */
    @Override
    public void close() throws IOException {

        if (!socketChannel.isClosed())
            socketChannel.disconnect(true);
        executor.requestStop();
        instance = null;
    }

    public P2pSocketResponseEvents getpSocketEvents() {
        return pSocketEvents;
    }

    public void setpSocketEvents(P2pSocketResponseEvents pSocketEvents) {
        this.pSocketEvents = pSocketEvents;
    }


    @Override
    public void userList() {

        P2pSocketRequest request = new P2pSocketRequest();
        request.setRequestType(P2pSocketRequest.SocketRequestType.userList);
        final String jsonStr = gson.toJson(request);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                socketChannel.sendMsg(jsonStr);
            }
        });
    }

    @Override
    public void register(String name) {

        P2pSocketRequest request = new P2pSocketRequest();
        request.setName(name);
        request.setRequestType(P2pSocketRequest.SocketRequestType.register);
        final String jsonStr = gson.toJson(request);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                socketChannel.sendMsg(jsonStr);
            }
        });
    }

    @Override
    public void call(String caller, String callee, String sdp) {
        P2pSocketRequest request = new P2pSocketRequest();
        request.setTo(callee);
        request.setFrom(caller);
        request.setRequestType(P2pSocketRequest.SocketRequestType.call);
        request.setSdp(sdp);

        final String jsonStr = gson.toJson(request);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                socketChannel.sendMsg(jsonStr);
            }
        });
        LogCat.v("send msg on call from : " + caller + "to : " + callee + " sdp : " + sdp);

    }

    @Override
    public void inComingCallResponse(String caller, String sdp, boolean callAble) {

        final P2pSocketRequest request = new P2pSocketRequest();
        request.setRequestType(P2pSocketRequest.SocketRequestType.incomingCallResponse);
        request.setFrom(caller);
        request.setSdp(sdp);
        request.setCallResponse(callAble);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                socketChannel.sendMsg(gson.toJson(request));
            }
        });

        LogCat.v("send msg inComingCallResponse from : " + caller + "-callAble : " + callAble + "-sdp : " + sdp);
    }

    @Override
    public void onIceCandidate(final IceCandidate candidate) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                P2pSocketRequest request = new P2pSocketRequest();
                request.setRequestType(P2pSocketRequest.SocketRequestType.onIceCandidate);
                request.setIceCandidate(candidate);
                socketChannel.sendMsg(gson.toJson(request));
            }
        });
        LogCat.v("send msg onIceCandidate candidate: " + candidate);

    }

    @Override
    public void stop() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                P2pSocketRequest request = new P2pSocketRequest();
                request.setRequestType(P2pSocketRequest.SocketRequestType.stop);
                socketChannel.sendMsg(gson.toJson(request));
            }
        });
        LogCat.v("send msg stop ");

    }
}
