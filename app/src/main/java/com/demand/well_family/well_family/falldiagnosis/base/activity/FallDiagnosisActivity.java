package com.demand.well_family.well_family.falldiagnosis.base.activity;

import android.app.Activity;
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
import com.demand.well_family.well_family.falldiagnosis.base.adapter.FallDiagnosisCategoryAdapter;
import com.demand.well_family.well_family.falldiagnosis.base.presenter.FallDiagnosisPresenter;
import com.demand.well_family.well_family.falldiagnosis.base.presenter.impl.FallDiagnosisPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.base.view.FallDiagnosisView;
import com.demand.well_family.well_family.falldiagnosis.environment.activity.EnvironmentEvaluationActivity;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.activity.SelfDiagnosisActivity;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.activity.PhysicalEvaluationActivity;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public class FallDiagnosisActivity extends Activity implements FallDiagnosisView, View.OnClickListener {
    private FallDiagnosisPresenter fallDiagnosisPresenter;


    private View decorView;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView toolbar_back;
    private FallDiagnosisCategoryAdapter fallDiagnosisCategoryAdapter;
    private RecyclerView rv_diagnosis_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falldiagnosis);

        fallDiagnosisPresenter = new FallDiagnosisPresenterImpl(this);
        fallDiagnosisPresenter.onCreate();
    }

    @Override
    public void init() {
        rv_diagnosis_category = (RecyclerView)findViewById(R.id.rv_falldiagnosismain);
        fallDiagnosisPresenter.getDiagnosisCategory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                finish();
                break;

        }
    }

    @Override
    public void navigateToFallEvaluationActivity(FallDiagnosisCategory fallDiagnosisCategory) {
        Intent intent = new Intent(FallDiagnosisActivity.this, SelfDiagnosisActivity.class);
        intent.putExtra("category_id", fallDiagnosisCategory.getId());
        startActivity(intent);
    }

    @Override
    public void navigateToPhysicalEvaluationActivity(FallDiagnosisCategory fallDiagnosisCategory) {
        Intent intent = new Intent(FallDiagnosisActivity.this, PhysicalEvaluationActivity.class);
        intent.putExtra("category_id", fallDiagnosisCategory.getId());
        startActivity(intent);
    }

    @Override
    public void navigateToEvEvaluationActivity(FallDiagnosisCategory fallDiagnosisCategory) {
        Intent intent = new Intent(FallDiagnosisActivity.this, EnvironmentEvaluationActivity.class);
        intent.putExtra("category_id", fallDiagnosisCategory.getId());
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
    public void showMessage(String message) {
        Toast.makeText(FallDiagnosisActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setCategoryAdapterInit(ArrayList<FallDiagnosisCategory> fallDiagnosisCategoryList) {
        fallDiagnosisCategoryAdapter = new FallDiagnosisCategoryAdapter(FallDiagnosisActivity.this, fallDiagnosisCategoryList, fallDiagnosisPresenter);
        rv_diagnosis_category.setAdapter(fallDiagnosisCategoryAdapter);
        rv_diagnosis_category.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void setBackgroundColorForSelfDiagnosis(FallDiagnosisCategoryAdapter.FallDiagnosisCategoryViewHolder holder) {
        fallDiagnosisCategoryAdapter.setBackgroundColorBeige(holder);
    }

    @Override
    public void setBackgroundColorForBodyEvaluation(FallDiagnosisCategoryAdapter.FallDiagnosisCategoryViewHolder holder) {
        fallDiagnosisCategoryAdapter.setBackgroundColorBeigegray(holder);

    }

    @Override
    public void setBackgroundColorForEvEvaluation(FallDiagnosisCategoryAdapter.FallDiagnosisCategoryViewHolder holder) {
        fallDiagnosisCategoryAdapter.setBackgroundColorIndipink(holder);
    }

    @Override
    public void showToolbarTitle(String title) {
        toolbar_title.setText(title);
    }
}
