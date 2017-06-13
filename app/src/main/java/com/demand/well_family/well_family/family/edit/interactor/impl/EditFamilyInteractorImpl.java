package com.demand.well_family.well_family.family.edit.interactor.impl;

import android.net.Uri;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.edit.interactor.EditFamilyInteractor;
import com.demand.well_family.well_family.family.edit.presenter.EditFamilyPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FamilyServerConnection;
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
 * Created by Dev-0 on 2017-04-20.
 */

public class EditFamilyInteractorImpl implements EditFamilyInteractor {
    private EditFamilyPresenter editFamilyPresenter;

    private User user;
    private Family family;

    private Uri uri = null;
    private String path;

    private FamilyServerConnection familyServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(EditFamilyInteractorImpl.class);

    public EditFamilyInteractorImpl(EditFamilyPresenter editFamilyPresenter) {
        this.editFamilyPresenter = editFamilyPresenter;
    }


    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setFamily(Family family) {
        this.family = family;
    }

    @Override
    public void setPhotoUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public Uri getPhotoUri() {
        return uri;
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
    public void setFamilyEdited(String familyName, String familyContent) {
        String accessToken = user.getAccess_token();
        int familyId = family.getId();

        familyServerConnection = new NetworkInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", familyName);
        map.put("content", familyContent);
        Call<ResponseBody> call_update_family_info = familyServerConnection.update_family_info(familyId, map);
        call_update_family_info.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    editFamilyPresenter.onSuccessSetFamilyEdited();
                } else {
                    editFamilyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                editFamilyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setFamilyAvatarEdited(FileToBase64Util fileToBase64Util) {
        String accessToken = user.getAccess_token();
        int familyId = family.getId();

        familyServerConnection = new NetworkInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), fileToBase64Util.addBase64Bitmap(fileToBase64Util.encodeImage(uri, path)));
        Call<ResponseBody> call_update_family_avatar = familyServerConnection.update_family_avatar(familyId, requestBody);

        call_update_family_avatar.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    editFamilyPresenter.onSuccessSetFamilyAvatarEdited();
                } else {
                    editFamilyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                editFamilyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getFamilyData() {
        String accessToken = user.getAccess_token();
        int familyId = family.getId();

        familyServerConnection = new NetworkInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);
        Call<Family> call_get_family_info = familyServerConnection.family(familyId);
        call_get_family_info.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {
                if (response.isSuccessful()) {
                    Family family = response.body();
                    editFamilyPresenter.onSuccessGetFamilyData(family);
                } else {
                    editFamilyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {
                log(t);
                editFamilyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public Family getFamily() {
        return this.family;
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
