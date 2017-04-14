package com.demand.well_family.well_family.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.main.login.activity.LoginActivity;
import com.demand.well_family.well_family.R;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-04-07.
 */

public class TextPopup extends Activity {
    private TextView tv_popup_text;
    private Button btn_popup_text_cancel;
    private Button btn_popup_text_commit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_text);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        getWindow().getAttributes().width = (int) (display.getWidth() * 0.9);
        getWindow().getAttributes().height = (int) (display.getHeight() * 0.4);

        init();
    }

    private void init() {
        tv_popup_text = (TextView) findViewById(R.id.tv_popup_text);
        tv_popup_text.setText("계정을 비활성화하시면 더이상 가족들의 소식을 받아보실 수 없습니다.\n정말 계정을 비활성화 하시겠습니까?");

        btn_popup_text_cancel = (Button) findViewById(R.id.btn_popup_text_cancel);
        btn_popup_text_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_popup_text_commit = (Button) findViewById(R.id.btn_popup_text_commit);
        btn_popup_text_commit.setOnClickListener(new View.OnClickListener() {
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

                Toast.makeText(TextPopup.this, "계정이 비활성화 되었습니다.", Toast.LENGTH_SHORT).show();

                int finishListSize = finishList.size();

                for (int i = 0; i < finishListSize; i++) {
                    finishList.get(i).finish();
                }

                Intent intent = new Intent(TextPopup.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
