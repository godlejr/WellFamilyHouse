package com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.create.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.create.adapter.SelfDiagnosisViewPagerAdapter;
import com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.create.presenter.SelfDiagnosisPresenter;
import com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.create.presenter.impl.SelfDiagnosisPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.create.view.SelfDiagnosisView;
import com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.result.activity.SelfDiagnosisResultActivity;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public class SelfDiagnosisActivity extends Activity implements SelfDiagnosisView, View.OnClickListener, View.OnTouchListener {
    private SelfDiagnosisPresenter selfDiagnosisPresenter;

    private View decorView;
    private TextView toolbarTitle;
    private ViewPager viewPager_diagnosisCategories;
    private SelfDiagnosisViewPagerAdapter selfDiagnosisViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfdiagnosis);

        FallDiagnosisCategory fallDiagnosisCategory = new FallDiagnosisCategory();
        fallDiagnosisCategory.setId(getIntent().getIntExtra("category_id", 0));


        selfDiagnosisPresenter = new SelfDiagnosisPresenterImpl(this);
        selfDiagnosisPresenter.onCreate(fallDiagnosisCategory);
    }

    @Override
    public void init() {
        viewPager_diagnosisCategories = (ViewPager) findViewById(R.id.viewpager_fall);
        viewPager_diagnosisCategories.setOnTouchListener(this);

        selfDiagnosisPresenter.onLoadData();
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
        Toast.makeText(SelfDiagnosisActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setNextView(int position) {
        viewPager_diagnosisCategories.setCurrentItem(position);
    }

    @Override
    public void setPreviousView(int position) {
        viewPager_diagnosisCategories.setCurrentItem(position);
    }

    @Override
    public void navigateToResultActivity(FallDiagnosisCategory fallDiagnosisCategory, ArrayList<Integer> answerList, int fallDiagnosisContentCategorySize) {
        Intent intent = new Intent(SelfDiagnosisActivity.this, SelfDiagnosisResultActivity.class);
        intent.putExtra("fall_diagnosis_category_id", fallDiagnosisCategory.getId());
        intent.putExtra("answerList", answerList);
        intent.putExtra("fallDiagnosisContentCategorySize", fallDiagnosisContentCategorySize);
        startActivity(intent);
        finish();
    }

    @Override
    public void setDiagnosisCategoryAdapter(ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList) {
        selfDiagnosisViewPagerAdapter = new SelfDiagnosisViewPagerAdapter(this.getLayoutInflater(), fallDiagnosisContentCategoryList, selfDiagnosisPresenter);
        viewPager_diagnosisCategories.setAdapter(selfDiagnosisViewPagerAdapter);
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
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.viewpager_fall:
                break;
        }
        return true;
    }
}
