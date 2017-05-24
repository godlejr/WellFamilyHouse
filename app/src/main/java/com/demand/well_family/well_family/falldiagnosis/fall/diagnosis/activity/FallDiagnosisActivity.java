package com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.adapter.FallViewPagerAdapter;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.presenter.FallDiagnosisPresenter;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.presenter.impl.FallDiagnosisPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.view.FallDiagnosisView;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public class FallDiagnosisActivity extends Activity implements FallDiagnosisView, View.OnClickListener, View.OnTouchListener {
    private FallDiagnosisPresenter fallDiagnosisPresenter;

    private View decorView;
    private TextView toolbarTitle;
    private ViewPager viewPager_diagnosisItem;
    private FallViewPagerAdapter fallViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_diagnosis);

        fallDiagnosisPresenter = new FallDiagnosisPresenterImpl(this);
        fallDiagnosisPresenter.onCreate();
    }

    @Override
    public void init() {
        viewPager_diagnosisItem = (ViewPager) findViewById(R.id.viewpager_fall);
        viewPager_diagnosisItem.setOnTouchListener(this);
        fallViewPagerAdapter = new FallViewPagerAdapter(this.getLayoutInflater(), null, fallDiagnosisPresenter);
        viewPager_diagnosisItem.setAdapter(fallViewPagerAdapter);
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
        Toast.makeText(FallDiagnosisActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setNextView(int page) {
        viewPager_diagnosisItem.setCurrentItem(page);
    }

    @Override
    public void setPreviousView(int page) {
        viewPager_diagnosisItem.setCurrentItem(page);
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
