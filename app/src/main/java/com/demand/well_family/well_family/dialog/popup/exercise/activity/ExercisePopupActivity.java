package com.demand.well_family.well_family.dialog.popup.exercise.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.DragEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.exercise.presenter.ExercisePopupPresenter;
import com.demand.well_family.well_family.dialog.popup.exercise.presenter.impl.ExercisePopupPresenterImpl;
import com.demand.well_family.well_family.dialog.popup.exercise.view.ExercisePopupView;
import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.exercise.flag.ExerciseIntentFlag;

/**
 * Created by ㅇㅇ on 2017-06-26.
 */

public class ExercisePopupActivity extends Activity implements ExercisePopupView, View.OnClickListener, View.OnDragListener, RatingBar.OnRatingBarChangeListener {
    private ExercisePopupPresenter exercisePopupPresenter;

    private Button btn_popupexercise;
    private RatingBar rb_popupexercise;
    private TextView tv_popupexercise_title;
    private TextView tv_popupexercise_subtitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_exercise);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ExerciseCategory exerciseCategory = (ExerciseCategory) getIntent().getSerializableExtra("exerciseCategory");

        exercisePopupPresenter = new ExercisePopupPresenterImpl(this);
        exercisePopupPresenter.onCreate(exerciseCategory);
    }

    @Override
    public void init() {
        tv_popupexercise_title = (TextView) findViewById(R.id.tv_popupexercise_title);
        tv_popupexercise_subtitle = (TextView) findViewById(R.id.tv_popupexercise_subtitle);
        btn_popupexercise = (Button) findViewById(R.id.btn_popupexercise);
        rb_popupexercise = (RatingBar) findViewById(R.id.rb_popupexercise);

        btn_popupexercise.setOnClickListener(this);
        rb_popupexercise.setOnRatingBarChangeListener(this);
    }

    @Override
    public void showTitle(String message) {
        tv_popupexercise_title.setText(message);
    }

    @Override
    public void showSubtitle(String message) {
        tv_popupexercise_subtitle.setText(message);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setRating(float rating) {
        rb_popupexercise.setRating(rating);
    }

    @Override
    public void navigateToBack() {
        setResult(ExerciseIntentFlag.RESULT_OK);
        finish();
    }


    @Override
    public void onBackPressed() {
        exercisePopupPresenter.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_popupexercise:
                int score = (int) rb_popupexercise.getRating();

                ExerciseStory exerciseStory = new ExerciseStory();
                exerciseStory.setScore(score);
                exercisePopupPresenter.onClickSubmit(exerciseStory);
                break;
        }
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return true;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        exercisePopupPresenter.onRatingChanged(rating);
    }

}
