package com.demand.well_family.well_family.falldiagnosis.environment.result.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.falldiagnosis.environment.result.presenter.EnvironmentEvaluationResultPresenter;
import com.demand.well_family.well_family.falldiagnosis.environment.result.presenter.impl.EnvironmentEvaluationResultPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.environment.result.view.EnvironmentEvaluationResultView;
import com.demand.well_family.well_family.util.ProgressBarAnimationUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-31.
 */

public class EnvironmentEvaluationResultActivity extends Activity implements EnvironmentEvaluationResultView, View.OnClickListener {
    private EnvironmentEvaluationResultPresenter environmentEvaluationResultPresenter;

    private TextView tv_environmentevaluation_total_count;
    private TextView tv_environmentevaluation_score;
    private TextView tv_environmentevaluation_result;
    private ProgressBar pb_environmentevaluation_score;

    private Button btn_environmentevaluation_send;

    private View decorView;
    private TextView toolbarTitle;
    private Toolbar toolbar;
    private ImageView toolbarBack;

    private ProgressBarAnimationUtil progressBarAnimationUtil;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environmentevaluationresult);

        FallDiagnosisContentCategory fallDiagnosisContentCategory = new FallDiagnosisContentCategory();
        fallDiagnosisContentCategory.setName(getIntent().getStringExtra("fall_diagnosis_content_category_name"));
        fallDiagnosisContentCategory.setId(getIntent().getIntExtra("fall_diagnosis_content_category_id", 0));

        FallDiagnosisCategory fallDiagnosisCategory = new FallDiagnosisCategory();


        fallDiagnosisCategory.setId(getIntent().getIntExtra("category_id", 0));
        ArrayList<Integer> answerList = (ArrayList<Integer>) getIntent().getSerializableExtra("answerList");

        ArrayList<Uri> photoList = (ArrayList<Uri>) getIntent().getSerializableExtra("photoList");
        ArrayList<String> pathList = (ArrayList<String>) getIntent().getSerializableExtra("pathList");

        int environmentEvaluationCategorySize = getIntent().getIntExtra("environmentEvaluationCategorySize", 0);


        environmentEvaluationResultPresenter = new EnvironmentEvaluationResultPresenterImpl(this);
        environmentEvaluationResultPresenter.onCreate(fallDiagnosisCategory, fallDiagnosisContentCategory, answerList, environmentEvaluationCategorySize, photoList, pathList);

    }

    @Override
    public void init() {
        pb_environmentevaluation_score = (ProgressBar) findViewById(R.id.pb_environmentevaluation_score);
        tv_environmentevaluation_total_count = (TextView) findViewById(R.id.tv_environmentevaluation_total_count);
        tv_environmentevaluation_score = (TextView) findViewById(R.id.tv_environmentevaluation_score);
        tv_environmentevaluation_result = (TextView) findViewById(R.id.tv_environmentevaluation_result);
        btn_environmentevaluation_send = (Button) findViewById(R.id.btn_environmentevaluation_send);

        environmentEvaluationResultPresenter.onLoadData();

        btn_environmentevaluation_send.setOnClickListener(this);
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
    public void showToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    public void showTotalCount(int count) {
        String scoreSet = " / " + String.valueOf(count);
        tv_environmentevaluation_total_count.setText(scoreSet);
    }

    @Override
    public void showProgressbar(int score, int count) {
        pb_environmentevaluation_score.setMax(count);
        pb_environmentevaluation_score.setProgress(score);
        progressBarAnimationUtil = new ProgressBarAnimationUtil(pb_environmentevaluation_score, 0, score);
        progressBarAnimationUtil.setDuration(1000);
        pb_environmentevaluation_score.startAnimation(progressBarAnimationUtil);
    }

    @Override
    public void showScore(int score) {
        tv_environmentevaluation_score.setText(String.valueOf(score));
    }

    @Override
    public void showScoreTextChangeColorWithSafe() {
        tv_environmentevaluation_score.setTextColor(Color.parseColor("#05a29d"));
    }

    @Override
    public void showScoreTextChangeColorWithCaution() {
        tv_environmentevaluation_score.setTextColor(Color.parseColor("#ffa200"));
    }

    @Override
    public void showScoreTextChangeColorWithRisk() {
        tv_environmentevaluation_score.setTextColor(Color.parseColor("#d54654"));
    }

    @Override
    public void showTotalCountTextChangeColorWithSafe() {
        tv_environmentevaluation_total_count.setTextColor(Color.parseColor("#05a29d"));
    }

    @Override
    public void showTotalCountTextChangeColorWithCaution() {
        tv_environmentevaluation_total_count.setTextColor(Color.parseColor("#ffa200"));
    }

    @Override
    public void showTotalCountTextChangeColorWithRisk() {
        tv_environmentevaluation_total_count.setTextColor(Color.parseColor("#d54654"));
    }

    @Override
    public void showProgressBarChangeColorWithSafe() {
        pb_environmentevaluation_score.setProgressDrawable(getResources().getDrawable(R.drawable.fall_result_progressbar_safe));
    }

    @Override
    public void showProgressBarChangeColorWithCaution() {
        pb_environmentevaluation_score.setProgressDrawable(getResources().getDrawable(R.drawable.fall_result_progressbar_caution));
    }

    @Override
    public void showProgressBarChangeColorWithRisk() {
        pb_environmentevaluation_score.setProgressDrawable(getResources().getDrawable(R.drawable.fall_result_progressbar_risk));
    }


    @Override
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_dialog);
    }

    @Override
    public void setProgressDialog(int position) {
        progressDialog.setProgress(position);

    }

    @Override
    public void goneProgressDialog() {
        progressDialog.dismiss();
    }


    @Override
    public void showResult(String message) {
        tv_environmentevaluation_result.setText(message);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
            case R.id.btn_environmentevaluation_send:
                environmentEvaluationResultPresenter.onClickSendResult();
                break;
        }
    }
}
