package com.demand.well_family.well_family;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.demand.well_family.well_family.connection.MainServerConnection;
import com.demand.well_family.well_family.connection.UserServerConnection;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.JoinFlag;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.main.base.activity.MainActivity;
import com.demand.well_family.well_family.main.agreement.activity.AgreementActivity;
import com.demand.well_family.well_family.register.SNSRegisterActivity;
import com.demand.well_family.well_family.settings.SearchAccountActivity;
import com.demand.well_family.well_family.util.ErrorUtil;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton login;
    private EditText email, pwd;
    private Button register;
    private Button btn_main_find_pwd;
    private ScrollView sv_login;

    private String et_email;
    private String et_pwd;

    private SharedPreferences loginInfo;
    private SharedPreferences.Editor editor;

    private MainServerConnection mainServerConnection;
    private UserServerConnection userServerConnection;

    public static ArrayList<Activity> finishList = new ArrayList<Activity>();

    private static final Logger logger = LoggerFactory.getLogger(LoginActivity.class);

    //facebook
    private ImageButton facebookLoginButton;
    private CallbackManager callbackManager;
    private Context context;

    //naver
    private OAuthLogin mOAuthLoginModule;
    private OAuthLoginButton oAuthLoginButton;
    private ImageButton naverLoginButton;
    private String client_id = "vX9icrhPpuwATfps78ce";
    private String client_secret = "YiBZjWpiNA";
    private String client_name = "웰패밀리 하우스";  // 네이버 앱의 로그인 화면에 표시할 애플리케이션 이름

    // access_token
    private String access_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        finishList.add(this);
        init();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.demand.well_family.well_family",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        context = this;
        SNSLogin();

    }

    private void SNSLogin() {
        //facebook
        facebookLoginButton = (ImageButton) findViewById(R.id.facebookLoginButton);
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLogin();
            }
        });

        //naver
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                LoginActivity.this,
                client_id, client_secret, client_name);

        oAuthLoginButton = (OAuthLoginButton) findViewById(R.id.oAuthLoginButton);
        oAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);

        naverLoginButton = (ImageButton) findViewById(R.id.naverLoginButton);
        naverLoginButton.bringToFront();
        naverLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
            }
        });
    }

    private void init() {
        login = (ImageButton) findViewById(R.id.btn_main_login);
        register = (Button) findViewById(R.id.btn_main_register);
        btn_main_find_pwd = (Button) findViewById(R.id.btn_main_find_pwd);
        email = (EditText) findViewById(R.id.et_login_email);
        pwd = (EditText) findViewById(R.id.et_login_pwd);
        sv_login = (ScrollView) findViewById(R.id.sv_login);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    final Handler handler;
                    handler = new Handler();

                    final Runnable r = new Runnable() {
                        public void run() {
                            sv_login.smoothScrollTo(0, 450);
                        }
                    };

                    handler.postDelayed(r, 200);
                }
            }
        });

        pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    final Handler handler;
                    handler = new Handler();

                    final Runnable r = new Runnable() {
                        public void run() {
                            sv_login.smoothScrollTo(0, 450);
                        }
                    };

                    handler.postDelayed(r, 200);
                }
            }
        });

        btn_main_find_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchAccountActivity.class);
                startActivity(intent);
            }
        });


    }

    private void facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                Arrays.asList("public_profile", "email"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String id = object.getString("id");
                            String name = object.getString("name");
                            String email = object.getString("email");

                            setSNSLogin(email, name, id, JoinFlag.FACEBOOK);
                        } catch (JSONException e) {
                            log(e);
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
                Log.e("facebookLogin", error.getMessage());
                Toast.makeText(LoginActivity.this, "일시적인 서버장애 입니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
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

                        try {
                            JSONObject json = new JSONObject(response);
                            String id = json.getJSONObject("response").getString("id");
                            String name = json.getJSONObject("response").getString("name");
                            String email = json.getJSONObject("response").getString("email");

                            setSNSLogin(email, name, id, JoinFlag.NAVER);

                        } catch (JSONException e) {
                            log(e);
                        }
                    }
                }).start();

            } else {
//                String errorCode = mOAuthLoginModule.getLastErrorCode(context).getCode();
//                String errorDesc = mOAuthLoginModule.getLastErrorDesc(context);
//                Toast.makeText(context, "errorCode:" + errorCode
//                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void setSNSLogin(final String email, final String name, final String id, final int login_category_id) {
        mainServerConnection = new HeaderInterceptor().getClientForMainServer().create(MainServerConnection.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", id);
        Call<User> call = mainServerConnection.login(map);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    final User userInfo = response.body();

                    if (userInfo == null) {
                        Intent intent = new Intent(LoginActivity.this, SNSRegisterActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("password", id);
                        intent.putExtra("name", name);
                        intent.putExtra("login_category_id", login_category_id);
                        startActivity(intent);
                    } else {
//                        editor = loginInfo.edit();
//                        editor.putInt("user_id", userInfo.getId());
                        setLogin(userInfo);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                log(t);
                Toast.makeText(LoginActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_login:
                et_email = email.getText().toString();
                et_pwd = pwd.getText().toString();

                HashMap<String, String> map = new HashMap<>();
                map.put("email", et_email);
                map.put("password", encrypt_SHA256(et_pwd));

                mainServerConnection = new HeaderInterceptor().getClientForMainServer().create(MainServerConnection.class);
                Call<User> call_login = mainServerConnection.login(map);

                call_login.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            final User userInfo = response.body();
                            if (userInfo == null) {
                                Toast.makeText(LoginActivity.this, "ID / 패스워드를 확인해주세요.", Toast.LENGTH_LONG).show();
                            } else {
                                setLogin(userInfo);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        log(t);
                        Toast.makeText(LoginActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });

                break;

            case R.id.btn_main_register:
                Intent intent = new Intent(v.getContext(), AgreementActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void setLogin(final User userInfo) {
        final String device_id = getDeviceId();
        final String token = FirebaseInstanceId.getInstance().getToken();
        access_token = userInfo.getAccess_token();

        userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
        Call<ResponseBody> call_update_deviceId_token = userServerConnection.update_deviceId_token(userInfo.getId(), token, device_id);
        call_update_deviceId_token.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
                    editor = loginInfo.edit();
                    editor.putInt("user_id", userInfo.getId());
                    editor.putString("device_id", device_id);
                    editor.putString("token", token);
                    editor.putInt("user_level", userInfo.getLevel());
                    editor.putString("access_token", userInfo.getAccess_token());
                    editor.putInt("login_category_id", userInfo.getLogin_category_id());
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                Toast.makeText(LoginActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getDeviceId() {
        final String androidId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
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

    public String encrypt_SHA256(String pwd) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(pwd.getBytes(), 0, pwd.length());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return String.format("%064x", new java.math.BigInteger(1, messageDigest.digest()));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
