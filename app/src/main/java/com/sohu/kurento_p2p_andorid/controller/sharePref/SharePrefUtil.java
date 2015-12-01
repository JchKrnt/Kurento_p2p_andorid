package com.sohu.kurento_p2p_andorid.controller.sharePref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sohu.kurento_p2p_andorid.R;

/**
 * Created by jingbiaowang on 2015/11/17.
 */
public class SharePrefUtil {

    public static String getWebSocketUrl(Context context, int prefSrc) {

        PreferenceManager.setDefaultValues(context, prefSrc, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String host = sharedPref.getString(context.getString(R.string.pref_key_host), context.getString(R.string.pref_default_host));
        String port = sharedPref.getString(context.getString(R.string.pref_key_port), context.getString(R.string.pref_default_port));
        String method = sharedPref.getString(context.getString(R.string.pref_key_method), context.getString(R.string.pref_default_method));

        StringBuffer urlSb = new StringBuffer(host);
        urlSb.append(":").append(port).append("/").append(method);

        return urlSb.toString();
    }

}
