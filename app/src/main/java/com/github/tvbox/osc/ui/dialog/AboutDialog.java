package com.github.tvbox.osc.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.tvbox.osc.BuildConfig;
import com.github.tvbox.osc.R;

import org.jetbrains.annotations.NotNull;

public class AboutDialog extends BaseDialog {

    private TextView appVersion;

    public AboutDialog(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.dialog_about);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        appVersion = findViewById(R.id.app_version);
        appVersion.setText(BuildConfig.VERSION_NAME);
    }
}