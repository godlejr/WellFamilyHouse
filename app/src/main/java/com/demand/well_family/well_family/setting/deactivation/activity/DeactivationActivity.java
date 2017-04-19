package com.demand.well_family.well_family.setting.deactivation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.setting.deactivationReason.activity.DeactivationReasonActivity;
import com.demand.well_family.well_family.setting.deactivation.presenter.DeactivationPresenter;
import com.demand.well_family.well_family.setting.deactivation.presenter.impl.DeactivationPresenterImpl;
import com.demand.well_family.well_family.setting.deactivation.view.DeactivationView;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-03-06.
 */

public class DeactivationActivity extends Activity implements DeactivationView, View.OnClickListener {
    private DeactivationPresenter deactivationPresenter;

    private Button btn_account_disable;
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;

    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_disable);

        deactivationPresenter = new DeactivationPresenterImpl(this);
        deactivationPresenter.onCreate();
    }

    @Override
    public void init() {
        finishList.add(this);
        btn_account_disable = (Button) findViewById(R.id.btn_account_disable);
        btn_account_disable.setOnClickListener(this);
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
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                navigateToBack();
                break;

            case R.id.btn_account_disable:
                navigateToReasonForDeactivation();
                break;
        }
    }

    @Override
    public void navigateToReasonForDeactivation() {
        Intent intent = new Intent(DeactivationActivity.this, DeactivationReasonActivity.class);
        startActivity(intent);
        navigateToBack();
    }

    @Override
    public void navigateToBack() {
        finish();
    }
}
