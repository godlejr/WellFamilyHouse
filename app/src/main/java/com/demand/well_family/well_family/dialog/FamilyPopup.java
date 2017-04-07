package com.demand.well_family.well_family.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.FamilyServerConnection;
import com.demand.well_family.well_family.flag.FamilyJoinFlag;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by ㅇㅇ on 2017-04-04.
 */

public class FamilyPopup extends Activity {
    //user_info
    private int user_id;
    private String user_name;
    private String user_avatar;
    private String user_email;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private String access_token;

    //family_info
    private int family_id;
    private String family_name;
    private String family_content;
    private String family_avatar;
    private int family_user_id;
    private String family_created_at;


    private static final Logger logger = LoggerFactory.getLogger(FamilyPopup.class);
    private SharedPreferences loginInfo;

    private TextView tv_popup_family_title;
    private TextView tv_popup_family_content;
    private ImageButton btn_popup_family_close;
    private ImageView iv_popup_family_avatar;
    private Button btn_popup_family_cancel;
    private Button btn_popup_family_commit;

    private int join_flag;
    private boolean delete_flag = false;
    private FamilyServerConnection familyServerConnection;

    private Intent intent;
    private int position;

    //result code
    private static final int DELETE_USER_TO_FAMILY = 3;
    private static final int JOIN = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_family);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        getWindow().getAttributes().width = (int) (display.getWidth() * 0.9);
        getWindow().getAttributes().height = (int) (display.getHeight() * 0.8);

        intent = getIntent();
        position = intent.getIntExtra("position", 0);
        delete_flag = intent.getBooleanExtra("delete_flag", false);
        setUserInfo();
        setFamilyInfo();
        init();
    }

    private void setUserInfo() {
        loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
        user_id = loginInfo.getInt("user_id", 0);
//        user_level = loginInfo.getInt("user_level", 0);
        user_name = loginInfo.getString("user_name", null);
        user_email = loginInfo.getString("user_email", null);
        user_birth = loginInfo.getString("user_birth", null);
        user_avatar = loginInfo.getString("user_avatar", null);
        user_phone = loginInfo.getString("user_phone", null);
        access_token = loginInfo.getString("access_token", null);
    }

    private void setFamilyInfo() {
        join_flag = getIntent().getIntExtra("join_flag", 0);

        family_id = intent.getIntExtra("family_id", 0);
        family_user_id = intent.getIntExtra("family_user_id", 0);
        family_name = intent.getStringExtra("family_name");
        family_content = intent.getStringExtra("family_content");
        family_avatar = intent.getStringExtra("family_avatar");
        family_created_at = intent.getStringExtra("family_created_at");
    }

    private void init() {
        tv_popup_family_title = (TextView) findViewById(R.id.tv_popup_family_title);
        tv_popup_family_content = (TextView) findViewById(R.id.tv_popup_family_content);
        btn_popup_family_close = (ImageButton) findViewById(R.id.btn_popup_family_close);
        iv_popup_family_avatar = (ImageView) findViewById(R.id.iv_popup_family_avatar);
        btn_popup_family_cancel = (Button) findViewById(R.id.btn_popup_family_cancel);
        btn_popup_family_commit = (Button) findViewById(R.id.btn_popup_family_commit);

        btn_popup_family_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        btn_popup_family_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (join_flag == FamilyJoinFlag.FAMILY_TO_USER) {
            // 초대 승인
            tv_popup_family_title.setText("가족 가입");
            tv_popup_family_content.setText("\'" + family_name + "\' 가족이 " + user_name + "님을 \n가족으로 초대하였습니다.");
            Glide.with(FamilyPopup.this).load(getString(R.string.cloud_front_family_avatar) + family_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_popup_family_avatar);
            btn_popup_family_cancel.setText("거절");
            btn_popup_family_commit.setText("수락");
            btn_popup_family_commit.setBackgroundResource(R.drawable.round_corner_green);
            btn_popup_family_commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    familyServerConnection = new HeaderInterceptor(access_token).getClientForFamilyServer().create(FamilyServerConnection.class);
                    Call<Void> call_update_user_for_familyjoin = familyServerConnection.update_user_for_familyjoin(family_id, user_id);
                    call_update_user_for_familyjoin.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(FamilyPopup.this, "\"" + family_name + "\" 에 가입되었습니다.", Toast.LENGTH_SHORT).show();
                                intent.putExtra("position", position);
                                setResult(JOIN, intent);
                                finish();
                            } else {
                                Toast.makeText(FamilyPopup.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            log(t);
                            Toast.makeText(FamilyPopup.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

        if (join_flag == FamilyJoinFlag.FAMILY) {
            /// 가족 탈퇴
            tv_popup_family_title.setText("가족 탈퇴");
            tv_popup_family_content.setText("가족 페이지를 탈퇴할 경우, \n\'" + family_name + "\' 가족과 함께 나누었던 소중한 추억들을 \n확인하실 수 없습니다.\n그래도 \'" + family_name + "\' 가족 페이지를 탈퇴하시겠습니까?");
            Glide.with(FamilyPopup.this).load(getString(R.string.cloud_front_family_avatar) + family_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_popup_family_avatar);
            btn_popup_family_cancel.setText("취소");
            btn_popup_family_commit.setText("탈퇴");
            btn_popup_family_commit.setBackgroundResource(R.drawable.round_corner_red);
            btn_popup_family_commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    familyServerConnection = new HeaderInterceptor(access_token).getClientForFamilyServer().create(FamilyServerConnection.class);
                    Call<ResponseBody> call_delete_user_from_family = familyServerConnection.delete_user_from_family(family_id, user_id);
                    call_delete_user_from_family.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(FamilyPopup.this, "\"" + family_name + "\" 에서 탈퇴하였습니다.", Toast.LENGTH_SHORT).show();
                                intent.putExtra("position", position);
                                setResult(DELETE_USER_TO_FAMILY, intent);
                                finish();
                            } else {
                                Toast.makeText(FamilyPopup.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            log(t);
                            Toast.makeText(FamilyPopup.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

        if (join_flag == FamilyJoinFlag.FAMILY_TO_USER) {
            // 가입 승인
            tv_popup_family_title.setText("가족 승인하기");
            tv_popup_family_content.setText(user_name + "회원님이 \'" + family_name + "\' 가족의 구성원이 되고싶어합니다.");
            Glide.with(FamilyPopup.this).load(getString(R.string.cloud_front_user_avatar) + family_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_popup_family_avatar);
            btn_popup_family_cancel.setText("거절");
            btn_popup_family_commit.setText("수락");
            btn_popup_family_commit.setBackgroundResource(R.drawable.round_corner_green);
            btn_popup_family_commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }

        if (delete_flag) {
            ///가족 삭제
            tv_popup_family_title.setText("가족 삭제");
            tv_popup_family_content.setText("가족 페이지를 삭제할 경우, \n\'" + family_name + "\' 가족과 함께 나누었던 소중한 추억들이 \n모두 삭제됩니다.\n그래도 \"" + family_name + "\" 가족 페이지를 삭제하시겠습니까?");
            Glide.with(FamilyPopup.this).load(getString(R.string.cloud_front_family_avatar) + family_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_popup_family_avatar);
            btn_popup_family_cancel.setText("취소");
            btn_popup_family_commit.setText("삭제");
            btn_popup_family_commit.setBackgroundResource(R.drawable.round_corner_red);
            btn_popup_family_commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    familyServerConnection = new HeaderInterceptor(access_token).getClientForFamilyServer().create(FamilyServerConnection.class);
                    Call<ResponseBody> call_delete_family = familyServerConnection.delete_family(family_id);
                    call_delete_family.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(FamilyPopup.this, "가족이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                intent.putExtra("position", position);
                                setResult(DELETE_USER_TO_FAMILY, intent);
                                finish();
                            } else {
                                Toast.makeText(FamilyPopup.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            log(t);
                            Toast.makeText(FamilyPopup.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
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

    @Override
    public void onBackPressed() {
//        Intent intent = getIntent();
//        intent.putExtra("position", position);
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
