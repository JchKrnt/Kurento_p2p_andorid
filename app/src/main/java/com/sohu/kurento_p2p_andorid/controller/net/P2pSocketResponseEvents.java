package com.sohu.kurento_p2p_andorid.controller.net;

import com.sohu.kurento.bean.IceCandidate;
import com.sohu.kurento_p2p_andorid.model.bean.BaseP2pSocketResponse;

/**
 * Created by jingbiaowang on 2015/11/11.
 * <p>
 * p2p socket callback events.
 */
public interface P2pSocketResponseEvents {

    public void onClose(String msg, BaseP2pSocketResponse.ResponseCode code);

    public void onOpened();

    public void onMessage(String msg);


    /**
     * error event on socket connect.
     *
     * @param errorCode
     * @param errorMsg
     */
    public void onError(BaseP2pSocketResponse.ResponseCode errorCode, String errorMsg);

    /**
     * user remote sdp.
     *
     * @param remoteSdp
     */
    public void onRemoteSdp(String remoteSdp);

    /**
     * on ice candidate interaction.
     *
     * @param iceCandidate
     */
    public void onIceCandidate(IceCandidate iceCandidate);

}
