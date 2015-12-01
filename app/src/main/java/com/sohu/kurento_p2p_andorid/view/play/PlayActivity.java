package com.sohu.kurento_p2p_andorid.view.play;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sohu.kurento.bean.RoomBean;
import com.sohu.kurento.bean.SettingsBean;
import com.sohu.kurento.bean.UserType;
import com.sohu.kurento.client.AppRTCAudioManager;
import com.sohu.kurento.client.KWRtcSession;
import com.sohu.kurento.client.apprtc.PeerConnectionClient;
import com.sohu.kurento.netClient.KWEvent;
import com.sohu.kurento.util.LogCat;
import com.sohu.kurento_p2p_andorid.R;
import com.sohu.kurento_p2p_andorid.controller.net.P2pSocketClient;
import com.sohu.kurento_p2p_andorid.controller.net.P2pSocketResponseEvents;
import com.sohu.kurento_p2p_andorid.model.bean.BaseP2pSocketResponse;

import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RendererCommon;
import org.webrtc.SessionDescription;
import org.webrtc.StatsReport;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoRendererGui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends FragmentActivity implements PeerConnectionClient.PeerConnectionEvents, PlayFragment.OnCallEvents, P2pSocketResponseEvents {
    public static final int ERROR_CODE = 99;
    public static final String EXTRA_ROOMID =
            "org.appspot.apprtc.ROOMID";
    public static final String EXTRA_LOOPBACK =
            "org.appspot.apprtc.LOOPBACK";
    public static final String EXTRA_VIDEO_CALL =
            "org.appspot.apprtc.VIDEO_CALL";
    public static final String EXTRA_VIDEO_WIDTH =
            "org.appspot.apprtc.VIDEO_WIDTH";
    public static final String EXTRA_VIDEO_HEIGHT =
            "org.appspot.apprtc.VIDEO_HEIGHT";
    public static final String EXTRA_VIDEO_FPS =
            "org.appspot.apprtc.VIDEO_FPS";
    public static final String EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED =
            "org.appsopt.apprtc.VIDEO_CAPTUREQUALITYSLIDER";
    public static final String EXTRA_VIDEO_BITRATE =
            "org.appspot.apprtc.VIDEO_BITRATE";
    public static final String EXTRA_VIDEOCODEC =
            "org.appspot.apprtc.VIDEOCODEC";
    public static final String EXTRA_HWCODEC_ENABLED =
            "org.appspot.apprtc.HWCODEC";
    public static final String EXTRA_AUDIO_BITRATE =
            "org.appspot.apprtc.AUDIO_BITRATE";
    public static final String EXTRA_AUDIOCODEC =
            "org.appspot.apprtc.AUDIOCODEC";
    public static final String EXTRA_NOAUDIOPROCESSING_ENABLED =
            "org.appspot.apprtc.NOAUDIOPROCESSING";
    public static final String EXTRA_CPUOVERUSE_DETECTION =
            "org.appspot.apprtc.CPUOVERUSE_DETECTION";
    public static final String EXTRA_DISPLAY_HUD =
            "org.appspot.apprtc.DISPLAY_HUD";
    public static final String EXTRA_CMDLINE =
            "org.appspot.apprtc.CMDLINE";
    public static final String EXTRA_RUNTIME =
            "org.appspot.apprtc.RUNTIME";
    private static final String TAG = "CallRTCClient";

    // List of mandatory application permissions.
    private static final String[] MANDATORY_PERMISSIONS = {
            "android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.RECORD_AUDIO",
            "android.permission.INTERNET"
    };

    // Peer connection statistics callback period in ms.
    private static final int STAT_CALLBACK_PERIOD = 1000;
    // Local preview screen position before call is connected.
    private static final int LOCAL_X_CONNECTING = 0;
    private static final int LOCAL_Y_CONNECTING = 0;
    private static final int LOCAL_WIDTH_CONNECTING = 100;
    private static final int LOCAL_HEIGHT_CONNECTING = 100;
    // Local preview screen position after call is connected.
    private static final int LOCAL_X_CONNECTED = 72;
    private static final int LOCAL_Y_CONNECTED = 72;
    private static final int LOCAL_WIDTH_CONNECTED = 25;
    private static final int LOCAL_HEIGHT_CONNECTED = 25;
    // Remote video screen position
    private static final int REMOTE_X = 0;
    private static final int REMOTE_Y = 0;
    private static final int REMOTE_WIDTH = 100;
    private static final int REMOTE_HEIGHT = 100;
    //    private PeerConnectionClient peerConnectionClient = null;
    private AppRTCAudioManager audioManager = null;
    private EglBase rootEglBase;
    private RendererCommon.ScalingType scalingType;
    private Toast logToast;
    private boolean commandLineRun;
    private int runTimeMs;
    private boolean activityRunning;
    private PeerConnectionClient.PeerConnectionParameters peerConnectionParameters;

    private SurfaceViewRenderer remotevideoview;
    private PercentFrameLayout remotevideolayout;
    private SurfaceViewRenderer localvideoview;
    private PercentFrameLayout localvideolayout;
    private FrameLayout callfragmentcontainer;
    private FrameLayout hudfragmentcontainer;

    private boolean iceConnected;
    private boolean isError;
    private boolean callControlFragmentVisible = true;
    private long callStartedTimeMs = 0;
    private PeerConnectionClient peerConnectionClient = null;

    PlayFragment playFragment;
    HudFragment hudFragment;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Thread.setDefaultUncaughtExceptionHandler(
//                new UnhandledExceptionHandler(this));

        // Set window styles for fullscreen-window size. Needs to be done before
        // adding content.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_play);

        iceConnected = false;
        scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;

        initialize();
    }

    private void initialize() {

        P2pSocketClient.newInstance().setpSocketEvents(this);

        remotevideoview = (SurfaceViewRenderer) findViewById(R.id.remote_video_view);
        remotevideolayout = (PercentFrameLayout) findViewById(R.id.remote_video_layout);
        localvideoview = (SurfaceViewRenderer) findViewById(R.id.local_video_view);
        localvideolayout = (PercentFrameLayout) findViewById(R.id.local_video_layout);
        callfragmentcontainer = (FrameLayout) findViewById(R.id.call_fragment_container);
        hudfragmentcontainer = (FrameLayout) findViewById(R.id.hud_fragment_container);

        playFragment = new PlayFragment();
        hudFragment = new HudFragment();

        progressDialog = new ProgressDialog(PlayActivity.this);

        // Show/hide call control fragment on view click.
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCallControlFragmentVisibility();
            }
        };

        remotevideoview.setOnClickListener(listener);
        remotevideoview.setOnClickListener(listener);

        //Create video renders
        rootEglBase = new EglBase();
        localvideoview.init(rootEglBase.getContext(), null);
        remotevideoview.init(rootEglBase.getContext(), null);
        localvideoview.setZOrderMediaOverlay(true);
        updateVideoView();

        // Check for mandatory permissions.
        for (String permission : MANDATORY_PERMISSIONS) {
            if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                logAndToast("Permission " + permission + " is not granted");
                setResult(RESULT_CANCELED);
                finish();
                return;
            }
        }

        //TODO getIntent data.

        peerConnectionParameters = new PeerConnectionClient.PeerConnectionParameters(true, false,
                0, 0, 15, 200, PeerConnectionClient.VIDEO_CODEC_VP8, true, 0, PeerConnectionClient
                .AUDIO_CODEC_OPUS, false, true);

        commandLineRun = false;
        runTimeMs = 0;

        Intent intent = new Intent();
        //TODO set intent room id.
        intent.putExtra(EXTRA_LOOPBACK, false);
        intent.putExtra(EXTRA_VIDEO_CALL, true);
        intent.putExtra(EXTRA_VIDEO_WIDTH, 0);
        intent.putExtra(EXTRA_VIDEO_HEIGHT, 0);
        intent.putExtra(EXTRA_VIDEO_FPS, 30);
        intent.putExtra(EXTRA_VIDEO_BITRATE, 256);
        intent.putExtra(EXTRA_VIDEOCODEC, PeerConnectionClient.VIDEO_CODEC_VP8);
        intent.putExtra(EXTRA_HWCODEC_ENABLED, true);
        intent.putExtra(EXTRA_AUDIO_BITRATE, 256);
        intent.putExtra(EXTRA_AUDIOCODEC, PeerConnectionClient.AUDIO_CODEC_OPUS);
        intent.putExtra(EXTRA_NOAUDIOPROCESSING_ENABLED, false);
        intent.putExtra(EXTRA_CPUOVERUSE_DETECTION, true);
        intent.putExtra(EXTRA_CMDLINE, false);
        intent.putExtra(EXTRA_DISPLAY_HUD, true);
        intent.putExtra(EXTRA_RUNTIME, 0);
        intent.putExtra(EXTRA_ROOMID, getIntent().getStringExtra(EXTRA_ROOMID));
        intent.putExtra(EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED, true);
        // Send intent arguments to fragments.
        playFragment.setArguments(intent.getExtras());
        hudFragment.setArguments(intent.getExtras());

        // Activate call and HUD fragments and start the call.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.call_fragment_container, playFragment);
        ft.add(R.id.hud_fragment_container, hudFragment);
        ft.commit();

        // For command line execution run connection for <runTimeMs> and exit.
        if (commandLineRun && runTimeMs > 0) {
            (new Handler()).postDelayed(new Runnable() {
                public void run() {
                    disconnect();
                }
            }, runTimeMs);
        }

        peerConnectionClient = PeerConnectionClient.getInstance();
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        options.networkIgnoreMask = PeerConnectionFactory.Options.ADAPTER_TYPE_WIFI;
        peerConnectionClient.setPeerConnectionFactoryOptions(options);
        peerConnectionClient.createPeerConnectionFactory(
                PlayActivity.this, peerConnectionParameters, PlayActivity.this);

        startCall();
    }

    private List<PeerConnection.IceServer> getIces() {
        String[] stunAddresses = new String[]{
//                "220.181.90.108:3478",
//                "61.135.176.88:3478",
//                "220.181.90.110:3478",
//                "220.181.90.108:3479",
//                "61.135.176.88:3479",
//                "220.181.90.108:3478"

        };

        List<PeerConnection.IceServer> iceServers = new ArrayList<PeerConnection.IceServer>();
//dooler:123456@123.126.104.47:3478
        iceServers.add(new PeerConnection.IceServer("stun:123.126.104.237:4000"));
        iceServers.add(new PeerConnection.IceServer("turn:123.126.104.47:3478", "dooler", "123456"));
        return iceServers;
    }


    // Helper functions.
    private void toggleCallControlFragmentVisibility() {
        if (!iceConnected || !playFragment.isAdded()) {
            return;
        }
        // Show/hide call control fragment
        callControlFragmentVisible = !callControlFragmentVisible;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (callControlFragmentVisible) {
            ft.show(playFragment);
            ft.show(hudFragment);
        } else {
            ft.hide(playFragment);
            ft.hide(hudFragment);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void updateVideoView() {
        remotevideolayout.setPosition(REMOTE_X, REMOTE_Y, REMOTE_WIDTH, REMOTE_HEIGHT);
        remotevideoview.setScalingType(scalingType);
        remotevideoview.setMirror(false);

        if (iceConnected) {
            localvideolayout.setPosition(
                    LOCAL_X_CONNECTED, LOCAL_Y_CONNECTED, LOCAL_WIDTH_CONNECTED,
                    LOCAL_HEIGHT_CONNECTED);
            localvideoview.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        } else {
            localvideolayout.setPosition(
                    LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING, LOCAL_WIDTH_CONNECTING,
                    LOCAL_HEIGHT_CONNECTING);
            localvideoview.setScalingType(scalingType);
        }
        localvideoview.setMirror(true);

        localvideoview.requestLayout();
        remotevideoview.requestLayout();
    }

    private void startCall() {
        callStartedTimeMs = System.currentTimeMillis();

        // Start room connection.
        logAndToast(getString(R.string.connecting_to,
                "remote"));

        // Create and audio manager that will take care of audio routing,
        // audio modes, audio device enumeration etc.
        audioManager = AppRTCAudioManager.create(this, new Runnable() {
                    // This method will be called each time the audio state (number and
                    // type of devices) has been changed.
                    @Override
                    public void run() {
                        onAudioManagerChangedState();
                    }
                }
        );
        // Store existing audio settings and change audio mode to
        // MODE_IN_COMMUNICATION for best possible VoIP performance.
        Log.d(TAG, "Initializing the audio manager...");
        audioManager.init();

        peerConnectionClient.createPeerConnection(rootEglBase.getContext(), localvideoview,
                remotevideoview, getIces());

        peerConnectionClient.createOffer();
    }

    /**
     * Disconnect from remote resources, dispose of local resources, and exit.
     */
    private void disconnect() {
        activityRunning = false;
        LogCat.debug("-----peer client disconnect");
        if (P2pSocketClient.newInstance().isOPened()) {
            P2pSocketClient.newInstance().stop();
        }

        if (peerConnectionClient != null) {
            peerConnectionClient.close();
            peerConnectionClient = null;
        }
        if (localvideoview != null) {
            localvideoview.release();
            localvideoview = null;
        }
        if (remotevideoview != null) {
            remotevideoview.release();
            remotevideoview = null;
        }
        if (audioManager != null) {
            audioManager.close();
            audioManager = null;
        }
        if (iceConnected && !isError) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    // Log |msg| and Toast about it.
    private void logAndToast(String msg) {
        Log.d(TAG, msg);
        if (logToast != null) {
            logToast.cancel();
        }
        logToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        logToast.show();
    }

    private void disconnectWithErrorMessage(final String errorMessage) {
        if (commandLineRun || !activityRunning) {
            LogCat.e("Critical error: " + errorMessage);
            disconnect();
        } else {
            new android.app.AlertDialog.Builder(this)
                    .setTitle(getText(R.string.channel_error_title))
                    .setMessage(errorMessage)
                    .setCancelable(false)
                    .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            disconnect();
                        }
                    }).create().show();
        }
    }

    private void reportError(final String description) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isError) {
                    isError = true;
                    disconnectWithErrorMessage(description);
                }
            }
        });
    }

    private void onAudioManagerChangedState() {
        // TODO(henrika): disable video if AppRTCAudioManager.AudioDevice.EARPIECE
        // is active.
    }

    @Override
    public void onLocalDescription(final SessionDescription sdp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
                if (getIntent().getBooleanExtra("call", true)) {
                    P2pSocketClient.newInstance().call(getIntent().getStringExtra("caller"), getIntent().getStringExtra("callee"), sdp.description);
                } else {
                    P2pSocketClient.newInstance().inComingCallResponse(getIntent().getStringExtra("caller"), sdp.description, true);
                }
            }
        });

    }

    @Override
    public void onIceCandidate(final IceCandidate candidate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogCat.i("ice candidate sdp: " + candidate.sdp);
                P2pSocketClient.newInstance().onIceCandidate(new com.sohu.kurento.bean.IceCandidate(candidate.sdp, candidate.sdpMid, candidate.sdpMLineIndex));
            }
        });

    }

    @Override
    public void onIceConnected() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                iceConnected = true;
                callConnected();
            }
        });

    }

    private void callConnected() {

        if (peerConnectionClient == null || isError) {
            Log.w(TAG, "Call is connected in closed or error state");
            return;
        }
        // Update video view.
        updateVideoView();
        // Enable statistics callback.
        peerConnectionClient.enableStatsEvents(true, STAT_CALLBACK_PERIOD);
    }

    @Override
    public void onIceDisconnected() {

        LogCat.debug("---ice disconnected");
        iceConnected = false;
        disconnect();
    }

    @Override
    public void onPeerConnectionClosed() {

    }

    @Override
    public void onPeerConnectionStatsReady(final StatsReport[] reports) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isError && iceConnected) {
                    hudFragment.updateEncoderStatistics(reports);
                }
            }
        });
    }

    @Override
    public void onPeerConnectionError(String description) {

        reportError(description);
    }

    @Override
    public void onCallHangUp() {

        disconnect();

    }

    @Override
    public void onCameraSwitch() {
        if (peerConnectionClient != null) {
            peerConnectionClient.switchCamera();
        }
    }

    @Override
    public void onVideoScalingSwitch(RendererCommon.ScalingType scalingType) {
        this.scalingType = scalingType;
        updateVideoView();
    }

    @Override
    public void onCaptureFormatChange(int width, int height, int framerate) {
        if (peerConnectionClient != null) {
            peerConnectionClient.changeCaptureFormat(width, height, framerate);
        }
    }

    @Override
    public void onClose(String msg, BaseP2pSocketResponse.ResponseCode code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogCat.v("---stop by server.");
                disconnect();
            }
        });

    }

    @Override
    public void onOpened() {

    }


    @Override
    public void onMessage(String msg) {

    }

    @Override
    public void onError(BaseP2pSocketResponse.ResponseCode errorCode, final String errorMsg) {
        reportError(errorMsg);
    }

    @Override
    public void onRemoteSdp(final String remoteSdp) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                peerConnectionClient.setRemoteDescription(new SessionDescription(SessionDescription.Type.ANSWER, remoteSdp));
            }
        });
    }

    /**
     * webSocket event.
     *
     * @param iceCandidate
     */
    @Override
    public void onIceCandidate(final com.sohu.kurento.bean.IceCandidate iceCandidate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogCat.i("ice candidate from server : " + iceCandidate.candidate);
                peerConnectionClient.addRemoteIceCandidate(new IceCandidate(iceCandidate.sdpMid, iceCandidate.sdpMLineIndex, iceCandidate.candidate));
            }
        });
    }

    @Override
    protected void onDestroy() {
        P2pSocketClient.newInstance().setpSocketEvents(null);
        super.onDestroy();
    }


}
