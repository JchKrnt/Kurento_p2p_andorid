package com.example.jingbiaowang.simplep2p.model;

import com.sohu.kurento.bean.IceCandidate;

/**
 * Created by jingbiaowang on 2015/11/25.
 */
public class RequestBean {

    private SocketRequestEvents.RequestType id;

    private String name;
    private String to;
    private String from;

    private String sdpOffer;

    private String callResponse;

    private IceCandidate candidate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSdpOffer() {
        return sdpOffer;
    }

    public void setSdpOffer(String sdpOffer) {
        this.sdpOffer = sdpOffer;
    }

    public String getCallResponse() {
        return callResponse;
    }

    public void setCallResponse(String callResponse) {
        this.callResponse = callResponse;
    }

    public IceCandidate getCandidate() {
        return candidate;
    }

    public void setCandidate(IceCandidate candidate) {
        this.candidate = candidate;
    }

    public SocketRequestEvents.RequestType getId() {
        return id;
    }

    public void setId(SocketRequestEvents.RequestType id) {
        this.id = id;
    }
}
