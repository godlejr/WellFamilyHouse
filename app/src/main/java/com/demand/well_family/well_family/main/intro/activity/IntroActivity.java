package com.demand.well_family.well_family.main.intro.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.demand.well_family.well_family.LoginActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.main.base.activity.MainActivity;
import com.demand.well_family.well_family.main.intro.presenter.IntroPresenter;
import com.demand.well_family.well_family.main.intro.presenter.impl.IntroPresenterImpl;
import com.demand.well_family.well_family.main.intro.view.IntroView;


public class IntroActivity extends Activity implements IntroView {
    private IntroPresenter introPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        introPresenter = new IntroPresenterImpl(this);
        introPresenter.onCreate();
    }

    @Override
    public void init() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                introPresenter.validateUserExist();
            }
        }, 2000);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToLoginActivity() {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToMainActivity() {
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}