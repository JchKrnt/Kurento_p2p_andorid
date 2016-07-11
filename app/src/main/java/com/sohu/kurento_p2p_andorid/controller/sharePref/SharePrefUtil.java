package com.sohu.kurento_p2p_andorid.controller.sharePref;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;

import com.sohu.kurento.client.apprtc.PeerConnectionClient;
import com.sohu.kurento_p2p_andorid.R;

import org.webrtc.EglBase;

/**
 * Created by jingbiaowang on 2015/11/17.
 */
public class SharePrefUtil {


    private static SharedPreferences preferences;
    private static Context context;

    public static void initPrefSrc(Context context_, int prefSrc){
        context = context_;
        PreferenceManager.setDefaultValues(context, prefSrc, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getWebSocketUrl() {
        String host = preferences.getString(context.getString(R.string.pref_key_host), context.getString(R.string.pref_default_host));
        String port = preferences.getString(context.getString(R.string.pref_key_port), context.getString(R.string.pref_default_port));
        String method = preferences.getString(context.getString(R.string.pref_key_method), context.getString(R.string.pref_default_method));
        StringBuffer urlSb = new StringBuffer(host);
        urlSb.append(":").append(port).append("/").append(method);

        return urlSb.toString();
    }


    public static boolean getVedioAble() {
        return preferences.getBoolean(context.getString(R.string.pref_key_video_callable), true);
    }

//    public boolean getReportAble(){
//        return preferences.getBoolean(context.getString(R.string.pref_key_report_status), false);
//    }

    public static PeerConnectionClient.VideoCodeType getVideoCodeType() {
        return PeerConnectionClient.VideoCodeType.valueOf(preferences.getString(context.getString(R.string.pref_key_video_code), context.getString(R.string.pref_default_video_code)));
    }

    public static int getVideoFps() {
        int videoFps = 30;
        String defaultValue = context.getString(R.string.pref_default_video_frame);
        String videoFpsStr = preferences.getString(context.getString(R.string.pref_key_video_frame), defaultValue);

        String[] videoFpss = videoFpsStr.split("[ x]+");
        if (videoFpss.length == 2) {
            videoFps = Integer.parseInt(videoFpss[0]);
        }

        return videoFps;
    }

    public static Point getVideoResolution(){

        String defaultSize = context.getString(R.string.pref_default_video_resolution_value);
        String videoSizeStr = preferences.getString(context.getString(R.string.pref_key_video_resulotion),defaultSize);
        if (videoSizeStr.equals("Default")){
            videoSizeStr = defaultSize;
        }

        String[] videoSizeAry = videoSizeStr.split("\\s*x\\s*");

        Point resolutionSize = new Point(Integer.parseInt(videoSizeAry[0]), Integer.parseInt(videoSizeAry[1]));

        return resolutionSize;
    }

    public static int getVideoMaxBitrate(){

        int bitrateValue = 0;
        String bitrateDefaultValue = context.getString(R.string.pref_maxVideoBitratevalue_default);
        String bitrateDefualtType  = context.getString(R.string.pref_maxVideoBitrate_default);
        String bitrateType = preferences.getString(context.getString(R.string.pref_maxVideoBitrate_key), bitrateDefualtType);
        if (bitrateType.equals(bitrateDefualtType)){
            bitrateValue = Integer.parseInt(bitrateDefaultValue);
        }else {
            String bitrateValueStr = preferences.getString(context.getString(R.string.pref_maxVideoBitratevalue_key), bitrateDefaultValue);

            bitrateValue = Integer.parseInt(bitrateValueStr);
        }

        return bitrateValue;
    }

    public static PeerConnectionClient.AudioCodeType getAudioCodeType(){

        String audioCodeTypeDefualt = context.getString(R.string.pref_audiocodec_default);
        PeerConnectionClient.AudioCodeType audioCodeType = PeerConnectionClient.AudioCodeType.valueOf(preferences.getString(context.getString(R.string.pref_audiocodec_key), audioCodeTypeDefualt));
        return audioCodeType;
    }

    public static int getAudioMaxBitrate(){

        int bitrate = 0;
        String bitrateDefaultType = context.getString(R.string.pref_maxAudiobitrate_default);
        String bitrateDefualtStr = context.getString(R.string.pref_maxAudiobitratevalue_default);
        String bitrateType = preferences.getString(context.getString(R.string.pref_maxAudiobitrate_key), bitrateDefaultType);
        if (bitrateType.equals(bitrateDefaultType)){

            bitrate = Integer.parseInt(bitrateDefualtStr);
        }else {
            String bitrateStr = preferences.getString(context.getString(R.string.pref_maxVideoBitratevalue_key), bitrateDefualtStr);
            bitrate = Integer.parseInt(bitrateStr);
        }

        return bitrate;
    }

    public static boolean getAudioProcessEnable(){

        String audioProcessDefaultType = context.getString(R.string.pref_audioprocessing_default);
        boolean audioProcessAble = preferences.getBoolean(context.getString(R.string.pref_audioprocessing_key), Boolean.parseBoolean(audioProcessDefaultType));
        return audioProcessAble;
    }


}
