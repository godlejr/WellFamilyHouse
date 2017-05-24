package com.demand.well_family.well_family.family.search.interactor.impl;

import com.demand.well_family.well_family.dto.FamilyInfoForFamilyJoin;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.search.adapter.FamilyAdapter;
import com.demand.well_family.well_family.family.search.interactor.SearchFamilyInteractor;
import com.demand.well_family.well_family.family.search.presenter.SearchFamilyPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-05-24.
 */

public class SearchFamilyInteractorImpl implements SearchFamilyInteractor {
    private static Logger logger = LoggerFactory.getLogger(SearchFamilyInteractorImpl.class);
    private SearchFamilyPresenter searchFamilyPresenter;
    private UserServerConnection userServerConnection;

    public SearchFamilyInteractorImpl(SearchFamilyPresenter searchFamilyPresenter) {
        this.searchFamilyPresenter = searchFamilyPresenter;
    }

    @Override
    public void setFamilySearched(User user, String keyword) {
        String accessToken = user.getAccess_token();
        int userId = user.getId();

        HashMap<String, String> map = new HashMap<>();
        map.put("search", keyword);

        userServerConnection = new HeaderInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
        Call<ArrayList<FamilyInfoForFamilyJoin>> call_find_family = userServerConnection.find_family(userId, map);
        call_find_family.enqueue(new Callback<ArrayList<FamilyInfoForFamilyJoin>>() {
            @Override
            public void onResponse(Call<ArrayList<FamilyInfoForFamilyJoin>> call, Response<ArrayList<FamilyInfoForFamilyJoin>> response) {
                if (response.isSuccessful()) {
                    ArrayList<FamilyInfoForFamilyJoin> familyList = response.body();
                    searchFamilyPresenter.onSuccessSetFamilySearched(familyList);
                } else {
                    searchFamilyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FamilyInfoForFamilyJoin>> call, Throwable t) {
                log(t);
                searchFamilyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setUserJoinFamily(FamilyInfoForFamilyJoin family, User user, final FamilyAdapter.FamilyViewHolder holder) {
        int userId = user.getId();
        String accessToken = user.getAccess_token();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("creator_id", String.valueOf(family.getUser_id()));
        map.put("family_id", String.valueOf(family.getId()));
        map.put("family_name", String.valueOf(family.getName()));

        userServerConnection = new HeaderInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
        Call<ResponseBody> call_join_family = userServerConnection.join_family(userId, map);
        call_join_family.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    searchFamilyPresenter.onSuccessSetUserJoinFamily(holder);
                } else {
                    searchFamilyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                searchFamilyPresenter.onNetworkError(null);
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
