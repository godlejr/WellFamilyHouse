package com.demand.well_family.well_family.falldiagnosis.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.falldiagnosis.base.presenter.FallDiagnosisPresenter;
import com.demand.well_family.well_family.falldiagnosis.base.presenter.impl.FallDiagnosisPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.base.view.FallDiagnosisView;
import com.demand.well_family.well_family.falldiagnosis.dangerevaluation.activity.DangerEvaluationActivity;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public class FallDiagnosisActivity extends Activity implements FallDiagnosisView, View.OnClickListener {
    private FallDiagnosisPresenter fallDiagnosisPresenter;

    private LinearLayout ll_fallevaluation;
    private LinearLayout ll_physicalevaluation;
    private LinearLayout ll_dangerevaluation;

    private View decorView;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView toolbar_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_diagnosis);

        fallDiagnosisPresenter = new FallDiagnosisPresenterImpl(this);
        fallDiagnosisPresenter.onCreate();
    }

    @Override
    public void init() {
        ll_fallevaluation = (LinearLayout) findViewById(R.id.ll_fallevaluation);
        ll_physicalevaluation = (LinearLayout) findViewById(R.id.ll_physicalevaluation);
        ll_dangerevaluation = (LinearLayout) findViewById(R.id.ll_dangerevaluation);

        ll_fallevaluation.setOnClickListener(this);
        ll_physicalevaluation.setOnClickListener(this);
        ll_dangerevaluation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.ll_fallevaluation:
                navigateToFallEvaluationActivity();
                break;
            case R.id.ll_physicalevaluation:
                navigateToPhysicalEvaluationActivity();
                break;
            case R.id.ll_dangerevaluation:
                navigateToDangerActivity();
                break;
        }
    }

    @Override
    public void navigateToFallEvaluationActivity() {
//        Intent intent = new Intent(FallDiagnosisActivity.this, );
//        startActivity(intent);
    }

    @Override
    public void navigateToPhysicalEvaluationActivity() {
//        Intent intent = new Intent(FallDiagnosisActivity.this, );
//        startActivity(intent);
    }

    @Override
    public void navigateToDangerActivity() {
        Intent intent = new Intent(FallDiagnosisActivity.this, DangerEvaluationActivity.class);
        startActivity(intent);
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
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(this);
    }

    @Override
    public void showToolbarTitle(String title) {
        toolbar_title.setText(title);
    }
}
