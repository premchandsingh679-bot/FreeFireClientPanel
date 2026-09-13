package com.freefire.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int REQUEST_OVERLAY_PERMISSION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button launchPanelBtn = findViewById(R.id.launchPanelBtn);
        Button closePanelBtn = findViewById(R.id.closePanelBtn);

        launchPanelBtn.setOnClickListener(v -> {
            checkAndStartFloatingPanel();
        });

        closePanelBtn.setOnClickListener(v -> {
            stopFloatingPanel();
        });
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
        Toast.makeText(this, "Panel launched! Drag to move.", Toast.LENGTH_SHORT).show();
    }

    private void stopFloatingPanel() {
        Intent serviceIntent = new Intent(this, FloatingPanelService.class);
        stopService(serviceIntent);
        Toast.makeText(this, "Panel closed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    startFloatingPanelService();
                } else {
                    Toast.makeText(this, "Overlay permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
