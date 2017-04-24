package com.demand.well_family.well_family.dialog.popup.deactivation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.deactivation.presenter.DeactivationPopupPresenter;
import com.demand.well_family.well_family.dialog.popup.deactivation.presenter.impl.DeactivationPopupPresenterImpl;
import com.demand.well_family.well_family.dialog.popup.deactivation.view.DeactivationPopupView;
import com.demand.well_family.well_family.main.login.activity.LoginActivity;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-04-07.
 */

public class DeactivationPopupActivity extends Activity implements DeactivationPopupView, View.OnClickListener {
    private DeactivationPopupPresenter deactivationPopupPresenter;

    private TextView tv_deactivation_popup_content;
    private Button btn_deactivation_popup_cancel;
    private Button btn_deactivation_popup_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_deactivation);

        deactivationPopupPresenter = new DeactivationPopupPresenterImpl(this);
        deactivationPopupPresenter.onCreate();
    }

    @Override
    public void setDisplay() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        getWindow().getAttributes().width = (int) (display.getWidth() * 0.9);
        getWindow().getAttributes().height = (int) (display.getHeight() * 0.4);
    }

    @Override
    public void init() {
        finishList.add(this);

        tv_deactivation_popup_content = (TextView) findViewById(R.id.tv_deactivation_popup_content);
        btn_deactivation_popup_cancel = (Button) findViewById(R.id.btn_deactivation_popup_cancel);
        btn_deactivation_popup_commit = (Button) findViewById(R.id.btn_deactivation_popup_commit);

        btn_deactivation_popup_commit.setOnClickListener(this);
        btn_deactivation_popup_cancel.setOnClickListener(this);
    }

    @Override
    public void setPopupContent(String message) {
        tv_deactivation_popup_content.setText(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_deactivation_popup_cancel:
                finish();
                break;

            case R.id.btn_deactivation_popup_commit:
                deactivationPopupPresenter.setDeactivation();
                break;
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(DeactivationPopupActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToLoginActivity() {
        Intent intent = new Intent(DeactivationPopupActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToBack() {
        int finishListSize = finishList.size();
        for (int i = 0; i < finishListSize; i++) {
            finishList.get(i).finish();
        }
    }

}
