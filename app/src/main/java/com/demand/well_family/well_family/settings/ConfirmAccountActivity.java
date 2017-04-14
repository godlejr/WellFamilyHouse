package com.demand.well_family.well_family.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.repository.MainServerConnection;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-03-07.
 */

public class ConfirmAccountActivity extends Activity {

    private TextView tv_confirm_account_name;
    private TextView tv_confirm_account_email;
    private ImageView iv_confirm_account_avatar;
    private RadioButton rb_confirm_account_email;
    private Button btn_confirm_account;
    private RadioGroup rg_confirm_account;

    private MainServerConnection mainServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(ConfirmAccountActivity.class);

    // intent user_info
    private int user_id;
    private String user_email;
    private String user_name;
    private String user_avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_account);

        setToolbar(getWindow().getDecorView());
        init();
    }

    private void setToolbar(View view) {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolBar);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("계정 확인");
    }

    private void init() {
        tv_confirm_account_name = (TextView) findViewById(R.id.tv_confirm_account_name);
        tv_confirm_account_email = (TextView) findViewById(R.id.tv_confirm_account_email);
        iv_confirm_account_avatar = (ImageView) findViewById(R.id.iv_confirm_account_avatar);

        rg_confirm_account = (RadioGroup) findViewById(R.id.rg_confirm_account);
        rb_confirm_account_email = (RadioButton) findViewById(R.id.rb_confirm_account_email);
        btn_confirm_account = (Button) findViewById(R.id.btn_confirm_account);

        user_id = getIntent().getIntExtra("user_id", 0);
        user_email = getIntent().getStringExtra("email");
        user_name = getIntent().getStringExtra("name");
        user_avatar = getIntent().getStringExtra("avatar");

        tv_confirm_account_email.setText(user_email);
        tv_confirm_account_name.setText(user_name);
        Glide.with(ConfirmAccountActivity.this).load(getString(R.string.cloud_front_user_avatar) + user_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_confirm_account_avatar);

        rg_confirm_account.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                rb.setSelected(true);

                if (rb.getId() == R.id.rb_confirm_account_email) {
                    Toast.makeText(ConfirmAccountActivity.this, "이메일로 발송", Toast.LENGTH_SHORT).show();
                }

                btn_confirm_account.setBackgroundResource(R.drawable.round_corner_btn_brown_r10);
                btn_confirm_account.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 임시 비밀번호 발송
                        mainServerConnection = new HeaderInterceptor().getClientForMainServer().create(MainServerConnection.class);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("user_id", String.valueOf(user_id));
                        map.put("email", user_email);
                        map.put("name", user_name);
                        Call<ResponseBody> call_find_password = mainServerConnection.findPassword(map);
                        call_find_password.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(ConfirmAccountActivity.this, user_email + "로 임시 비밀번호가 발송되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(ConfirmAccountActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                log(t);
                                Toast.makeText(ConfirmAccountActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
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
