package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluation;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.presenter.PhysicalEvaluationResultPresenter;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.presenter.impl.PhysicalEvaluationResultPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.view.PhysicalEvaluationResultView;
import com.demand.well_family.well_family.util.ProgressBarAnimationUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-26.
 */

public class PhysicalEvaluationResultActivity extends Activity implements PhysicalEvaluationResultView, View.OnClickListener {
    private PhysicalEvaluationResultPresenter physicalEvaluationResultPresenter;

    private TextView tv_physicalevaluation_score_total;
    private TextView tv_physicalevaluation_result;
    private TextView tv_physicalevaluation_result_leg;
    private TextView tv_physicalevaluation_result_move;
    private TextView tv_physicalevaluation_result_balance;
    private Button btn_physicalevaluation_result;
    private ProgressBar pg_physicalevaluation_score;
    private View decorView;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ImageView toolbarBack;
    private ProgressBarAnimationUtil progressBarAnimationUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physicalevaluationresult);

        FallDiagnosisCategory fallDiagnosisCategory = new FallDiagnosisCategory();
        fallDiagnosisCategory.setId(getIntent().getIntExtra("category_id", 0));
        ArrayList<PhysicalEvaluationCategory> physicalEvaluationCategoryList = (ArrayList<PhysicalEvaluationCategory>) getIntent().getSerializableExtra("physicalEvaluationCategoryList");
        ArrayList<PhysicalEvaluation> physicalEvaluationList = (ArrayList<PhysicalEvaluation>) getIntent().getSerializableExtra("physicalEvaluationList");


        physicalEvaluationResultPresenter = new PhysicalEvaluationResultPresenterImpl(this);
        physicalEvaluationResultPresenter.onCreate(fallDiagnosisCategory,physicalEvaluationCategoryList,physicalEvaluationList );
    }

    @Override
    public void init() {
        tv_physicalevaluation_score_total = (TextView) findViewById(R.id.tv_physicalevaluation_score_total);
        tv_physicalevaluation_result = (TextView) findViewById(R.id.tv_physicalevaluation_result);
        tv_physicalevaluation_result_leg = (TextView) findViewById(R.id.tv_physicalevaluation_result_leg);
        tv_physicalevaluation_result_move = (TextView) findViewById(R.id.tv_physicalevaluation_result_move);
        tv_physicalevaluation_result_balance = (TextView) findViewById(R.id.tv_physicalevaluation_result_balance);
        btn_physicalevaluation_result = (Button) findViewById(R.id.btn_physicalevaluation_result);
        pg_physicalevaluation_score = (ProgressBar) findViewById(R.id.pg_physicalevaluation_score);

        physicalEvaluationResultPresenter.onLoadData();

        btn_physicalevaluation_result.setOnClickListener(this);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(PhysicalEvaluationResultActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_physicalevaluation_result:
                physicalEvaluationResultPresenter.onClickSendResult();
                break;

            case R.id.toolbar_back:
                finish();
                break;
        }
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
    public void showToolbarTitle(String message) {
        toolbarTitle.setText(message);
    }

    @Override
    public void showProgressbar(int score, int count) {
        pg_physicalevaluation_score.setMax(count);
        pg_physicalevaluation_score.setProgress(score);
        progressBarAnimationUtil = new ProgressBarAnimationUtil(pg_physicalevaluation_score, 0, score);
        progressBarAnimationUtil.setDuration(1000);
        pg_physicalevaluation_score.startAnimation(progressBarAnimationUtil);
    }

    @Override
    public void showTotalScore(String score) {
        tv_physicalevaluation_score_total.setText(score);
    }

    @Override
    public void showTotalScoreTextChangeColorWithSafe() {
        tv_physicalevaluation_score_total.setTextColor(Color.parseColor("#05a29d"));
    }

    @Override
    public void showTotalScoreTextChangeColorWithCaution() {
        tv_physicalevaluation_score_total.setTextColor(Color.parseColor("#ffa200"));
    }

    @Override
    public void showTotalScoreTextChangeColorWithRisk() {
        tv_physicalevaluation_score_total.setTextColor(Color.parseColor("#d54654"));
    }

    @Override
    public void showProgressBarChangeColorWithSafe() {
        pg_physicalevaluation_score.setProgressDrawable(getResources().getDrawable(R.drawable.fall_result_progressbar_safe));
    }

    @Override
    public void showProgressBarChangeColorWithCaution() {
        pg_physicalevaluation_score.setProgressDrawable(getResources().getDrawable(R.drawable.fall_result_progressbar_caution));
    }

    @Override
    public void showProgressBarChangeColorWithRisk() {
        pg_physicalevaluation_score.setProgressDrawable(getResources().getDrawable(R.drawable.fall_result_progressbar_risk));
    }

    @Override
    public void showBalanceScore(String score) {
        tv_physicalevaluation_result_balance.setText(score);
    }

    @Override
    public void showMovementScore(String score) {
        tv_physicalevaluation_result_move.setText(score);
    }

    @Override
    public void showLegStrengthScore(String score) {
        tv_physicalevaluation_result_leg.setText(score);
    }

    @Override
    public void showResult(String message) {
        tv_physicalevaluation_result.setText(message);
    }

    @Override
    public void navigateToBack() {
        finish();
    }
}
