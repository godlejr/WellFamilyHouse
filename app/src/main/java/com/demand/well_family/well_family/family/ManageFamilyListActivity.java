package com.demand.well_family.well_family.family;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.FamilyServerConnection;
import com.demand.well_family.well_family.dialog.FamilyPopup;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ㅇㅇ on 2017-04-05.
 */

public class ManageFamilyListActivity extends Activity {

    //user_info
    private int user_id;
    private String user_email;
    private String user_name;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private String user_avatar;
    private String access_token;

    //family_info
    private int family_id;
    private String family_name;
    private String family_content;
    private String family_avatar;
    private int family_user_id;
    private String family_created_at;

    private CircleImageView iv_manage_family_avatar;
    private TextView tv_manage_family_name;
    private TextView tv_manage_family_content;
    private Button btn_manage_family_remove;
    private RecyclerView rv_manage_family_join;

    private static final Logger logger = LoggerFactory.getLogger(FamilyActivity.class);
    private SharedPreferences loginInfo;

    private FamilyServerConnection familyServerConnection;
    private int position;

    //request code
    private static final int DELETE_FAMILY = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_list);

        setUserInfo();
        init();
    }

    private void setUserInfo() {
        loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
        user_id = loginInfo.getInt("user_id", 0);
        user_level = loginInfo.getInt("user_level", 0);
        user_name = loginInfo.getString("user_name", null);
        user_email = loginInfo.getString("user_email", null);
        user_birth = loginInfo.getString("user_birth", null);
        user_avatar = loginInfo.getString("user_avatar", null);
        user_phone = loginInfo.getString("user_phone", null);
        access_token = loginInfo.getString("access_token", null);
        setToolbar(this.getWindow().getDecorView());
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
        toolbar_title.setText("가족 설정");
    }

    private void init() {
        tv_manage_family_name = (TextView) findViewById(R.id.tv_manage_family_name);
        tv_manage_family_content = (TextView) findViewById(R.id.tv_manage_family_content);
        iv_manage_family_avatar = (CircleImageView) findViewById(R.id.iv_manage_family_avatar);
        btn_manage_family_remove = (Button) findViewById(R.id.btn_manage_family_remove);
        rv_manage_family_join = (RecyclerView) findViewById(R.id.rv_manage_family_join);

        Intent intent = getIntent();
        family_id = intent.getIntExtra("family_id", 0);
        family_user_id = intent.getIntExtra("family_user_id", 0);
        family_name = intent.getStringExtra("family_name");
        family_content = intent.getStringExtra("family_content");
        family_avatar = intent.getStringExtra("family_avatar");
        family_created_at = intent.getStringExtra("family_created_at");

        tv_manage_family_name.setText(family_name);
        tv_manage_family_content.setText(family_content);
        Glide.with(this).load(getString(R.string.cloud_front_family_avatar) + family_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_manage_family_avatar);

        btn_manage_family_remove.setOnClickListener(new View.OnClickListener() { // 가족 삭제
            @Override
            public void onClick(View v) {
                Intent remove_intent = new Intent(ManageFamilyListActivity.this, FamilyPopup.class);
                remove_intent.putExtra("family_id", family_id);
                remove_intent.putExtra("family_user_id", family_user_id);
                remove_intent.putExtra("family_name", family_name);
                remove_intent.putExtra("family_content", family_content);
                remove_intent.putExtra("family_avatar", family_avatar);
                remove_intent.putExtra("family_created_at", family_created_at);
                remove_intent.putExtra("delete_flag", true);
                startActivityForResult(remove_intent, DELETE_FAMILY);
            }
        });
    }

    class FamilyJoinViewHolder extends RecyclerView.ViewHolder {

        public FamilyJoinViewHolder(View itemView) {
            super(itemView);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_FAMILY) {
            if (resultCode == RESULT_OK) {
                position = data.getIntExtra("position", 0);

                Intent intent = getIntent();
                intent.putExtra("position", position);
                setResult(DELETE_FAMILY, intent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("position", position);
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
