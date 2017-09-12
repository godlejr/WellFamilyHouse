package com.demand.well_family.well_family.main.login.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.main.agreement.activity.AgreementActivity;
import com.demand.well_family.well_family.main.base.activity.MainActivity;
import com.demand.well_family.well_family.main.login.presenter.LoginPresenter;
import com.demand.well_family.well_family.main.login.presenter.impl.LoginPresenterImpl;
import com.demand.well_family.well_family.main.login.view.LoginView;
import com.demand.well_family.well_family.main.sns.activity.JoinFromSNSActivity;
import com.demand.well_family.well_family.setting.searchAccount.activity.SearchAccountActivity;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import java.util.ArrayList;
import java.util.Arrays;


public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener, View.OnFocusChangeListener {
    private LoginPresenter loginPresenter;
    private Handler progressDialogHandler;
    private ProgressDialog progressDialog;

    //demand
    private ImageButton btn_main_login;
    private EditText et_login_email, et_login_pwd;
    private Button btn_main_register;
    private Button btn_main_find_pwd;
    private ScrollView sv_login;

    //facebook
    private ImageButton facebookLoginButton;

    //kakao
    private Button kakaoLoginButton;
    private SessionCallback mKakaocallback;


    //naver
    private OAuthLoginButton oAuthLoginButton;
    private ImageButton naverLoginButton;

    //activity list
    public static ArrayList<Activity> finishList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPresenter = new LoginPresenterImpl(this);

        loginPresenter.onBeforeCreate();
        setContentView(R.layout.activity_login);
        loginPresenter.onCreate();
    }


    @Override
    public void initFacebookLogin() {
        FacebookSdk.sdkInitialize(this);
    }

    @Override
    public void initNaverLogin(OAuthLogin oAuthLogin, String naverClientId, String naverClientSecret, String naverClientName) {
        oAuthLogin.init(this, naverClientId, naverClientSecret, naverClientName);
    }


    @Override
    public void init() {
        finishList.add(this);

        this.progressDialogHandler = new Handler();
        this.progressDialog = new ProgressDialog(this);

        btn_main_login = (ImageButton) findViewById(R.id.btn_main_login);
        btn_main_register = (Button) findViewById(R.id.btn_main_register);
        btn_main_find_pwd = (Button) findViewById(R.id.btn_main_find_pwd);
        et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
        sv_login = (ScrollView) findViewById(R.id.sv_login);

        //facebook
        facebookLoginButton = (ImageButton) findViewById(R.id.facebookLoginButton);
        facebookLoginButton.setOnClickListener(this);

        //kakao
        kakaoLoginButton = (Button) findViewById(R.id.kakaoLoginButton);
        kakaoLoginButton.setOnClickListener(this);

        //naver
        oAuthLoginButton = (OAuthLoginButton) findViewById(R.id.oAuthLoginButton);
        naverLoginButton = (ImageButton) findViewById(R.id.naverLoginButton);
        naverLoginButton.bringToFront();
        oAuthLoginButton.setOAuthLoginHandler(loginPresenter.getOAuthLoginHandler());
        naverLoginButton.setOnClickListener(this);

        //demand
        btn_main_login.setOnClickListener(this);
        btn_main_register.setOnClickListener(this);
        btn_main_find_pwd.setOnClickListener(this);
        et_login_email.setOnFocusChangeListener(this);
        et_login_pwd.setOnFocusChangeListener(this);
    }

    @Override
    public String getDeviceId() {
        return android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID).toString();
    }

    @Override
    public String getFireBaseToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    public void setLoginSmoothScrollTo(int x, int y) {
        sv_login.smoothScrollTo(x, y);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToSearchAccountActivity() {
        Intent intent = new Intent(this, SearchAccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToAgreementActivity() {
        Intent intent = new Intent(this, AgreementActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToJoinFromSNSActivity(User user) {
        Intent intent = new Intent(LoginActivity.this, JoinFromSNSActivity.class);
        intent.putExtra("email", user.getEmail());
        intent.putExtra("password", user.getPassword());
        intent.putExtra("name", user.getName());
        intent.putExtra("login_category_id", user.getLogin_category_id());
        startActivity(intent);
    }

    @Override
    public String getOAuthLoginResponse(OAuthLogin oAuthLogin, String url) {
        String token = oAuthLogin.getAccessToken(this);
        String response = oAuthLogin.requestApi(this, token, url);
        return response;
    }

    @Override
    public void isKakaoLogin() {
        mKakaocallback = new SessionCallback();
        com.kakao.auth.Session.getCurrentSession().addCallback(mKakaocallback);
        com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
        com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, LoginActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_find_pwd:
                navigateToSearchAccountActivity();
                break;

            case R.id.facebookLoginButton:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                loginPresenter.onClickFacebookLogin();
                break;

            case R.id.kakaoLoginButton:
                loginPresenter.onClickKakaoLogin();
                break;

            case R.id.naverLoginButton:
                loginPresenter.getNaverOAuthLogin().startOauthLoginActivity(this, loginPresenter.getOAuthLoginHandler());
                break;

            case R.id.btn_main_login:
                String email = et_login_email.getText().toString();
                String password = et_login_pwd.getText().toString();
                loginPresenter.onClickLogin(email, password);
                break;

            case R.id.btn_main_register:
                navigateToAgreementActivity();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        int finishListSize = finishList.size();

        for (int i = 0; i < finishListSize; i++) {
            finishList.get(i).finish();
        }

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        loginPresenter.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_login_email:
            case R.id.et_login_pwd:
                loginPresenter.onLoginFocusChange(hasFocus);
                break;
        }
    }

    public class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            KakaoRequestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Log.e("KAKAO_LOGIN", exception.getMessage());
            }
        }
    }

    protected void KakaoRequestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;

                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(getApplicationContext(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("KAKAO_LOGIN", "오류로 카카오로그인 실패 ");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("KAKAO_LOGIN", "오류로 카카오로그인 실패 ");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                loginPresenter.onSuccessKakaoLogin(userProfile);
            }

            @Override
            public void onNotSignedUp() {
                // 자동가입이 아닐경우 동의창
            }
        });
    }


    @Override
    public void showProgressDialog() {
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_dialog);
    }

    @Override
    public void goneProgressDialog() {
        progressDialogHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, 200);
    }
}
