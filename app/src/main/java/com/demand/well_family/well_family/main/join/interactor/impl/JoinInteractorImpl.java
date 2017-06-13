package com.demand.well_family.well_family.main.join.interactor.impl;

import android.text.InputFilter;
import android.text.Spanned;

import com.demand.well_family.well_family.repository.MainServerConnection;
import com.demand.well_family.well_family.flag.JoinFlag;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.main.base.interactor.impl.MainInteractorImpl;
import com.demand.well_family.well_family.main.join.interactor.JoinInteractor;
import com.demand.well_family.well_family.main.join.presenter.JoinPresenter;
import com.demand.well_family.well_family.util.EncryptionUtil;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-04-13.
 */

public class JoinInteractorImpl implements JoinInteractor {

    private boolean emailCheck;

    private JoinPresenter joinPresenter;

    private MainServerConnection mainServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(MainInteractorImpl.class);


    public JoinInteractorImpl(JoinPresenter joinPresenter) {
        this.joinPresenter = joinPresenter;
        this.emailCheck = false;
    }

    public InputFilter characterFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int length = source.length();
            for (int i = 0; i < length; i++) {
                Pattern pattern = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
                if (!pattern.matcher(source).matches()) {
                        joinPresenter.onNameFilter();
                    return "";
                }
            }
            return null;
        }
    };

    @Override
    public String getUserBirth(String birth) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(birth);
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
            String birthDate = transFormat.format(date);
            return birthDate;
        } catch (ParseException e) {
            log(e);
            return null;
        }
    }

    @Override
    public boolean getEmailValidation(String email) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        boolean isNormal = matcher.matches();
        return isNormal;
    }

    @Override
    public boolean getPasswordValidation(String password) {
        String regex = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{6,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        boolean isNormal = matcher.matches();
        return isNormal;
    }

    @Override
    public void setJoin(String email, String password, String name, String birth, String phone) {
        String encryptedPassword = EncryptionUtil.encryptSHA256(password);

        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", encryptedPassword);
        map.put("name", name);
        map.put("birth", birth);
        map.put("phone", phone);
        map.put("login_category_id", String.valueOf(JoinFlag.DEMAND));


        mainServerConnection = new NetworkInterceptor().getClientForMainServer().create(MainServerConnection.class);
        Call<ResponseBody> call_join = mainServerConnection.join(map);
        call_join.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    joinPresenter.onSuccessJoin();
                } else {
                    joinPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                joinPresenter.onNetworkError(null);
            }
        });
    }


    @Override
    public void getEmailCheck(String email) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);

        mainServerConnection = new NetworkInterceptor().getClientForMainServer().create(MainServerConnection.class);
        Call<Integer> call_email_check = mainServerConnection.email_check(map);
        call_email_check.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int check = response.body();
                    joinPresenter.validateEmail(check);
                } else {
                    joinPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                joinPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public InputFilter[] getInputFilterForName() {
        return new  InputFilter[]{characterFilter};
    }

    public boolean isEmailCheck() {
        return emailCheck;
    }

    public void setEmailCheck(boolean emailCheck) {
        this.emailCheck = emailCheck;
    }

    private static void log(Throwable throwable) {
        StackTraceElement[] ste = throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if (LogFlag.printFlag) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + throwable.getMessage());
                logger.info(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }
}
