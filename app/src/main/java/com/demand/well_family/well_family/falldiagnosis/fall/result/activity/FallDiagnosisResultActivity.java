package com.demand.well_family.well_family.falldiagnosis.fall.result.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.falldiagnosis.fall.result.presenter.FallDiagnosisResultPresenter;
import com.demand.well_family.well_family.falldiagnosis.fall.result.presenter.impl.FallDiagnosisResultPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.fall.result.view.FallDiagnosisResultView;

/**
 * Created by ㅇㅇ1 on 2017-05-23.
 */

public class FallDiagnosisResultActivity extends Activity implements FallDiagnosisResultView, View.OnClickListener {
    private FallDiagnosisResultPresenter fallDiagnosisResultPresenter;

    private View decorView;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_diagnosis_result);

        fallDiagnosisResultPresenter = new FallDiagnosisResultPresenterImpl(this);
        fallDiagnosisResultPresenter.onCreate();
    }

    @Override
    public void init() {

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
        Toolbar toolbar = (Toolbar) decorView.findViewById(R.id.toolBar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView toolbarBack = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbarBack.setOnClickListener(this);
    }

    @Override
    public void showToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(FallDiagnosisResultActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                finish();
                break;

        }
    }
}
