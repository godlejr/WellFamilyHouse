package com.demand.well_family.well_family.setting.agreement.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.setting.agreement.presenter.AgreementPresenter;
import com.demand.well_family.well_family.setting.agreement.presenter.impl.AgreementPresenterImpl;
import com.demand.well_family.well_family.setting.agreement.view.AgreementView;
import com.demand.well_family.well_family.setting.deactivationReason.activity.DeactivationReasonActivity;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-03-06.
 */

public class AgreementActivity extends Activity implements AgreementView, View.OnClickListener {
    private AgreementPresenter agreementPresenter;

    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private TextView agreement1;
    private TextView agreement2;

    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_agreement);

        agreementPresenter = new AgreementPresenterImpl(this);
        agreementPresenter.onCreate();
    }

    @Override
    public void init() {
        finishList.add(this);

        agreement1 = (TextView)findViewById(R.id.agreement1);
        agreement2 = (TextView)findViewById(R.id.agreement2);


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

        }
    }


    @Override
    public void navigateToBack() {
        finish();
    }
}
