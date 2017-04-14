package com.demand.well_family.well_family.main.sns.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.main.sns.interator.JoinFromSNSInterator;
import com.demand.well_family.well_family.main.sns.interator.impl.JoinFromSNSInteratorImpl;
import com.demand.well_family.well_family.main.sns.presenter.JoinFromSNSPresenter;
import com.demand.well_family.well_family.main.sns.view.JoinFromSNSView;
import com.demand.well_family.well_family.util.APIError;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.Calendar;

/**
 * Created by Dev-0 on 2017-04-14.
 */

public class JoinFromSNSPresenterImpl implements JoinFromSNSPresenter {
    private JoinFromSNSView joinFromSNSView;
    private JoinFromSNSInterator joinFromSNSInterator;
    private PreferenceUtil preferenceUtil;

    public JoinFromSNSPresenterImpl(Context context) {
        this.joinFromSNSView = (JoinFromSNSView) context;
        this.joinFromSNSInterator = new JoinFromSNSInteratorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        joinFromSNSView.init();
        View decorView = joinFromSNSView.getDecorView();
        joinFromSNSView.setToolbar(decorView);
        joinFromSNSView.showToolbarTitle("약관동의");
    }

    @Override
    public void setCalendar() {
        Calendar calendar = Calendar.getInstance();
        joinFromSNSView.showDatePicker(calendar);
    }

    @Override
    public void setUserBirth(String birth) {
        String date = joinFromSNSInterator.getUserBirth(birth);
        joinFromSNSView.showBrith(date);
    }

    @Override
    public void onClickJoin(String email, String password, String name, int loginCategoryId, String phone, String birth) {
        boolean joinable = true;

        int birthLength = birth.length();
        int phoneLength = phone.length();

        if (birthLength == 0) {
            joinFromSNSView.showBirthCheckNotification();
            joinable = false;
        }

        if (phoneLength == 0) {
            joinFromSNSView.showPhoneCheckNotification();
            joinable = false;
        }

        if (joinable) {
            joinFromSNSView.goneBirthCheckNotification();
            joinFromSNSView.gonePhoneCheckNotification();

            joinFromSNSInterator.setJoin(email,password,name,loginCategoryId,phone,birth);
        } else {
            return;
        }
    }

    @Override
    public void onSuccessJoin(String email, String password) {
        joinFromSNSInterator.setLogin(email,password);
    }

    @Override
    public void onSuccessLogin(User user) {
        String deviceId = joinFromSNSView.getDeviceId();
        String firebaseToken = joinFromSNSView.getFireBaseToken();

        joinFromSNSInterator.setDeviceIdAndToken(user,deviceId,firebaseToken);
    }

    @Override
    public void onSuccessSetDeviceIdAndToken(User user, String deviceId, String firebaseToken, String accessToken) {
        preferenceUtil.setUserInfo(user,deviceId,firebaseToken,accessToken);
        joinFromSNSView.navigateToMainActivity();
    }

    @Override
    public void onNetworkError(APIError apiError) {
        if (apiError == null) {
            joinFromSNSView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            joinFromSNSView.showMessage(apiError.message());
        }
    }
}
