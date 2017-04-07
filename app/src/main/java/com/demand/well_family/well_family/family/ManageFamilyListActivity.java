package com.demand.well_family.well_family.family;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.FamilyServerConnection;
import com.demand.well_family.well_family.dialog.FamilyPopup;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.flag.FamilyJoinFlag;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    // request code
    private static final int DELETE_FAMILY = 5;

    // result code
    private static final int DELETE_USER_TO_FAMILY = 3;

    // adapter
    private FamilyJoinAdapter familyJoinAdapter;

    private ArrayList<UserInfoForFamilyJoin> joiners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_list);

        setUserInfo();
        setJoinersInfo();
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

    private void setFamilyInfo() {
        Intent intent = getIntent();
        family_id = intent.getIntExtra("family_id", 0);
        family_user_id = intent.getIntExtra("family_user_id", 0);
        family_name = intent.getStringExtra("family_name");
        family_content = intent.getStringExtra("family_content");
        family_avatar = intent.getStringExtra("family_avatar");
        family_created_at = intent.getStringExtra("family_created_at");
        position = intent.getIntExtra("position", 0);
    }

    private void setJoinersInfo() {
        setFamilyInfo();

        rv_manage_family_join = (RecyclerView) findViewById(R.id.rv_manage_family_join);
        familyServerConnection = new HeaderInterceptor(access_token).getClientForFamilyServer().create(FamilyServerConnection.class);

        Call<ArrayList<UserInfoForFamilyJoin>> call_family_joiners = familyServerConnection.family_joiners(family_id);
        call_family_joiners.enqueue(new Callback<ArrayList<UserInfoForFamilyJoin>>() {
            @Override
            public void onResponse(Call<ArrayList<UserInfoForFamilyJoin>> call, Response<ArrayList<UserInfoForFamilyJoin>> response) {
                if (response.isSuccessful()) {
                    joiners = response.body();
                    familyJoinAdapter = new FamilyJoinAdapter(joiners, ManageFamilyListActivity.this, R.layout.item_manage_family_join);
                    rv_manage_family_join.setAdapter(familyJoinAdapter);
                    rv_manage_family_join.setLayoutManager(new LinearLayoutManager(ManageFamilyListActivity.this, LinearLayoutManager.VERTICAL, false));

                } else {
                    Toast.makeText(ManageFamilyListActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserInfoForFamilyJoin>> call, Throwable t) {
                log(t);
                Toast.makeText(ManageFamilyListActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

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
                remove_intent.putExtra("position", position);
                remove_intent.putExtra("delete_flag", true);
                startActivityForResult(remove_intent, DELETE_FAMILY);
            }
        });
    }

    class FamilyJoinViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView iv_manage_family_join_avatar;
        private TextView tv_manage_family_join_name;
        private Button btn_manage_family_join;

        public FamilyJoinViewHolder(View itemView) {
            super(itemView);

            iv_manage_family_join_avatar = (CircleImageView) itemView.findViewById(R.id.iv_manage_family_join_avatar);
            tv_manage_family_join_name = (TextView) itemView.findViewById(R.id.tv_manage_family_join_name);
            btn_manage_family_join = (Button) itemView.findViewById(R.id.btn_manage_family_join);
        }
    }

    class FamilyJoinAdapter extends RecyclerView.Adapter<FamilyJoinViewHolder> {
        private ArrayList<UserInfoForFamilyJoin> joinsList;
        private Context context;
        private int layout;

        public FamilyJoinAdapter(ArrayList<UserInfoForFamilyJoin> joinsList, Context context, int layout) {
            this.joinsList = joinsList;
            this.context = context;
            this.layout = layout;
        }

        @Override
        public FamilyJoinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FamilyJoinViewHolder viewHolder = new FamilyJoinViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(FamilyJoinViewHolder holder, final int position) {
            Glide.with(context).load(getString(R.string.cloud_front_user_avatar) + joinsList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_manage_family_join_avatar);
            holder.tv_manage_family_join_name.setText(joinsList.get(position).getName());

            int flag = joinsList.get(position).getJoin_flag();

            if (flag == FamilyJoinFlag.USER_TO_FAMILY) {
                // 가입 승인
                holder.btn_manage_family_join.setText("가입 승인");
                holder.btn_manage_family_join.setBackgroundResource(R.drawable.round_corner_border_green);
                holder.btn_manage_family_join.setTextColor(Color.parseColor("#1DDB16"));

                holder.btn_manage_family_join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        familyServerConnection = new HeaderInterceptor(access_token).getClientForFamilyServer().create(FamilyServerConnection.class);
                        Call<Void> call_update_user_for_familyjoin = familyServerConnection.update_user_for_familyjoin(family_id, joinsList.get(position).getId());
                        call_update_user_for_familyjoin.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(ManageFamilyListActivity.this, "가입을 승인하셨습니다.", Toast.LENGTH_SHORT).show();

                                    joinsList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
                                } else {
                                    Toast.makeText(ManageFamilyListActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                log(t);
                                Toast.makeText(ManageFamilyListActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }

            if (flag == FamilyJoinFlag.FAMILY_TO_USER) {
                // 요청 대기
                holder.btn_manage_family_join.setText("요청 대기");
                holder.btn_manage_family_join.setBackgroundResource(R.drawable.round_corner_border_gray);
                holder.btn_manage_family_join.setTextColor(Color.parseColor("#999999"));
            }

        }

        @Override
        public int getItemCount() {
            return joinsList.size();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_FAMILY) {
            if (resultCode == DELETE_USER_TO_FAMILY) {
                position = data.getIntExtra("position", 0);

                Intent intent = getIntent();
                intent.putExtra("position", position);
                intent.putExtra("delete", true);
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