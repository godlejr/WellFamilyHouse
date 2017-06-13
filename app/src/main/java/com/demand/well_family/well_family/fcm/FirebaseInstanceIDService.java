package com.demand.well_family.well_family.fcm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.util.ErrorUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private UserServerConnection userServerConnection;
    private SharedPreferences loginInfo;

    private static final Logger logger = LoggerFactory.getLogger(FirebaseInstanceIdService.class);

    //user info
    private int user_id;
    private String user_email;
    private String user_name;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private String user_avatar;
    private String device_id;
    private String access_token;

    private Context context;

    @Override
    public void onCreate() {
        context = this;

        loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
        user_id = loginInfo.getInt("user_id", 0);
        user_level = loginInfo.getInt("user_level", 0);
        user_name = loginInfo.getString("user_name", null);
        user_email = loginInfo.getString("user_email", null);
        user_birth = loginInfo.getString("user_birth", null);
        user_avatar = loginInfo.getString("user_avatar", null);
        user_phone = loginInfo.getString("user_phone", null);
        device_id = loginInfo.getString("device_id", null);
        access_token = loginInfo.getString("access_token", null);

        Log.e("service user",user_id+"");
        Log.e("service",user_id+"");
    }

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();

        Log.e("service",token);
       // sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        Log.e("service",3+"");
        userServerConnection = new NetworkInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);

        Call<ResponseBody> call_update_token = userServerConnection.update_token(user_id, token);
        call_update_token.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
//                Intent intent = new Intent(context, FirebaseInstanceIDService.class);
//                startService(intent);
                } else {
                    Toast.makeText(context, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
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
