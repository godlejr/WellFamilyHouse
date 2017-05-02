package com.demand.well_family.well_family.falldiagnosis.dangerevaluation.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Evaluation;
import com.demand.well_family.well_family.falldiagnosis.dangerevaluation.adapter.DangerEvaluationAdapter;
import com.demand.well_family.well_family.falldiagnosis.dangerevaluation.presenter.DangerEvaluationPresenter;
import com.demand.well_family.well_family.falldiagnosis.dangerevaluation.presenter.impl.DangerEvaluationPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.dangerevaluation.view.DangerEvaluationView;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public class DangerEvaluationActivity extends Activity implements DangerEvaluationView, View.OnClickListener {
    private DangerEvaluationPresenter dangerEvaluationPresenter;

    private View decorView;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView toolbar_back;
    private RecyclerView rv_danger_evaluation;
    private DangerEvaluationAdapter dangerEvaluationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_evaluation);

        dangerEvaluationPresenter = new DangerEvaluationPresenterImpl(this);
        dangerEvaluationPresenter.onCreate();
    }

    @Override
    public void init() {
        rv_danger_evaluation = (RecyclerView) findViewById(R.id.rv_danger_evaluation);
        rv_danger_evaluation.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dangerEvaluationPresenter.setDangerEvaluationList();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                finish();
                break;

        }
    }

    @Override
    public void setDangerEvaluationList(ArrayList<Evaluation> dangerEvaluationList) {
        dangerEvaluationAdapter = new DangerEvaluationAdapter(dangerEvaluationList, DangerEvaluationActivity.this);
        setDangerEvaluationAdapter(dangerEvaluationAdapter);
    }

    @Override
    public void setDangerEvaluationAdapter(DangerEvaluationAdapter dangerEvaluationAdapter) {
        rv_danger_evaluation.setAdapter(dangerEvaluationAdapter);
    }
}
