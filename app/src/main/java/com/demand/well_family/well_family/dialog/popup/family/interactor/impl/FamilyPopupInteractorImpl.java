package com.demand.well_family.well_family.dialog.popup.family.interactor.impl;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.family.interactor.FamilyPopupInteractor;
import com.demand.well_family.well_family.dialog.popup.family.presenter.FamilyPopupPresenter;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;

import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FamilyServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public class FamilyPopupInteractorImpl implements FamilyPopupInteractor {
    private UserInfoForFamilyJoin userInfoForFamilyJoin;
    private Family family;
    private User user;
    private boolean deleteFlag;

    private FamilyPopupPresenter familyPopupPresenter;
    private FamilyServerConnection familyServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(FamilyPopupInteractorImpl.class);

    public FamilyPopupInteractorImpl(FamilyPopupPresenter familyPopupPresenter) {
        this.familyPopupPresenter = familyPopupPresenter;
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
    public void setUserInfoForFamilyJoin(UserInfoForFamilyJoin userInfoForFamilyJoin) {
        this.userInfoForFamilyJoin = userInfoForFamilyJoin;
    }

    @Override
    public UserInfoForFamilyJoin getUserInfoForFamilyJoin() {
        return this.userInfoForFamilyJoin;
    }

    @Override
    public void setAcceptInvitation() {
        int userId = user.getId();
        String accessToken = user.getAccess_token();
        int familyId = family.getId();

        familyServerConnection = new HeaderInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);
        Call<Void> call_update_user_for_familyjoin = familyServerConnection.update_user_for_familyjoin(familyId, userId);
        call_update_user_for_familyjoin.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    familyPopupPresenter.onSuccessAcceptInvitation();
                } else {
                    familyPopupPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log(t);
                familyPopupPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setFamilySecession() {
        String accessToken = user.getAccess_token();
        int familyId = family.getId();
        int userId = user.getId();

        familyPopupPresenter.setPopupButtonBackground(R.drawable.round_corner_red_r10);

        familyServerConnection = new HeaderInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);
        Call<ResponseBody> call_delete_user_from_family = familyServerConnection.delete_user_from_family(familyId, userId);
        call_delete_user_from_family.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    familyPopupPresenter.onSuccessFamilySecession();
                } else {
                    familyPopupPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                familyPopupPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setAcceptRequest() {
        String accessToken = user.getAccess_token();
        int familyId = family.getId();
        int joinerId = userInfoForFamilyJoin.getId();

        familyServerConnection = new HeaderInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);
        Call<Void> call_update_user_for_familyjoin = familyServerConnection.update_user_for_familyjoin(familyId, joinerId);
        call_update_user_for_familyjoin.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    familyPopupPresenter.onSuccessAcceptRequest();
                } else {
                    familyPopupPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log(t);
                familyPopupPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setDeleteFamily() {
        String accessToken = user.getAccess_token();
        int familyId = family.getId();

        familyServerConnection = new HeaderInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);
        Call<ResponseBody> call_delete_family = familyServerConnection.delete_family(familyId);
        call_delete_family.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    familyPopupPresenter.onSuccessDeleteFamily();
                } else {
                    familyPopupPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                familyPopupPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setFamily(Family family) {
        this.family = family;
    }

    @Override
    public Family getFamily() {
        return this.family;
    }

    @Override
    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public boolean getDeleteFlag() {
        return this.deleteFlag;
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
