package com.demand.well_family.well_family.users.search.interactor.impl;

import android.util.Log;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FamilyServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.users.search.adapter.UserAdapter;
import com.demand.well_family.well_family.users.search.interactor.SearchUserInteractor;
import com.demand.well_family.well_family.users.search.presenter.SearchUserPresenter;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public class SearchUserInteractorImpl implements SearchUserInteractor {
    private SearchUserPresenter searchUserPresenter;
    private FamilyServerConnection familyServerConnection;

    private Family family;
    private static final Logger logger = LoggerFactory.getLogger(SearchUserInteractorImpl.class);

    public SearchUserInteractorImpl(SearchUserPresenter searchUserPresenter) {
        this.searchUserPresenter = searchUserPresenter;
    }

    @Override
    public void setUserSearched(final User user, Family family, String searchKeyword) {
        String accessToken = user.getAccess_token();
        int familyId = family.getId();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("search", searchKeyword);

        familyServerConnection = new NetworkInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);
        Call<ArrayList<UserInfoForFamilyJoin>> call_user = familyServerConnection.find_user(familyId, map);
        call_user.enqueue(new Callback<ArrayList<UserInfoForFamilyJoin>>() {
            @Override
            public void onResponse(Call<ArrayList<UserInfoForFamilyJoin>> call, Response<ArrayList<UserInfoForFamilyJoin>> response) {
                if (response.isSuccessful()) {
                    ArrayList<UserInfoForFamilyJoin> userList = response.body();
                    searchUserPresenter.onSuccessSetUserSearched(userList);
                } else {
                    searchUserPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserInfoForFamilyJoin>> call, Throwable t) {
                log(t);
                searchUserPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public Family getFamily() {
        return this.family;
    }

    @Override
    public void setFamily(Family family) {
        this.family = family;
    }

    @Override
    public void setUserJoinFamily(User user, final UserInfoForFamilyJoin userFound) {
        String accessToken = user.getAccess_token();
        int familyId = family.getId();
        int userFoundId = userFound.getId();

        familyServerConnection = new NetworkInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);
        Call<Void> call_update_user_for_familyjoin = familyServerConnection.update_user_for_familyjoin(familyId, userFoundId);
        call_update_user_for_familyjoin.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    searchUserPresenter.onSuccessSetUserJoinFamily(userFound);
                } else {
                    searchUserPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log(t);
                searchUserPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setUserInvited(User user, final UserInfoForFamilyJoin userFound, final UserAdapter.UserViewHolder holder) {
        String accessToken = user.getAccess_token();
        int familyId = family.getId();

        familyServerConnection = new NetworkInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(userFound.getId()));
        Call<ResponseBody> call_invite = familyServerConnection.insert_user_into_family(familyId, map);
        call_invite.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    searchUserPresenter.onSuccessSetUserInvited(userFound, holder);
                } else {
                    searchUserPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                    try {
                        Log.e("ㅇㅇㅇ", response.errorBody().string());
                    } catch (IOException e) {


                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                searchUserPresenter.onNetworkError(null);
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
