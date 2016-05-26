package com.sohu.kurento_p2p_andorid.view;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.sohu.kurento_p2p_andorid.R;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatActivity {

	private MySettingPreferenceFragmet fragmet;
	private MySharePreferenceChangeListener sharePreferenceChangeListener = new
			MySharePreferenceChangeListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentManager fManager = getFragmentManager();
		FragmentTransaction ft = fManager.beginTransaction();
		fragmet = new MySettingPreferenceFragmet();
		ft.replace(android.R.id.content, fragmet);
		ft.commit();

	}

	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences sharedPreferences = fragmet.getPreferenceScreen().getSharedPreferences();
		sharedPreferences.registerOnSharedPreferenceChangeListener(sharePreferenceChangeListener);

		initSharePreference(sharedPreferences);
	}

	private void initSharePreference(SharedPreferences sharedPreferences) {

		updateSummary(sharedPreferences, getString(R.string.pref_key_host));
		updateSummary(sharedPreferences, getString(R.string.pref_key_port));
		updateSummary(sharedPreferences, getString(R.string.pref_key_method));
		updateSummary(sharedPreferences, getString(R.string.pref_key_stun));
		updateCheckBoxSummary(sharedPreferences, getString(R.string.pref_key_video_callable));
		updateSummary(sharedPreferences, getString(R.string.pref_key_video_code));
		updateSummary(sharedPreferences, getString(R.string.pref_key_video_frame));
		updateSummary(sharedPreferences, getString(R.string.pref_key_video_resulotion));
		updateSummary(sharedPreferences, getString(R.string.pref_maxVideoBitrate_key));
		updateSummaryBitrate(sharedPreferences, getString(R.string.pref_maxVideoBitratevalue_key));
		updateVideoBitrateEnable(sharedPreferences);

		updateSummary(sharedPreferences, getString(R.string.pref_audiocodec_key));
		updateSummary(sharedPreferences, getString(R.string.pref_maxAudiobitrate_key));
		updateSummaryBitrate(sharedPreferences, getString(R.string.pref_maxAudiobitratevalue_key));
		updateAudioBitratEnable(sharedPreferences);
		updateCheckBoxSummary(sharedPreferences, getString(R.string.pref_audioprocessing_key));

	}

	private void updateSummary(SharedPreferences sharedPreferences, String key) {

		Preference updatePref = fragmet.findPreference(key);
		updatePref.setSummary(sharedPreferences.getString(key, ""));
	}

	private void updateCheckBoxSummary(SharedPreferences sharedPreferences, String key){

		CheckBoxPreference updatePref = (CheckBoxPreference) fragmet.findPreference(key);

		updatePref.setChecked(sharedPreferences.getBoolean(key, true));

	}

	private void updateSummaryBitrate(
			SharedPreferences sharedPreferences, String key) {
		Preference updatedPref = fragmet.findPreference(key);
		updatedPref.setSummary(sharedPreferences.getString(key, "") + " kbps");
	}

	private void updateVideoBitrateEnable(SharedPreferences sharedPreferences){

		String defaultTypeValue = getString(R.string.pref_maxVideoBitrate_default);
		String typeValue = sharedPreferences.getString(getString(R.string.pref_maxVideoBitrate_key), defaultTypeValue);
		Preference bitratePref = fragmet.findPreference(getString(R.string.pref_maxVideoBitratevalue_key));

		if (defaultTypeValue.equals(typeValue)){
			bitratePref.setEnabled(false);
		}else {
			bitratePref.setEnabled(true);
		}
	}

	private void updateAudioBitratEnable(SharedPreferences sharedPreferences){
		String defaultTypeValue = getString(R.string.pref_maxAudiobitrate_default);
		String typeValue = sharedPreferences.getString(getString(R.string.pref_maxAudiobitrate_key), defaultTypeValue);
		Preference bitratePref = fragmet.findPreference(getString(R.string.pref_maxAudiobitratevalue_key));

		if (defaultTypeValue.equals(typeValue)){
			bitratePref.setEnabled(false);
		}else {
			bitratePref.setEnabled(true);
		}
	}

	@Override
	protected void onPause() {
		fragmet.getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(sharePreferenceChangeListener);
		super.onPause();

	}

	/**
	 * fragment.
	 */
	private class MySettingPreferenceFragmet extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
		}
	}


	private class MySharePreferenceChangeListener implements SharedPreferences
			.OnSharedPreferenceChangeListener {


		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

			if (key.equals(getString(R.string.pref_key_host)) ||
					key.equals(getString(R.string.pref_key_port)) ||
					key.equals(getString(R.string.pref_key_method)) ||
					key.equals(getString(R.string.pref_key_stun))||
					key.equals(getString(R.string.pref_key_video_code))||
					key.equals(getString(R.string.pref_key_video_frame))||
					key.equals(getString(R.string.pref_key_video_resulotion))||
					key.equals(getString(R.string.pref_maxVideoBitrate_key))||
					key.equals(getString(R.string.pref_audiocodec_key))||
					key.equals(getString(R.string.pref_maxAudiobitrate_key))) {
				updateSummary(sharedPreferences, key);
			}else if (key.equals(getString(R.string.pref_key_video_callable)) ||
					key.equals(getString(R.string.pref_audioprocessing_key))){
				updateCheckBoxSummary(sharedPreferences, key);
			}else if (key.equals(getString(R.string.pref_maxVideoBitratevalue_key))||
					key.equals(getString(R.string.pref_maxAudiobitratevalue_key))){
				updateSummaryBitrate(sharedPreferences, key);
			}

			if (key.equals(getString(R.string.pref_maxVideoBitrate_key))){
				updateVideoBitrateEnable(sharedPreferences);
			}

			if (key.equals(getString(R.string.pref_maxAudiobitrate_key))){
				updateAudioBitratEnable(sharedPreferences);
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		setResult(MainActivity.P2PSETTINGS_RESULTCODE);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK: {
				setResult(MainActivity.P2PSETTINGS_RESULTCODE);
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
