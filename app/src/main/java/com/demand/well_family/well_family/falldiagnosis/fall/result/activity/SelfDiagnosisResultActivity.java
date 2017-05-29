package com.demand.well_family.well_family.falldiagnosis.fall.result.activity;

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
import com.demand.well_family.well_family.falldiagnosis.fall.result.presenter.SelfDiagnosisResultPresenter;
import com.demand.well_family.well_family.falldiagnosis.fall.result.presenter.impl.SelfDiagnosisResultPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.fall.result.view.SelfDiagnosisResultView;
import com.demand.well_family.well_family.util.ProgressBarAnimationUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ1 on 2017-05-23.
 */

public class SelfDiagnosisResultActivity extends Activity implements SelfDiagnosisResultView, View.OnClickListener {
    private SelfDiagnosisResultPresenter selfDiagnosisResultPresenter;

    private View decorView;
    private TextView toolbarTitle;

    private ProgressBar pb_score;
    private TextView tv_total_count;
    private TextView tv_score;
    private TextView tv_result;
    private Button btn_send_result;

    private ProgressBarAnimationUtil progressBarAnimationUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falldiagnosisresult);


        ArrayList<Boolean> answerList = (ArrayList<Boolean>) getIntent().getSerializableExtra("answerList");

        FallDiagnosisCategory fallDiagnosisCategory = new FallDiagnosisCategory();
        fallDiagnosisCategory.setId(getIntent().getIntExtra("fall_diagnosis_category_id",0));

        selfDiagnosisResultPresenter = new SelfDiagnosisResultPresenterImpl(this);
        selfDiagnosisResultPresenter.onCreate(answerList, fallDiagnosisCategory);
    }

    @Override
    public void init() {
        pb_score = (ProgressBar) findViewById(R.id.pb_score);
        tv_total_count = (TextView) findViewById(R.id.tv_total_count);
        tv_score = (TextView) findViewById(R.id.tv_physicalevaluation_score_total);
        tv_result = (TextView) findViewById(R.id.tv_result);
        btn_send_result = (Button) findViewById(R.id.btn_send_result);

        selfDiagnosisResultPresenter.onLoadData();

        btn_send_result.setOnClickListener(this);
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
    public void showTotalCount(int count) {
        String scoreSet = " / " + String.valueOf(count);
        tv_total_count.setText(scoreSet);
    }

    @Override
    public void showProgressbar(int score, int count) {
        pb_score.setMax(count);
        pb_score.setProgress(score);
        progressBarAnimationUtil = new ProgressBarAnimationUtil(pb_score, 0, score);
        progressBarAnimationUtil.setDuration(1000);
        pb_score.startAnimation(progressBarAnimationUtil);
    }

    @Override
    public void showScore(int score) {
        tv_score.setText(String.valueOf(score));
    }

    @Override
    public void showScoreTextChangeColorWithSafe() {
        tv_score.setTextColor(Color.parseColor("#05a29d"));
    }

    @Override
    public void showScoreTextChangeColorWithCaution() {
        tv_score.setTextColor(Color.parseColor("#ffa200"));
    }

    @Override
    public void showScoreTextChangeColorWithRisk() {
        tv_score.setTextColor(Color.parseColor("#d54654"));
    }

    @Override
    public void showTotalCountTextChangeColorWithSafe() {
        tv_total_count.setTextColor(Color.parseColor("#05a29d"));
    }

    @Override
    public void showTotalCountTextChangeColorWithCaution() {
        tv_total_count.setTextColor(Color.parseColor("#ffa200"));
    }

    @Override
    public void showTotalCountTextChangeColorWithRisk() {
        tv_total_count.setTextColor(Color.parseColor("#d54654"));
    }

    @Override
    public void showProgressBarChangeColorWithSafe() {
        pb_score.setProgressDrawable(getResources().getDrawable(R.drawable.fall_result_progressbar_safe));
    }

    @Override
    public void showProgressBarChangeColorWithCaution() {
        pb_score.setProgressDrawable(getResources().getDrawable(R.drawable.fall_result_progressbar_caution));
    }

    @Override
    public void showProgressBarChangeColorWithRisk() {
        pb_score.setProgressDrawable(getResources().getDrawable(R.drawable.fall_result_progressbar_risk));
    }


    @Override
    public void showResult(String message) {
        tv_result.setText(message);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(SelfDiagnosisResultActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                navigateToBack();
                break;
            case R.id.btn_send_result:
                selfDiagnosisResultPresenter.onClickSendResult();
                break;
        }
    }
}
