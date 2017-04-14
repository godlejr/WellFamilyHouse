package com.demand.well_family.well_family.main.agreement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.main.agreement.presenter.AgreementPresenter;
import com.demand.well_family.well_family.main.agreement.presenter.impl.AgreementPresenterImpl;
import com.demand.well_family.well_family.main.agreement.view.AgreementView;
import com.demand.well_family.well_family.main.join.activity.JoinActivity;


public class AgreementActivity extends AppCompatActivity implements AgreementView, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private AgreementPresenter agreementPresenter;

    private Button btn_agreement_join;
    private CheckBox cb_agreement_all, cb_agreement1, cb_agreement2;
    private TextView agreement1;
    private TextView agreement2;

    //toolbar
    private View decorView;
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        agreementPresenter = new AgreementPresenterImpl(this);
        agreementPresenter.onCreate();
    }

    @Override
    public void init() {
        agreement1 = (TextView) findViewById(R.id.agreement1);
        agreement1.setMovementMethod(new ScrollingMovementMethod());
        agreement2 = (TextView) findViewById(R.id.agreement2);
        agreement2.setMovementMethod(new ScrollingMovementMethod());

        btn_agreement_join = (Button) findViewById(R.id.btn_agreement_join);
        cb_agreement_all = (CheckBox) findViewById(R.id.cb_agreement_all);
        cb_agreement1 = (CheckBox) findViewById(R.id.cb_agreement1);
        cb_agreement2 = (CheckBox) findViewById(R.id.cb_agreement2);

        btn_agreement_join.setOnClickListener(this);
        cb_agreement_all.setOnCheckedChangeListener(this);
        cb_agreement1.setOnCheckedChangeListener(this);
        cb_agreement2.setOnCheckedChangeListener(this);
    }

    @Override
    public void setToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_back.setOnClickListener(this);
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void setAgreementAll(boolean isCheck) {
        cb_agreement_all.setChecked(isCheck);
    }

    @Override
    public void setAgreement1Check(boolean isCheck) {
        cb_agreement1.setChecked(isCheck);
    }

    @Override
    public void setAgreement2Check(boolean isCheck) {
        cb_agreement2.setChecked(isCheck);
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
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
    public void navigateToJoinActivity() {
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                navigateToBack();
                break;

            case R.id.btn_agreement_join:
                boolean agreement1Check = cb_agreement1.isChecked();
                boolean agreement2Check = cb_agreement2.isChecked();
                agreementPresenter.onClickAgreement(agreement1Check, agreement2Check);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_agreement_all:
                boolean agreementAllCheck = cb_agreement_all.isChecked();
                agreementPresenter.onCheckedChangedAgreementAll(agreementAllCheck);
                break;

            case R.id.cb_agreement1:
            case R.id.cb_agreement2:
                boolean agreement1Check = cb_agreement1.isChecked();
                boolean agreement2Check = cb_agreement2.isChecked();
                agreementPresenter.onCheckedChangedAgreement(agreement1Check, agreement2Check);
                break;
        }
    }
}
