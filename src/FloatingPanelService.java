package com.freefire.client;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class FloatingPanelService extends Service {

    private WindowManager windowManager;
    private View floatingPanel;
    private WindowManager.LayoutParams params;
    private static final String CHANNEL_ID = "FloatingPanel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        // Inflate floating panel layout
        floatingPanel = LayoutInflater.from(this).inflate(R.layout.floating_panel, null);

        // Setup WindowManager params
        int layoutType = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;

        params = new WindowManager.LayoutParams(
                340,  // width
                WindowManager.LayoutParams.WRAP_CONTENT,  // height
                layoutType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 50;
        params.y = 100;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingPanel, params);

        setupPanelListeners();
        startForeground();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Floating Panel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void startForeground() {
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Free Fire Client Panel")
                .setContentText("Floating panel is active")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();

        startForeground(1, notification);
    }

    private void setupPanelListeners() {
        // Close button
        Button closeBtn = floatingPanel.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(v -> {
            stopSelf();
        });

        // Switches
        Switch esSwitch = floatingPanel.findViewById(R.id.esSwitch);
        Switch aimSwitch = floatingPanel.findViewById(R.id.aimSwitch);
        Switch flSwitch = floatingPanel.findViewById(R.id.flSwitch);
        TextView statusText = floatingPanel.findViewById(R.id.statusText);

        esSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                statusText.setText("ES ON");
                statusText.setTextColor(0xFF65D88A);
            } else {
                statusText.setText("READY");
                statusText.setTextColor(0xFF65D88A);
            }
        });

        aimSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                statusText.setText("AIM ON");
                statusText.setTextColor(0xFF65D88A);
            } else {
                statusText.setText("READY");
                statusText.setTextColor(0xFF65D88A);
            }
        });

        flSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                statusText.setText("FL ON");
                statusText.setTextColor(0xFF65D88A);
            } else {
                statusText.setText("READY");
                statusText.setTextColor(0xFF65D88A);
            }
        });

        // Make panel draggable
        floatingPanel.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;
            private long touchStartTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        touchStartTime = System.currentTimeMillis();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(floatingPanel, params);
                        return true;

                    case MotionEvent.ACTION_UP:
                        long touchDuration = System.currentTimeMillis() - touchStartTime;
                        if (touchDuration < 200) {
                            return false;
                        }
                        return true;
                }
                return false;
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingPanel != null && windowManager != null) {
            windowManager.removeView(floatingPanel);
        }
    }
}
