package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.view.PhysicalEvaluationView;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter.CreatePhysicalEvaluationPresenter;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter.impl.CreatePhysicalEvaluationPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.view.CreatePhysicalEvaluationView;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public class CreatePhysicalEvaluationActivity extends Activity implements CreatePhysicalEvaluationView, View.OnClickListener {
    private CreatePhysicalEvaluationPresenter createPhysicalEvaluationPresenter;

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private View decorView;
    private ImageView toolbarBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createphysicalevaluation);

        createPhysicalEvaluationPresenter = new CreatePhysicalEvaluationPresenterImpl(this);
        createPhysicalEvaluationPresenter.onCreate();
    }

    @Override
    public void init() {

    }

    @Override
    public void showToolbarTitle(String message) {
        toolbarTitle.setText(message);
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
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarBack = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbarBack.setOnClickListener(this);

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(CreatePhysicalEvaluationActivity.this, message, Toast.LENGTH_SHORT).show();
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
