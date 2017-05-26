package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.adapter.CreatePhysicalEvaluationAdapter;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter.CreatePhysicalEvaluationPresenter;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter.impl.CreatePhysicalEvaluationPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.view.CreatePhysicalEvaluationView;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public class CreatePhysicalEvaluationActivity extends Activity implements CreatePhysicalEvaluationView, View.OnClickListener{
    private CreatePhysicalEvaluationPresenter createPhysicalEvaluationPresenter;

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private View decorView;
    private ImageView toolbarBack;

    private ImageView iv_createphysicalevaluation_next;
    private ImageView iv_createphysicalevaluation_start;

    private ViewPager vp_physicalevaluation;
    private CreatePhysicalEvaluationAdapter createPhysicalEvaluationAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createphysicalevaluation);

        FallDiagnosisCategory fallDiagnosisCategory = new FallDiagnosisCategory();
        fallDiagnosisCategory.setId(getIntent().getIntExtra("category_id", 0));

        createPhysicalEvaluationPresenter = new CreatePhysicalEvaluationPresenterImpl(this);
        createPhysicalEvaluationPresenter.onCreate(fallDiagnosisCategory);
    }

    @Override
    public void init() {
        vp_physicalevaluation = (ViewPager)findViewById(R.id.vp_physicalevaluation);
        iv_createphysicalevaluation_start = (ImageView)findViewById(R.id.iv_createphysicalevaluation_start);
        iv_createphysicalevaluation_next = (ImageView)findViewById(R.id.iv_createphysicalevaluation_next);
        iv_createphysicalevaluation_start.setOnClickListener(this);
        iv_createphysicalevaluation_next.setOnClickListener(this);


        createPhysicalEvaluationPresenter.onLoadData();
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
    public void setCreatePhysicalEvaluationAdapter(ArrayList<PhysicalEvaluationCategory> createPhysicalEvaluationList) {
        createPhysicalEvaluationAdapter = new CreatePhysicalEvaluationAdapter(CreatePhysicalEvaluationActivity.this.getLayoutInflater(), createPhysicalEvaluationList, createPhysicalEvaluationPresenter);
        vp_physicalevaluation.setAdapter(createPhysicalEvaluationAdapter);
    }

    @Override
    public void setNextView(int position) {
        vp_physicalevaluation.setCurrentItem(position + 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                finish();
                break;

            case R.id.iv_createphysicalevaluation_start:

                break;

            case R.id.iv_createphysicalevaluation_next:
                int position = vp_physicalevaluation.getCurrentItem();
                int count = createPhysicalEvaluationAdapter.getCount();

                createPhysicalEvaluationPresenter.onClickNext(position, count);
                break;
        }
    }


}
