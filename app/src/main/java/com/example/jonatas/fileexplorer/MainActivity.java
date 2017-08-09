package com.example.jonatas.fileexplorer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jonatas.fileexplorer.fragment.FilesListFragment;

public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION = 0;

    private String defaultDirectory;
    private String presentDirectory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        requestStoragePermission();
        getDefaultDirectory();
        loadFragmentList(defaultDirectory);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    private boolean initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return true;
    }

    public void loadFragmentList(String directory) {
        FilesListFragment filesListFragment = FilesListFragment.newInstance(directory);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, filesListFragment).commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_action_settings:
                Intent settingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivity);
                break;
            case R.id.item_action_refresh:
                loadFragmentList(presentDirectory);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void getDefaultDirectory() {
        SharedPreferences preferences = getSharedPreferences("com.example.jonatas.fileexplorer.PREFERENCES_KEY", Context.MODE_PRIVATE);
        defaultDirectory = preferences.getString("DEFAULT_FOLDER_DIR", "/");
    }

    public void setPresentDirectory(String presentDirectory) {
        this.presentDirectory = presentDirectory;
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION);
        }
    }
}
