package com.demand.well_family.well_family.falldiagnosis.environment.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.falldiagnosis.environment.base.adapter.EnvironmentEvaluationAdapter;
import com.demand.well_family.well_family.falldiagnosis.environment.base.presenter.EnvironmentEvaluationPresenter;
import com.demand.well_family.well_family.falldiagnosis.environment.base.presenter.impl.EnvironmentEvaluationPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.environment.base.view.EnvironmentEvaluationView;
import com.demand.well_family.well_family.falldiagnosis.environment.create.activity.CreateEnvironmentEvaluationActivity;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public class EnvironmentEvaluationActivity extends Activity implements EnvironmentEvaluationView, View.OnClickListener {
    private EnvironmentEvaluationPresenter dangerEvaluationPresenter;

    private View decorView;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView toolbar_back;
    private RecyclerView rv_environment_evaluation;
    private EnvironmentEvaluationAdapter environmentEvaluationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment_evaluation);

        FallDiagnosisCategory fallDiagnosisCategory = new FallDiagnosisCategory();
        fallDiagnosisCategory.setId(getIntent().getIntExtra("category_id", 0));

        dangerEvaluationPresenter = new EnvironmentEvaluationPresenterImpl(this);
        dangerEvaluationPresenter.onCreate(fallDiagnosisCategory);
    }

    @Override
    public void init() {
        rv_environment_evaluation = (RecyclerView) findViewById(R.id.rv_environment_evaluation);
        rv_environment_evaluation.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dangerEvaluationPresenter.onLoadData();
    }

    @Override
    public void setDangerEvaluationAdapterInit(ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList) {
        environmentEvaluationAdapter = new EnvironmentEvaluationAdapter(EnvironmentEvaluationActivity.this, fallDiagnosisContentCategoryList, dangerEvaluationPresenter);
        rv_environment_evaluation.setAdapter(environmentEvaluationAdapter);
    }

    @Override
    public void navigateToCreateEnvironmentEvaluationActivity(FallDiagnosisCategory fallDiagnosisCategory, FallDiagnosisContentCategory fallDiagnosisContentCategory) {
        Intent intent = new Intent(this, CreateEnvironmentEvaluationActivity.class);

        intent.putExtra("category_id", fallDiagnosisCategory.getId());
        intent.putExtra("fall_diagnosis_content_category_name", fallDiagnosisContentCategory.getName());
        intent.putExtra("fall_diagnosis_content_category_id", fallDiagnosisContentCategory.getId());
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
    public void showMessage(String message) {
        Toast.makeText(EnvironmentEvaluationActivity.this, message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                finish();
                break;

        }
    }

}
