package com.demand.well_family.well_family.main.base.interator.impl;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.UserServerConnection;
import com.demand.well_family.well_family.dto.App;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.main.base.interator.MainInterator;
import com.demand.well_family.well_family.main.base.presenter.MainPresenter;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public class MainInteratorImpl implements MainInterator {
    private MainPresenter mainPresenter;

    private UserServerConnection userServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(MainInteratorImpl.class);

    public MainInteratorImpl(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void getFamilyData(User user) {
        String access_token = user.getAccess_token();
        int user_id = user.getId();

        userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
        Call<ArrayList<Family>> call = userServerConnection.family_Info(user_id);
        call.enqueue(new Callback<ArrayList<Family>>() {
            @Override
            public void onResponse(Call<ArrayList<Family>> call, Response<ArrayList<Family>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Family> familyList = response.body();
                    mainPresenter.onFamilyDataParsing(familyList);
                } else {
                    mainPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Family>> call, Throwable t) {
                log(t);
                mainPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public ArrayList<App> getAppData() {
        ArrayList<App> appList = new ArrayList<>();
        appList.add(new App("추억소리", R.drawable.memory_sound));
        appList.add(new App("셀핏", R.drawable.logo_selffeet));
        appList.add(new App("해핏", R.drawable.happyfeet, "com.demand.happyfeet"));
        appList.add(new App("버블핏", R.drawable.logo_bubblefeet, "com.demand.bubblefeet"));
        appList.add(new App("Good Buddy", R.drawable.goodbuddy, "healthcare.nhis.GoodBuddy"));

        return appList;
    }

    @Override
    public void setUserInfo(User user) {
        int user_id = user.getId();
        String access_token = user.getAccess_token();

        userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
        Call<User> call_user_info = userServerConnection.user_Info(user_id);
        call_user_info.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    mainPresenter.setToolbarAndMenu(user);
                } else {
                    mainPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                log(t);
                mainPresenter.onNetworkError(null);
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
                logger.info("Exception: " + throwable.getMessage());
                logger.info(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }
}
