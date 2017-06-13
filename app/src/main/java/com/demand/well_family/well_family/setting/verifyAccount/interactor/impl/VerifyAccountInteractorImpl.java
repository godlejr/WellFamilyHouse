package com.demand.well_family.well_family.setting.verifyAccount.interactor.impl;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.MainServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.setting.verifyAccount.interactor.VerifyAccountInteractor;
import com.demand.well_family.well_family.setting.verifyAccount.presenter.VerifyAccountPresenter;
import com.demand.well_family.well_family.util.EncryptionUtil;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by ㅇㅇ on 2017-04-18.
 */


public class VerifyAccountInteractorImpl implements VerifyAccountInteractor {
    private VerifyAccountPresenter verifyAccountPresenter;
    private MainServerConnection mainServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(VerifyAccountInteractorImpl.class);

    public VerifyAccountInteractorImpl(VerifyAccountPresenter verifyAccountPresenter) {
        this.verifyAccountPresenter = verifyAccountPresenter;
    }

    @Override
    public void getUserInfo(String email, String password) {
        String encryptedPassword = EncryptionUtil.encryptSHA256(password);

        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", encryptedPassword);

        mainServerConnection = new NetworkInterceptor().getClientForMainServer().create(MainServerConnection.class);
        Call<User> call_login = mainServerConnection.login(map);
        call_login.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    verifyAccountPresenter.onSuccessVerifyAccount(user);
                } else {
                    verifyAccountPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                log(t);
                verifyAccountPresenter.onNetworkError(null);
            }
        });
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
}
