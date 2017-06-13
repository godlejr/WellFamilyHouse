package com.demand.well_family.well_family.setting.resetPassword.interactor.impl;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.setting.resetPassword.interactor.ResetPasswordInteractor;
import com.demand.well_family.well_family.setting.resetPassword.presenter.ResetPasswordPresenter;
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

public class ResetPasswordInteractorImpl implements ResetPasswordInteractor {
    private ResetPasswordPresenter resetPasswordPresenter;
    private User user;
    private UserServerConnection userServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordInteractorImpl.class);

    public ResetPasswordInteractorImpl(ResetPasswordPresenter resetPasswordPresenter) {
        this.resetPasswordPresenter = resetPasswordPresenter;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setPassword(String password) {
        String accessToken = user.getAccess_token();
        int userId = user.getId();

        String encryptedPassword = EncryptionUtil.encryptSHA256(password);
        HashMap<String, String> map = new HashMap<>();
        map.put("password", encryptedPassword);

        userServerConnection = new NetworkInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
        Call<Void> call_reset_password = userServerConnection.updatePassword(userId, map);
        call_reset_password.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    resetPasswordPresenter.onSuccessSetPassword();
                } else {
                    resetPasswordPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log(t);
                resetPasswordPresenter.onNetworkError(null);
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
