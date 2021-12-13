package com.example.recitewords.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.lifecycle.LifecycleExtKt;
import com.example.recitewords.R;

public class SplashActivity extends AppCompatActivity {
    public static final int ANIM_ITEM_DURATION = 1200;
    public static final String[] PERMISSIONS_DEBUG = {
            Manifest.permission.VIBRATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    private static final String TAG = "SplashActivity";
    private static final int REQUEST_CODE_PERMISSIONS = 1;
    private static final int REQUEST_CODE_SETTING = 2;
    private static final int REQUEST_CODE_ACTIVATION = 3;
    private boolean animationStarted;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        // 判断当前activity是不是所在任务栈的根
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
        setContentView(R.layout.activity_splash);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        if (!animationStarted) {
            animate();
            animationStarted = true;
        }
    }

    private void animate() {
        ViewPropertyAnimatorCompat logoAnimator = ViewCompat
                .animate(findViewById(R.id.ivSplashAppLogo))
                .translationY(-250)
                .setDuration(ANIM_ITEM_DURATION)
                .setInterpolator(new DecelerateInterpolator());
        logoAnimator.start();

        ViewPropertyAnimatorCompat nameAnimator = ViewCompat
                .animate(findViewById(R.id.tvSplashAppName))
                .translationY(50)
                .alpha(1)
                .setDuration(ANIM_ITEM_DURATION)
                .setInterpolator(new DecelerateInterpolator());
        nameAnimator.start();

        nameAnimator.setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View view) {
                requestPermission();
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SETTING) {
            requestPermission();
        } else if (requestCode == REQUEST_CODE_ACTIVATION) {
            if (resultCode == RESULT_OK) {
                gotoHome();
            }
        }
    }

    private MaterialDialog getMaterialDialog() {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog(this, MaterialDialog.getDEFAULT_BEHAVIOR())
                    .cancelable(false)
                    .message(null, "缺少必要权限，应用无法正常运行！", null)
                    .positiveButton(null, "去开启", materialDialog -> {
                        materialDialog.dismiss();
                        gotoSettings();
                        return null;
                    });
            LifecycleExtKt.lifecycleOwner(materialDialog, this);
        }
        return materialDialog;
    }

    private void gotoSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CODE_SETTING);
    }

    private void gotoHome() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    public void requestPermission() {
        if (hasPermissions(PERMISSIONS_DEBUG)) {
            onPermissionGranted();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_DEBUG, REQUEST_CODE_PERMISSIONS);
        }
    }

    protected void onPermissionGranted() {
        gotoHome();
    }

    private boolean hasPermissions(@NonNull String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = true;
        for (int grantResult : grantResults) {
            if (PackageManager.PERMISSION_GRANTED != grantResult) {
                granted = false;
                break;
            }
        }
        if (granted) {
            onPermissionGranted();
        } else {
            getMaterialDialog().show();
        }
    }

}