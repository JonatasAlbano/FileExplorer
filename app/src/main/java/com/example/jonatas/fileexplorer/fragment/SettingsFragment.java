package com.example.jonatas.fileexplorer.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.example.jonatas.fileexplorer.R;

/**
 * Created by jonatas on 10/04/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    private SharedPreferences preferences;
    EditTextPreference editTextPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        preferences = getActivity().getSharedPreferences("com.example.jonatas.fileexplorer.PREFERENCES_KEY", Context.MODE_PRIVATE);
        editTextPreference = (EditTextPreference) findPreference("DEFAULT_FOLDER");
        editTextPreference.setText(preferences.getString("DEFAULT_FOLDER_DIR", "/"));
    }

    @Override
    public void onStop() {
        super.onStop();
        preferences.edit().putString("DEFAULT_FOLDER_DIR", editTextPreference.getText()).apply();
    }
}
