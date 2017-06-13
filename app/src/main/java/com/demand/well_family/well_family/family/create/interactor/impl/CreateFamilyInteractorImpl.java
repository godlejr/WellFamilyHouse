package com.demand.well_family.well_family.family.create.interactor.impl;

import android.net.Uri;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.create.interactor.CreateFamilyInteractor;
import com.demand.well_family.well_family.family.create.presenter.CreateFamilyPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FamilyServerConnection;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;
import com.demand.well_family.well_family.util.FileToBase64Util;
import com.demand.well_family.well_family.util.RealPathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-04-19.
 */

public class CreateFamilyInteractorImpl implements CreateFamilyInteractor {
    private CreateFamilyPresenter createFamilyPresenter;

    private User user;
    private Uri uri;
    private String path;

    private UserServerConnection userServerConnection;
    private FamilyServerConnection familyServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(CreateFamilyInteractorImpl.class);

    public CreateFamilyInteractorImpl(CreateFamilyPresenter createFamilyPresenter) {
        this.createFamilyPresenter = createFamilyPresenter;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setPhotoUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public Uri getPhotoUri() {
        return this.uri;
    }

    @Override
    public void setPhotoPath(RealPathUtil realPathUtil) {
        try {
            path = realPathUtil.getRealPathFromURI_API19(uri);
        } catch (Exception e) {
            log(e);
            path = realPathUtil.getRealPathFromURI_API11to18(uri);
        }
    }

    @Override
    public void setFamilyAdded(String familyName, String familyContent) {
        String accessToken = user.getAccess_token();
        int userId = user.getId();

        HashMap<String, String> map = new HashMap<>();
        map.put("family_name", familyName);
        map.put("family_content", familyContent);

        userServerConnection = new NetworkInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
        Call<Integer> call = userServerConnection.insert_family(userId, map);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int familyId = response.body();
                    createFamilyPresenter.onSuccessSetFamilyAdded(familyId);
                } else {
                    createFamilyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                createFamilyPresenter.onNetworkError(null);
            }
        });

    }

    @Override
    public void setFamilyAvatarAdded(final int familyId, FileToBase64Util fileToBase64Util) {
        String accessToken = user.getAccess_token();

        familyServerConnection = new NetworkInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), fileToBase64Util.addBase64Bitmap(fileToBase64Util.encodeImage(uri, path)));

        Call<ResponseBody> call_photo = familyServerConnection.update_family_avatar(familyId, requestBody);
        call_photo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    createFamilyPresenter.onSuccessSetFamilyAvatarAdded(familyId);
                } else {
                    createFamilyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                createFamilyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getFamilyData(int familyId) {
        String accessToken = user.getAccess_token();

        familyServerConnection = new NetworkInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);
        Call<Family> call_family = familyServerConnection.family(familyId);
        call_family.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {
                if (response.isSuccessful()) {
                    Family family = response.body();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        log(e);
                    }
                    createFamilyPresenter.onSuccessGetFamilyData(family);
                } else {
                    createFamilyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {
                log(t);
                createFamilyPresenter.onNetworkError(null);
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
