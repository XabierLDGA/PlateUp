package com.plateup.app;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();

        WindowCompat.setDecorFitsSystemWindows(window, true);

        window.setStatusBarColor(Color.parseColor("#FFF8F5"));
        window.setNavigationBarColor(Color.parseColor("#FFF8F5"));

        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(window, window.getDecorView());

        controller.setAppearanceLightStatusBars(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            controller.setAppearanceLightNavigationBars(true);
        }
    }
}