package com.demand.well_family.well_family.exercise.create.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.exercise.activity.ExercisePopupActivity;
import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.exercise.create.presenter.CreateExercisePresenter;
import com.demand.well_family.well_family.exercise.create.presenter.impl.CreateExercisePresenterImpl;
import com.demand.well_family.well_family.exercise.create.view.CreateExerciseView;

import java.io.IOException;

/**
 * Created by ㅇㅇ on 2017-06-26.
 */

public class CreateExerciseActivity extends Activity implements CreateExerciseView, View.OnClickListener, MediaPlayer.OnPreparedListener, SurfaceHolder.Callback, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {
    private CreateExercisePresenter createExercisePresenter;


    private SurfaceHolder surfaceHolder;
    private SurfaceView sv_exercise;
    private ImageButton ib_exercise;
    private MediaPlayer mediaPlayer;
    private SeekBar sb_exercise;

    private View decorView;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView toolbar_back;
    private LinearLayout ll_exercise;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createexercise);

        ExerciseCategory exerciseCategory = (ExerciseCategory) getIntent().getSerializableExtra("exerciseCategory");

        createExercisePresenter = new CreateExercisePresenterImpl(this);
        createExercisePresenter.onCreate(exerciseCategory);
    }

    @Override
    public void init() {
        sv_exercise = (SurfaceView) findViewById(R.id.sv_exercise);
        ib_exercise = (ImageButton) findViewById(R.id.ib_exercise);
        sb_exercise = (SeekBar) findViewById(R.id.sb_exercise);
        ll_exercise = (LinearLayout) findViewById(R.id.ll_exercise);

        surfaceHolder = sv_exercise.getHolder();
        ll_exercise.bringToFront();
        sb_exercise.setEnabled(false);
        surfaceHolder.addCallback(this);
        ib_exercise.setOnClickListener(this);
        sb_exercise.setOnSeekBarChangeListener(this);
    }

    @Override
    public void setVideoItem(String video) throws IOException {
        String url = getResources().getString(R.string.cloud_front_exercise) + video;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.setDisplay(surfaceHolder);

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.prepareAsync();
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                createExercisePresenter.onBackPressed();
                break;

            case R.id.ib_exercise:
                boolean isPlaying = mediaPlayer.isPlaying();
                createExercisePresenter.onClickPlay(isPlaying);
                break;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.e("ㅇㅇ",mp.getDuration()+"" );
        sb_exercise.setMax(mp.getDuration());
        sb_exercise.setEnabled(true);

        mp.seekTo(0);
        mp.start();

        createExercisePresenter.setSeekBarStart();
    }


    @Override
    public void navigateToExercisePopupActivity(ExerciseCategory exerciseCategory) {
        Intent intent = new Intent(this, ExercisePopupActivity.class);
        intent.putExtra("exerciseCategory", exerciseCategory);
        startActivity(intent);
        finish();
    }

    @Override
    public void showPauseButton() {
        Glide.with(this).load(R.drawable.exercise_pause).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(ib_exercise);
    }

    @Override
    public void showPlayButton() {
        Glide.with(this).load(R.drawable.exercise_play).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(ib_exercise);
    }

    @Override
    public void setMediaPlayerStart() {
        mediaPlayer.start();
    }

    @Override
    public void setMediaPlayerPause() {
        mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.start();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        createExercisePresenter.onBackPressed();
    }

    @Override
    public void navigateToBack() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.start();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            finish();
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        createExercisePresenter.surfaceCreated();
        holder.setFixedSize(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mediaPlayer.pause();
        Glide.with(this).load(R.drawable.exercise_play).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(ib_exercise);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int pausePosition = seekBar.getProgress();
        mediaPlayer.seekTo(pausePosition);
    }

    @Override
    public void setProgressBar() {
        sb_exercise.setProgress(mediaPlayer.getCurrentPosition());
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        createExercisePresenter.onComplete();

    }
}
