package com.demand.well_family.well_family.notification.falldiagnosis.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.notification.falldiagnosis.presenter.NotificationFallDiagnosisStoryPresenter;
import com.demand.well_family.well_family.notification.falldiagnosis.presenter.impl.NotificationFallDiagnosisStoryPresenterImpl;
import com.demand.well_family.well_family.notification.falldiagnosis.view.NotificationFallDiagnosisStoryView;

/**
 * Created by ㅇㅇ on 2017-06-13.
 */

public class NotificationFallDiagnosisStoryDetail extends Activity implements NotificationFallDiagnosisStoryView{
    private NotificationFallDiagnosisStoryPresenter notificationFallDiagnosisStoryPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falldiagnosisstorydetail);

        FallDiagnosisStory fallDiagnosisStory = new FallDiagnosisStory();
        fallDiagnosisStory.setId(getIntent().getIntExtra("storyId", 0));

        notificationFallDiagnosisStoryPresenter = new NotificationFallDiagnosisStoryPresenterImpl(this);
        notificationFallDiagnosisStoryPresenter.onCreate(fallDiagnosisStory);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void init() {
        notificationFallDiagnosisStoryPresenter.onLoadData();
    }
}
