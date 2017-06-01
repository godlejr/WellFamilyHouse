package com.demand.well_family.well_family.falldiagnosis.environment.create.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.falldiagnosis.environment.create.adapter.CreateEnvironmentEvaluationAdapter;
import com.demand.well_family.well_family.falldiagnosis.environment.create.presenter.CreateEnvironmentEvaluationPresenter;
import com.demand.well_family.well_family.falldiagnosis.environment.create.presenter.impl.CreateEnvironmentEvaluationPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.environment.create.view.CreateEnvironmentEvaluationView;
import com.demand.well_family.well_family.falldiagnosis.environment.photo.activity.EnvironmentEvaluationPhotoActivity;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-30.
 */

public class CreateEnvironmentEvaluationActivity extends Activity implements CreateEnvironmentEvaluationView, View.OnClickListener {
    private CreateEnvironmentEvaluationPresenter createEnvironmentEvaluationPresenter;

    private ViewPager vp_create_environment_evaluation;
    private CreateEnvironmentEvaluationAdapter createEnvironmentEvaluationAdapter;

    private View decorView;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ImageView toolbarBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_environment_evaluation);

        FallDiagnosisContentCategory fallDiagnosisContentCategory = new FallDiagnosisContentCategory();
        fallDiagnosisContentCategory.setName(getIntent().getStringExtra("fall_diagnosis_content_category_name"));
        fallDiagnosisContentCategory.setId(getIntent().getIntExtra("fall_diagnosis_content_category_id", 0));

        FallDiagnosisCategory fallDiagnosisCategory = new FallDiagnosisCategory();
        fallDiagnosisCategory.setId(getIntent().getIntExtra("category_id", 0));


        createEnvironmentEvaluationPresenter = new CreateEnvironmentEvaluationPresenterImpl(this);
        createEnvironmentEvaluationPresenter.onCreate(fallDiagnosisCategory, fallDiagnosisContentCategory);
    }

    @Override
    public void init() {
        vp_create_environment_evaluation = (ViewPager) findViewById(R.id.vp_create_environment_evaluation);
        createEnvironmentEvaluationPresenter.onLoadData();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(CreateEnvironmentEvaluationActivity.this, message, Toast.LENGTH_SHORT).show();
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
    public void showToolbar(String message) {
        toolbarTitle.setText(message);
    }

    @Override
    public void setCreateEnvironmentEvaluationAdapterInit(ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList) {
        createEnvironmentEvaluationAdapter = new CreateEnvironmentEvaluationAdapter(getLayoutInflater(), environmentEvaluationCategoryList, createEnvironmentEvaluationPresenter);
        vp_create_environment_evaluation.setAdapter(createEnvironmentEvaluationAdapter);
    }

    @Override
    public void setNextView(int position) {
        vp_create_environment_evaluation.setCurrentItem(position);
    }


    @Override
    public void navigateToEnvironmentEvaluationAPhotoActivity(FallDiagnosisCategory fallDiagnosisCategory, FallDiagnosisContentCategory fallDiagnosisContentCategory, ArrayList<Integer> answerList, int environmentEvaluationCategorySize) {
        Intent intent = new Intent(this, EnvironmentEvaluationPhotoActivity.class);

        intent.putExtra("category_id", fallDiagnosisCategory.getId());
        intent.putExtra("fall_diagnosis_content_category_name", fallDiagnosisContentCategory.getName());
        intent.putExtra("fall_diagnosis_content_category_id", fallDiagnosisContentCategory.getId());
        intent.putExtra("environmentEvaluationCategorySize", environmentEvaluationCategorySize);
        intent.putExtra("answerList", answerList);
        startActivity(intent);
        finish();
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
