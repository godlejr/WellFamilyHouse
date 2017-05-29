package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluation;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.adapter.CreatePhysicalEvaluationAdapter;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter.CreatePhysicalEvaluationPresenter;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter.impl.CreatePhysicalEvaluationPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.view.CreatePhysicalEvaluationView;
import com.demand.well_family.well_family.flag.LogFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public class CreatePhysicalEvaluationActivity extends Activity implements CreatePhysicalEvaluationView, View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private CreatePhysicalEvaluationPresenter createPhysicalEvaluationPresenter;

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private View decorView;
    private ImageView toolbarBack;

    private LinearLayout ll_createphysicalevaluation_replay_and_next;
    private LinearLayout ll_createphysicalevaluation_timer;
    private ImageView iv_createphysicalevaluation_next;
    private ImageView iv_createphysicalevaluation_start;
    private ImageView iv_createphysicalevaluation_pause;
    private ImageView iv_createphysicalevaluation_replay;
    private TextView tv_createphysicalevaluation_count_down;
    private TextView tv_createphysicalevaluation_minute;
    private TextView tv_createphysicalevaluation_second;
    private TextView tv_createphysicalevaluation_millisecond;


    private MediaPlayer mediaPlayer;

    private ViewPager vp_physicalevaluation;
    private CreatePhysicalEvaluationAdapter createPhysicalEvaluationAdapter;
    private Animation anim_slow_down;
    private Animation anim_show_up;


    private static final Logger logger = LoggerFactory.getLogger(CreatePhysicalEvaluationActivity.class);


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
        vp_physicalevaluation = (ViewPager) findViewById(R.id.vp_physicalevaluation);
        ll_createphysicalevaluation_replay_and_next = (LinearLayout) findViewById(R.id.ll_createphysicalevaluation_replay_and_next);
        ll_createphysicalevaluation_timer = (LinearLayout) findViewById(R.id.ll_createphysicalevaluation_timer);
        iv_createphysicalevaluation_start = (ImageView) findViewById(R.id.iv_createphysicalevaluation_start);
        iv_createphysicalevaluation_next = (ImageView) findViewById(R.id.iv_createphysicalevaluation_next);
        iv_createphysicalevaluation_pause = (ImageView) findViewById(R.id.iv_createphysicalevaluation_pause);
        iv_createphysicalevaluation_replay = (ImageView) findViewById(R.id.iv_createphysicalevaluation_replay);
        tv_createphysicalevaluation_count_down = (TextView) findViewById(R.id.tv_createphysicalevaluation_count_down);
        tv_createphysicalevaluation_minute = (TextView) findViewById(R.id.tv_createphysicalevaluation_minute);
        tv_createphysicalevaluation_second = (TextView) findViewById(R.id.tv_createphysicalevaluation_second);
        tv_createphysicalevaluation_millisecond = (TextView) findViewById(R.id.tv_createphysicalevaluation_millisecond);


        iv_createphysicalevaluation_start.setOnClickListener(this);
        iv_createphysicalevaluation_next.setOnClickListener(this);
        iv_createphysicalevaluation_pause.setOnClickListener(this);
        iv_createphysicalevaluation_replay.setOnClickListener(this);


        createPhysicalEvaluationPresenter.onLoadData();

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        anim_slow_down = AnimationUtils.loadAnimation(this, R.anim.anim_slow_down);
        anim_show_up  = AnimationUtils.loadAnimation(this, R.anim.anim_show_up);
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
    public void playCountDown() {
        try {
            mediaPlayer.setDataSource(getString(R.string.cloud_front_self_diagnosis_audio));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            log(e);
        }
    }

    @Override
    public void showReplayAndNextButton() {
        ll_createphysicalevaluation_replay_and_next.setVisibility(View.VISIBLE);
    }

    @Override
    public void gonewReplayAndNextButton() {
        ll_createphysicalevaluation_replay_and_next.setVisibility(View.GONE);
    }

    @Override
    public void showPauseButton() {
        iv_createphysicalevaluation_pause.setVisibility(View.VISIBLE);
        iv_createphysicalevaluation_pause.bringToFront();
    }

    @Override
    public void gonePauseButton() {
        iv_createphysicalevaluation_pause.setVisibility(View.GONE);
    }

    @Override
    public void showPlayButton() {
        iv_createphysicalevaluation_start.setVisibility(View.VISIBLE);
    }

    @Override
    public void gonePlayButton() {
        iv_createphysicalevaluation_start.setVisibility(View.GONE);
    }

    @Override
    public void showCountDown(String message) {
        tv_createphysicalevaluation_count_down.startAnimation(anim_slow_down);
        tv_createphysicalevaluation_count_down.setText(message);
    }

    @Override
    public void showCountDown() {
        tv_createphysicalevaluation_count_down.setVisibility(View.VISIBLE);
        tv_createphysicalevaluation_count_down.startAnimation(anim_show_up);

        tv_createphysicalevaluation_count_down.setText("5");
        tv_createphysicalevaluation_count_down.bringToFront();
    }

    @Override
    public void goneCountDown() {
        tv_createphysicalevaluation_count_down.setVisibility(View.GONE);
    }

    @Override
    public void showTimerLayout() {
        ll_createphysicalevaluation_timer.setVisibility(View.VISIBLE);
    }

    @Override
    public void goneTimerLayout() {
        ll_createphysicalevaluation_timer.setVisibility(View.GONE);
    }

    @Override
    public void showMinute(String minute) {
        tv_createphysicalevaluation_minute.setText(minute);
    }

    @Override
    public void showSecond(String second) {
        tv_createphysicalevaluation_second.setText(second);
    }

    @Override
    public void showMilliSecond(String milliSecond) {
        tv_createphysicalevaluation_millisecond.setText(milliSecond);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                finish();
                break;

            case R.id.iv_createphysicalevaluation_replay:
                createPhysicalEvaluationPresenter.onClickReplay();
                break;

            case R.id.iv_createphysicalevaluation_pause:
                createPhysicalEvaluationPresenter.onClickPause();
                break;

            case R.id.iv_createphysicalevaluation_start:
                createPhysicalEvaluationPresenter.onClickStart();
                break;

            case R.id.iv_createphysicalevaluation_next:
                int position = vp_physicalevaluation.getCurrentItem();
                String minute = tv_createphysicalevaluation_minute.getText().toString();
                String second = tv_createphysicalevaluation_second.getText().toString();
                String milliSecond = tv_createphysicalevaluation_millisecond.getText().toString();

                PhysicalEvaluation physicalEvaluation = new PhysicalEvaluation();
                physicalEvaluation.setMinute(Integer.parseInt(minute));
                physicalEvaluation.setSecond(Integer.parseInt(second));
                physicalEvaluation.setMillisecond(Integer.parseInt(milliSecond));

                createPhysicalEvaluationPresenter.onClickNext(physicalEvaluation, position);
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.pause();
        mp.stop();
        mp.reset();
        createPhysicalEvaluationPresenter.onCompletionCountDown();
    }


    private static void log(Throwable throwable) {
        StackTraceElement[] ste = throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if (LogFlag.printFlag) {
            if (logger.isInfoEnabled()) {
                logger.error("Exception: " + throwable.getMessage());
                logger.error(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
