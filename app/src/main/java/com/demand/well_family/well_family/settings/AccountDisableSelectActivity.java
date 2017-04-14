package com.demand.well_family.well_family.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.main.login.activity.LoginActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.TextPopup;
import com.demand.well_family.well_family.flag.LogFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ㅇㅇ on 2017-03-22.
 */

public class AccountDisableSelectActivity extends Activity {
    private Button btn_account_disable;
    private RadioGroup rg_account_disable;
    private FrameLayout[] frameLayouts;
    private Spinner sp_account_disable;
    private ArrayAdapter<String> spinnerAdapter;
    private static final Logger logger = LoggerFactory.getLogger(AccountDisableSelectActivity.class);

    // action
    private TextView tv_account_disable_logout;
    private TextView tv_account_disable_help1;
    private TextView tv_account_disable_help2;
    private TextView tv_account_disable_help3;
    private TextView tv_account_disable_setting1;
    private TextView tv_account_disable_setting2;
    private TextView tv_account_disable_invite;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_disable_select);

        init();
    }

    public void setToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("계정 비활성화");
    }

    private void init() {
        setToolbar(getWindow().getDecorView());
        setSpinner();

        frameLayouts = new FrameLayout[8];
        frameLayouts[0] = (FrameLayout) findViewById(R.id.fl_account_disable1);
        frameLayouts[1] = (FrameLayout) findViewById(R.id.fl_account_disable2);
        frameLayouts[2] = (FrameLayout) findViewById(R.id.fl_account_disable3);
        frameLayouts[3] = (FrameLayout) findViewById(R.id.fl_account_disable4);
        frameLayouts[4] = (FrameLayout) findViewById(R.id.fl_account_disable5);
        frameLayouts[5] = (FrameLayout) findViewById(R.id.fl_account_disable6);
        frameLayouts[6] = (FrameLayout) findViewById(R.id.fl_account_disable7);
        frameLayouts[7] = (FrameLayout) findViewById(R.id.fl_account_disable8);

        rg_account_disable = (RadioGroup) findViewById(R.id.rg_account_disable);
        rg_account_disable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_account_disable1) {
                    setVisible(0);

                    tv_account_disable_logout = (TextView) findViewById(R.id.tv_account_disable_logout);
                    tv_account_disable_logout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = loginInfo.edit();
                            editor.remove("user_id");
                            editor.remove("user_name");
                            editor.remove("user_email");
                            editor.remove("user_birth");
                            editor.remove("user_avatar");
                            editor.remove("user_phone");
                            editor.remove("user_level");
                            editor.remove("access_token");
                            editor.commit();

                            intent = new Intent(AccountDisableSelectActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(AccountDisableSelectActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (checkedId == R.id.rb_account_disable2) {
                    setVisible(1);
                    tv_account_disable_help1 = (TextView) findViewById(R.id.tv_account_disable_help1);
                    tv_account_disable_help1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 고객센터 문의
                        }
                    });

                } else if (checkedId == R.id.rb_account_disable3) {
                    setVisible(2);
                    tv_account_disable_setting1 = (TextView) findViewById(R.id.tv_account_disable_setting1);
                    tv_account_disable_setting1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 알림 설정
                            finish();
                        }
                    });
                } else if (checkedId == R.id.rb_account_disable4) {
                    setVisible(3);
                    tv_account_disable_help2 = (TextView) findViewById(R.id.tv_account_disable_help2);
                    tv_account_disable_help2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 고객센터 문의
                        }
                    });
                } else if (checkedId == R.id.rb_account_disable5) {
                    setVisible(4);
                    tv_account_disable_help3 = (TextView) findViewById(R.id.tv_account_disable_help3);
                    tv_account_disable_help3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 게시글 신고 방법
                        }
                    });
                } else if (checkedId == R.id.rb_account_disable6) {
                    setVisible(5);
                    tv_account_disable_setting2 = (TextView) findViewById(R.id.tv_account_disable_setting2);
                    tv_account_disable_setting2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 계정 보호하기
                            finish();
                        }
                    });
                } else if (checkedId == R.id.rb_account_disable7) {
                    setVisible(6);
                    tv_account_disable_invite = (TextView) findViewById(R.id.tv_account_disable_invite);
                    tv_account_disable_invite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 가족 초대하기
                        }
                    });
                } else if (checkedId == R.id.rb_account_disable8) {
                    // 다른 계정
                    setVisible(8);
                } else if (checkedId == R.id.rb_account_disable9) {
                    setVisible(7);
                }

                btn_account_disable.setBackgroundResource(R.drawable.round_corner_btn_brown_r10);
            }
        });

        btn_account_disable = (Button) findViewById(R.id.btn_account_disable);
        btn_account_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountDisableSelectActivity.this, TextPopup.class);
                startActivity(intent);

            }
        });
    }

    private void setSpinner() {
        sp_account_disable = (Spinner) findViewById(R.id.sp_account_disable);
        final String spinnerArray[] = {"7일", "14일", "30일"};

        spinnerAdapter = new ArrayAdapter<String>(AccountDisableSelectActivity.this, R.layout.custom_spinner_item, spinnerArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setBackgroundColor(Color.WHITE);
                tv.setTextColor(Color.parseColor("#424242"));
//                tv.append("  ▼");
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setBackgroundColor(Color.WHITE);
                tv.setTextColor(Color.parseColor("#424242"));
                tv.setGravity(Gravity.CENTER);
                return view;
            }
        };
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_item);

        sp_account_disable.setAdapter(spinnerAdapter);
        sp_account_disable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AccountDisableSelectActivity.this, spinnerArray[position] + " 후 계정이 재활성화됩니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setVisible(int position) {
        for (int i = 0; i < 8; i++) {
            frameLayouts[i].setVisibility(View.GONE);
        }
        if (position < 8) {
            frameLayouts[position].setVisibility(View.VISIBLE);
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

}
