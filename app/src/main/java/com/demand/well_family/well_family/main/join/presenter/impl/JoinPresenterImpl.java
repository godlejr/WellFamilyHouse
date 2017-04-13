package com.demand.well_family.well_family.main.join.presenter.impl;

import android.content.Context;
import android.text.InputFilter;
import android.view.View;

import com.demand.well_family.well_family.main.join.interator.JoinInterator;
import com.demand.well_family.well_family.main.join.interator.impl.JoinInteratorImpl;
import com.demand.well_family.well_family.main.join.presenter.JoinPresenter;
import com.demand.well_family.well_family.main.join.view.JoinView;
import com.demand.well_family.well_family.util.APIError;

import java.util.Calendar;

/**
 * Created by Dev-0 on 2017-04-13.
 */

public class JoinPresenterImpl implements JoinPresenter {
    private JoinView joinView;
    private JoinInterator joinInterator;

    public JoinPresenterImpl(Context context) {
        this.joinView = (JoinView) context;
        this.joinInterator = new JoinInteratorImpl(this);
    }


    @Override
    public void onCreate() {
        joinView.init();
        View decorView = joinView.getDecorView();
        joinView.setToolbar(decorView);
        joinView.showToolbarTitle("회원 가입");
    }

    @Override
    public void setCalendar() {
        Calendar calendar = Calendar.getInstance();
        joinView.showDatePicker(calendar);
    }

    @Override
    public void setUserBirth(String birth) {
        String date = joinInterator.getUserBirth(birth);
        joinView.showBrith(date);
    }

    @Override
    public void setNameFilterCallback() {
        InputFilter[] inputFilters = joinInterator.getInputFilterForName();
        joinView.setNameFilterCallback(inputFilters);
    }

    @Override
    public void onClickJoin(String email, String password, String passwordConfirm, String name, String birth, String phone) {
        boolean joinable = true;

        int emailLength = email.length();
        int passwordLength = password.length();
        int passswordConfirmLength = passwordConfirm.length();
        int nameLength = name.length();
        int birthLength = birth.length();
        int phoneLength = phone.length();

        if (emailLength == 0) {
            joinView.showEmailCheckNotification();
            joinable = false;
        }

        if (passwordLength == 0) {
            joinView.showPasswordCheckNotification();
            joinable = false;
        }

        if (passswordConfirmLength == 0) {
            joinView.showPasswordConfirmCheckNotification();
            joinable = false;
        }

        if (nameLength == 0) {
            joinView.showNameCheckNotification();
            joinable = false;
        }

        if (birthLength == 0) {
            joinView.showBirthCheckNotification();
            joinable = false;
        }

        if (phoneLength == 0) {
            joinView.showPhoneCheckNotification();
            joinable = false;
        }

        boolean emailCheck = joinInterator.isEmailCheck();

        if (emailCheck == false) {
            joinView.showMessage("아이디 중복확인을 해주세요.");
            joinable = false;
        }

        if (!password.equals(passwordConfirm)) {
            joinView.showPasswordConfirmCheckNotification();
            joinable = false;
        }

        boolean passwordCheck = joinInterator.getPasswordValidation(password);
        if (passwordCheck == false) {
            joinView.showMessage("비밀번호는 6~20자 대소문자, 숫자/특수문자를 포함해야합니다.");
            joinable = false;
        }

        if (joinable) {
            joinView.goneEmailCheckNotification();
            joinView.goneBirthCheckNotification();
            joinView.goneNameCheckNotification();
            joinView.gonePasswordCheckNotification();
            joinView.gonePasswordConfirmCheckNotification();
            joinView.gonePhoneCheckNotification();

            joinInterator.setJoin(email, password, name, birth, phone);
        } else {
            return;
        }

    }

    @Override
    public void onClickEmailCheck(String email) {
        int emailLength = email.trim().length();

        if (emailLength == 0) {
            joinView.showEmailCheckNotification();
        } else if (!joinInterator.getEmailValidation(email)) {
            joinView.showMessage("올바른 이메일 형식을 입력해주세요.");
        } else {
            joinView.goneEmailCheckNotification();
            joinInterator.getEmailCheck(email);
        }
    }

    @Override
    public void onSuccessJoin() {
        joinView.showMessage("회원가입이 되었습니다.");
        joinView.navigateToLoginActivity();
    }

    @Override
    public void onNameFilter() {
        joinView.showMessage("특수문자는 입력이 불가합니다.");
    }

    @Override
    public void validateEmail(int check) {
        if (check != 1) {
            joinView.showMessage("사용가능한 아이디입니다.");
            joinInterator.setEmailCheck(true);
        } else {
            joinView.showMessage("사용중인 아이디입니다.");
        }
    }

    @Override
    public void onNetworkError(APIError apiError) {
        if (apiError == null) {
            joinView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            joinView.showMessage(apiError.message());
        }
    }
}
