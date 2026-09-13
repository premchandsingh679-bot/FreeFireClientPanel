package com.freefire.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class SettingsActivity extends Activity {

    private Switch notificationsSwitch;
    private Switch autoLaunchSwitch;
    private Spinner themeSpinner;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences("FF_Prefs", MODE_PRIVATE);

        // Initialize views
        Button backBtn = findViewById(R.id.backBtn);
        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        autoLaunchSwitch = findViewById(R.id.autoLaunchSwitch);
        themeSpinner = findViewById(R.id.themeSpinner);
        Button aboutBtn = findViewById(R.id.aboutBtn);

        // Load preferences
        loadPreferences();

        // Back button
        backBtn.setOnClickListener(v -> {
            finish();
        });

        // Notifications toggle
        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("notifications_enabled", isChecked).apply();
        });

        // Auto launch toggle
        autoLaunchSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("auto_launch_panel", isChecked).apply();
        });

        // Theme spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                android.R.array.string_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapter);
        themeSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                preferences.edit().putInt("theme", position).apply();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // About button
        aboutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private void loadPreferences() {
        boolean notificationsEnabled = preferences.getBoolean("notifications_enabled", true);
        boolean autoLaunchEnabled = preferences.getBoolean("auto_launch_panel", false);
        int theme = preferences.getInt("theme", 0);

        notificationsSwitch.setChecked(notificationsEnabled);
        autoLaunchSwitch.setChecked(autoLaunchEnabled);
        themeSpinner.setSelection(theme);
    }
}
