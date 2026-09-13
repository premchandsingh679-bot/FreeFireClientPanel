package com.freefire.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int REQUEST_OVERLAY_PERMISSION = 1001;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("FF_Prefs", MODE_PRIVATE);

        // Create notification channel
        NotificationHelper.createNotificationChannel(this);

        Button launchPanelBtn = findViewById(R.id.launchPanelBtn);
        Button closePanelBtn = findViewById(R.id.closePanelBtn);
        Button settingsBtn = findViewById(R.id.settingsBtn);

        launchPanelBtn.setOnClickListener(v -> {
            checkAndStartFloatingPanel();
        });

        closePanelBtn.setOnClickListener(v -> {
            stopFloatingPanel();
        });

        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Auto launch panel if enabled
        if (preferences.getBoolean("auto_launch_panel", false)) {
            checkAndStartFloatingPanel();
        }
    }

    private void checkAndStartFloatingPanel() {
        // Check overlay permission (Android 6.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(android.net.Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
                return;
            }
        }

        // Start floating panel service
        startFloatingPanelService();
    }

    private void startFloatingPanelService() {
        Intent serviceIntent = new Intent(this, FloatingPanelService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        Toast.makeText(this, R.string.panel_launched, Toast.LENGTH_SHORT).show();

        // Show notification if enabled
        if (preferences.getBoolean("notifications_enabled", true)) {
            NotificationHelper.showNotification(this, "Free Fire Client", "Panel launched successfully!");
        }
    }

    private void stopFloatingPanel() {
        Intent serviceIntent = new Intent(this, FloatingPanelService.class);
        stopService(serviceIntent);
        Toast.makeText(this, R.string.panel_closed, Toast.LENGTH_SHORT).show();

        // Show notification if enabled
        if (preferences.getBoolean("notifications_enabled", true)) {
            NotificationHelper.showNotification(this, "Free Fire Client", "Panel closed.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    startFloatingPanelService();
                } else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
