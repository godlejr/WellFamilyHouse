package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.adapter.PhysicalEvaluationAdapter;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.presenter.PhysicalEvaluationPresenter;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.presenter.impl.PhysicalEvaluationPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.view.PhysicalEvaluationView;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.activity.CreatePhysicalEvaluationActivity;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public class PhysicalEvaluationActivity extends Activity implements PhysicalEvaluationView, View.OnClickListener {
    private PhysicalEvaluationPresenter physicalEvaluationPresenter;

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private View decorView;
    private ImageView toolbarBack;

    private Button btn_physicalevaluation_start;
    private RecyclerView rv_physicalevaluation;
    private PhysicalEvaluationAdapter physicalEvaluationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physicalevaluation);

        physicalEvaluationPresenter = new PhysicalEvaluationPresenterImpl(this);
        physicalEvaluationPresenter.onCreate();
    }

    @Override
    public void init() {
        rv_physicalevaluation = (RecyclerView)findViewById(R.id.rv_physicalevaluation);
        btn_physicalevaluation_start = (Button)findViewById(R.id.btn_physicalevaluation_start);
        physicalEvaluationPresenter.onLoadData();
        btn_physicalevaluation_start.setOnClickListener(this);
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
        Toast.makeText(PhysicalEvaluationActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPhysicalEvaluationAdapterInit(ArrayList<String> physicalEvaluationList) {
        rv_physicalevaluation.setLayoutManager(new LinearLayoutManager(PhysicalEvaluationActivity.this, LinearLayoutManager.VERTICAL, false));
        physicalEvaluationAdapter = new PhysicalEvaluationAdapter(PhysicalEvaluationActivity.this, null, physicalEvaluationPresenter);
        rv_physicalevaluation.setAdapter(physicalEvaluationAdapter);
    }

    @Override
    public void navigateToCreatePhysicalEvaluationActivity() {
        Intent intent = new Intent(PhysicalEvaluationActivity.this, CreatePhysicalEvaluationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_back:
                finish();
                break;

            case R.id.btn_physicalevaluation_start:
                physicalEvaluationPresenter.onClickStart();
        }
    }


}
