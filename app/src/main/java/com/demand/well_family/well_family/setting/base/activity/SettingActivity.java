package com.demand.well_family.well_family.setting.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.setting.deactivation.activity.DeactivationActivity;
import com.demand.well_family.well_family.setting.verifyAccount.activity.VerifyAccountActivity;
import com.demand.well_family.well_family.setting.base.presenter.SettingPresenter;
import com.demand.well_family.well_family.setting.base.presenter.impl.SettingPresenterImpl;
import com.demand.well_family.well_family.setting.base.view.SettingView;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-03-06.
 */

public class SettingActivity extends Activity implements SettingView, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private SettingPresenter settingPresenter;

    private Switch sw_setting_family;
    private LinearLayout ll_setting_deactivation;
    private LinearLayout ll_setting_pwd;
    private View decorView;

    //toolbar
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settingPresenter = new SettingPresenterImpl(this);
        settingPresenter.onCreate();
    }

    @Override
    public void init() {
        finishList.add(this);

        sw_setting_family = (Switch) findViewById(R.id.sw_setting_family);
        ll_setting_deactivation = (LinearLayout) findViewById(R.id.ll_setting_disable);
        ll_setting_pwd = (LinearLayout) findViewById(R.id.ll_setting_pwd);

        sw_setting_family.setOnCheckedChangeListener(this);
        ll_setting_deactivation.setOnClickListener(this);
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void setToolbar(View decorView) {
        toolbar = (Toolbar) decorView.findViewById(R.id.toolBar);
        toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);

        toolbar_back.setOnClickListener(this);
    }

    @Override
    public void getNotificationCheck() {
        settingPresenter.getNotificationCheck();
    }

    @Override
    public void setNotificationCheck(boolean isCheck) {
        sw_setting_family.setChecked(isCheck);
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                navigateToBack();
                break;

            case R.id.ll_setting_disable:
                navigateToDeactivationActivity();
                break;

            case R.id.ll_setting_pwd:
                navigateToResetPasswordActivity();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        settingPresenter.setNotificationCheck(isChecked);
    }

    @Override
    public void navigateToDeactivationActivity() {
        Intent intent = new Intent(SettingActivity.this, DeactivationActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToResetPasswordActivity() {
        Intent intent = new Intent(SettingActivity.this, VerifyAccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void goneResetPassword() {
        ll_setting_pwd.setVisibility(View.GONE);
    }

    @Override
    public void showResetPassword() {
        ll_setting_pwd.setVisibility(View.VISIBLE);
        ll_setting_pwd.setOnClickListener(this);
    }

    @Override
    public void navigateToBack() {
        finish();
    }
}
