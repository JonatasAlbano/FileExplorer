package com.example.jonatas.fileexplorer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.jonatas.fileexplorer.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initFragmentPreferences();
    }

    private void initFragmentPreferences() {
        SettingsFragment settingsFragment = new SettingsFragment();
        getFragmentManager().beginTransaction().replace(R.id.frame_layout_settings, settingsFragment).commit();
    }
}
