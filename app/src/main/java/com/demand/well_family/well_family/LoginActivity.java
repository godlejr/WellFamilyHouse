package com.demand.well_family.well_family;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.log.LogFlag;
import com.demand.well_family.well_family.register.AgreementActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login;
    private EditText email, pwd;
    private Button register;

    private String et_email;
    private String et_pwd;

    private SharedPreferences loginInfo;
    private SharedPreferences.Editor editor;
    private Server_Connection server_connection;

    public static ArrayList<Activity> finishList = new ArrayList<Activity>();

    private static final Logger logger = LoggerFactory.getLogger(LoginActivity.class);

    //facebook
    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;
    private Context context;

    //naver
    private OAuthLogin mOAuthLoginModule;
    private OAuthLoginButton oAuthLoginButton;
    private String client_id = "C7DiZf0HXR0Oo3L237p4";
    private String client_secret = "21xT29Q4hJ";
    private String client_name = "웰패밀리 하우스";  // 네이버 앱의 로그인 화면에 표시할 애플리케이션 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        finishList.add(this);
        init();

        context = this;
        facebookLogin();
        naverLogin();
    }

    private void init() {
        login = (Button) findViewById(R.id.btn_main_login);
        register = (Button) findViewById(R.id.btn_main_register);
        email = (EditText) findViewById(R.id.et_login_email);
        pwd = (EditText) findViewById(R.id.et_login_pwd);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    private void facebookLogin(){
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton = (LoginButton) findViewById(R.id.facebookLoginButton);
        facebookLoginButton.setReadPermissions("email");
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("facebookLogin", loginResult+"");

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String id = object.getString("id");             //id
                            String name = object.getString("name");         // 이름
                            String email = object.getString("email");       // 이메일
                            String gender = object.getString("gender");     // 성별


                            Toast.makeText(context, id+","+name+","+email+","+gender, Toast.LENGTH_SHORT).show();
                            Log.e("facebook",  object.toString());

                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("facebookLogin", "onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("facebookLogin", "onError");

            }
        });
    }

    private void naverLogin() {
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                LoginActivity.this,
                client_id, client_secret, client_name);

        oAuthLoginButton = (OAuthLoginButton) findViewById(R.id.oAuthLoginButton);
        oAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);

        oAuthLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
            }
        });
    }

    OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String token = mOAuthLoginModule.getAccessToken(LoginActivity.this);
                        String response = mOAuthLoginModule.requestApi(LoginActivity.this, token, "https://openapi.naver.com/v1/nid/me");
                        Log.e("Tt", response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String id = json.getJSONObject("response").getString("id");
                            String name = json.getJSONObject("response").getString("name");
                            String email = json.getJSONObject("response").getString("email");
                            String profile_image = json.getJSONObject("response").getString("profile_image");

                            Log.e("user_info", id + "\n" + name + "\n" + email + "\n" + profile_image);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    }
                }).start();

            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(context).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(context);
                Toast.makeText(context, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_login:
                et_email = email.getText().toString();
                et_pwd = pwd.getText().toString();

                server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("email", et_email);
                map.put("password", et_pwd);
                Call<ArrayList<User>> call = server_connection.login(map);

                call.enqueue(new Callback<ArrayList<User>>() {
                    @Override
                    public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                        final ArrayList<User> userList = response.body();

                        if (userList.size() == 0) {
                            //
                            Toast.makeText(LoginActivity.this, "ID / 패스워드를 확인해주세요.", Toast.LENGTH_LONG).show();
                        } else {
                            final String device_id = getDeviceId();
                            final String token = FirebaseInstanceId.getInstance().getToken();

                            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                            HashMap<String, String> map = new HashMap<>();
                            map.put("device_id", device_id);
                            map.put("token", token);
                            Call<ResponseBody> call_update_deviceId = server_connection.update_deviceId_token(userList.get(0).getId(), map);

                            call_update_deviceId.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
                                    editor = loginInfo.edit();
                                    editor.putInt("user_id", userList.get(0).getId());
                                    editor.putString("user_name", userList.get(0).getName());
                                    editor.putString("user_email", userList.get(0).getEmail());
                                    editor.putString("user_birth", userList.get(0).getBirth());
                                    editor.putString("user_avatar", userList.get(0).getAvatar());
                                    editor.putString("user_phone", userList.get(0).getPhone());
                                    editor.putString("device_id", device_id);
                                    editor.putString("token", token);
                                    editor.putInt("user_level", userList.get(0).getLevel());
                                    editor.commit();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("user_id", userList.get(0).getId());
                                    intent.putExtra("user_email", userList.get(0).getEmail());
                                    intent.putExtra("user_birth", userList.get(0).getBirth());
                                    intent.putExtra("user_phone", userList.get(0).getPhone());
                                    intent.putExtra("user_name", userList.get(0).getName());
                                    intent.putExtra("user_level", userList.get(0).getLevel());
                                    intent.putExtra("user_avatar", userList.get(0).getAvatar());
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    log(t);
                                    Toast.makeText(LoginActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                        log(t);
                        Toast.makeText(LoginActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.btn_main_register:
                Intent intent = new Intent(v.getContext(), AgreementActivity.class);
                startActivity(intent);
                break;
        }
    }

    private String getDeviceId() {
        final String androidId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        logger.info(androidId);
        return androidId.toString();
    }


    @Override
    public void onBackPressed() {
        int finishListSize = finishList.size();

        for (int i = 0; i < finishListSize; i++) {
            finishList.get(i).finish();
        }

        super.onBackPressed();
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
