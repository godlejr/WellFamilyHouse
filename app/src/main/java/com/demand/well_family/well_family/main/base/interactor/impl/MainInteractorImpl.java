package com.demand.well_family.well_family.main.base.interactor.impl;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.dto.App;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.main.base.interactor.MainInteractor;
import com.demand.well_family.well_family.main.base.presenter.MainPresenter;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public class MainInteractorImpl implements MainInteractor {
    private MainPresenter mainPresenter;


    private UserServerConnection userServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(MainInteractorImpl.class);

    public MainInteractorImpl(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void getFamilyData(User user) {
        String accessToken = user.getAccess_token();
        int user_id = user.getId();

        userServerConnection = new NetworkInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
        Call<ArrayList<Family>> call = userServerConnection.family_Info(user_id);
        call.enqueue(new Callback<ArrayList<Family>>() {
            @Override
            public void onResponse(Call<ArrayList<Family>> call, Response<ArrayList<Family>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Family> familyList = response.body();
                    mainPresenter.onSuccessGetFamilyData(familyList);
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
        appList.add(new App("????????????", R.drawable.memory_sound));
        appList.add(new App("????????????", R.drawable.logo_selffeet));
        appList.add(new App("????????????", R.drawable.logo_selffeet_1));
        appList.add(new App("??????", R.drawable.happyfeet, "com.demand.happyfeet"));
        appList.add(new App("?????????", R.drawable.logo_bubblefeet, "com.demand.bubblefeet"));
        appList.add(new App("GoodBuddy", R.drawable.goodbuddy, "com.demand.goodbuddy"));

//        appList.add(new App("Good Buddy", R.drawable.goodbuddy, "healthcare.nhis.GoodBuddy"));
//        appList.add(new App("h2o", R.drawable.h2o, "healthcare.demand.H2Ov2"));

        return appList;
    }

    @Override
    public void setUserInfo(User user) {
        int user_id = user.getId();
        String access_token = user.getAccess_token();

        userServerConnection = new NetworkInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
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

    @Override
    public String getUserBirth(String birth) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(birth);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy??? MM??? dd??????");
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            log(e);
            return null;
        }
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
