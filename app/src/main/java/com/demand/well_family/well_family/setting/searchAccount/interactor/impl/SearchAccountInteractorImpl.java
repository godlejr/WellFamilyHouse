package com.demand.well_family.well_family.setting.searchAccount.interactor.impl;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.MainServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.setting.searchAccount.interactor.SearchAccountInteractor;
import com.demand.well_family.well_family.setting.searchAccount.presenter.SearchAccountPresenter;
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

public class SearchAccountInteractorImpl implements SearchAccountInteractor {
    private SearchAccountPresenter findAccountPresenter;
    private MainServerConnection mainServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(SearchAccountInteractorImpl.class);

    public SearchAccountInteractorImpl(SearchAccountPresenter findAccountPresenter) {
        this.findAccountPresenter = findAccountPresenter;
    }

    @Override
    public void getUserInfoFromEmail(String email) {
        mainServerConnection = new HeaderInterceptor().getClientForMainServer().create(MainServerConnection.class);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", email);

        Call<User> call_find_email = mainServerConnection.findEmail(map);
        call_find_email.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    findAccountPresenter.onSuccessGetUserInfo(user);
                } else {
                    findAccountPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                log(t);
                findAccountPresenter.onNetworkError(null);
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
