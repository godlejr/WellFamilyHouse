package com.demand.well_family.well_family.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.dto.FamilyInfoForFamilyJoin;
import com.demand.well_family.well_family.family.base.activity.FamilyActivity;
import com.demand.well_family.well_family.flag.FamilyJoinFlag;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-04-03.
 */

public class SearchFamilyActivity extends Activity {
    // user_info
    private int user_id;
    private String user_email;
    private String user_name;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private String user_avatar;
    private String access_token;

    private static final Logger logger = LoggerFactory.getLogger(SearchFamilyActivity.class);
    private SharedPreferences loginInfo;

    private Button btn_find_family;
    private EditText et_find_family;
    private RecyclerView rv_find_family;
    private String search;
    private FamilyAdapter familyAdapter;

    private ArrayList<FamilyInfoForFamilyJoin> familyList;
    private UserServerConnection userServerConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_family);

        setUserInfo();
        init();
    }

    private void init() {
        btn_find_family = (Button) findViewById(R.id.btn_find_family);
        et_find_family = (EditText) findViewById(R.id.et_find_family);

        btn_find_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = et_find_family.getText().toString();
                if (search.length() != 0) {
                    // 검색
                    HashMap<String, String> map = new HashMap<>();
                    map.put("search", search);

                    userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
                    Call<ArrayList<FamilyInfoForFamilyJoin>> call_find_family = userServerConnection.find_family(user_id, map);
                    call_find_family.enqueue(new Callback<ArrayList<FamilyInfoForFamilyJoin>>() {
                        @Override
                        public void onResponse(Call<ArrayList<FamilyInfoForFamilyJoin>> call, Response<ArrayList<FamilyInfoForFamilyJoin>> response) {
                            if (response.isSuccessful()) {
                                familyList = response.body();
                                getFamilyData(familyList);
                            } else {
                                Toast.makeText(SearchFamilyActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<FamilyInfoForFamilyJoin>> call, Throwable t) {
                            log(t);
                            Toast.makeText(SearchFamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });


                } else {
                    Toast.makeText(SearchFamilyActivity.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        setToolbar(getWindow().getDecorView());
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
        toolbar_title.setText("가족 찾기");
        toolbar.setBackgroundColor(Color.WHITE);
    }

    private void getFamilyData(ArrayList<FamilyInfoForFamilyJoin> familyList) {
        rv_find_family = (RecyclerView) findViewById(R.id.rv_find_family);
        familyAdapter = new FamilyAdapter(familyList, R.layout.item_find_family, SearchFamilyActivity.this);
        rv_find_family.setAdapter(familyAdapter);
        rv_find_family.setLayoutManager(new LinearLayoutManager(SearchFamilyActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    private class FamilyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_search_family;
        private TextView tv_search_family_name;
        private TextView tv_search_family_content;
        private Button btn_search_family_commit;
        private CircleImageView iv_search_family_avatar;

        public FamilyViewHolder(View itemView) {
            super(itemView);

            iv_search_family_avatar = (CircleImageView) itemView.findViewById(R.id.iv_search_family_avatar);
            tv_search_family_name = (TextView) itemView.findViewById(R.id.tv_search_family_name);
            tv_search_family_content = (TextView) itemView.findViewById(R.id.tv_search_family_content);
            btn_search_family_commit = (Button) itemView.findViewById(R.id.btn_search_family_commit);
            ll_search_family = (LinearLayout) itemView.findViewById(R.id.ll_search_family);
        }
    }

    private class FamilyAdapter extends RecyclerView.Adapter<FamilyViewHolder> {
        private ArrayList<FamilyInfoForFamilyJoin> familyList;
        private int layout;
        private Context context;

        public FamilyAdapter(ArrayList<FamilyInfoForFamilyJoin> familyList, int layout, Context context) {
            this.familyList = familyList;
            this.layout = layout;
            this.context = context;
        }

        @Override
        public FamilyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FamilyViewHolder viewHolder = new FamilyViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final FamilyViewHolder holder, final int position) {
            Glide.with(context).load(getString(R.string.cloud_front_family_avatar) + familyList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_search_family_avatar);
            holder.tv_search_family_name.setText(familyList.get(position).getName());
            holder.tv_search_family_content.setText(familyList.get(position).getContent());

            holder.ll_search_family.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), FamilyActivity.class);

                    intent.putExtra("family_id", familyList.get(position).getId());
                    intent.putExtra("family_name", familyList.get(position).getName());
                    intent.putExtra("family_content", familyList.get(position).getContent());
                    intent.putExtra("family_avatar", familyList.get(position).getAvatar());
                    intent.putExtra("family_user_id", familyList.get(position).getUser_id());
                    intent.putExtra("family_created_at", familyList.get(position).getCreated_at());

                    startActivity(intent);
                }
            });

            int join_flag = familyList.get(position).getJoin_flag();

            if (join_flag == FamilyJoinFlag.FAMILY) {
                // 가족
                if (user_id == familyList.get(position).getUser_id()) {
                    holder.btn_search_family_commit.setText("가족");
                    holder.btn_search_family_commit.setBackgroundResource(R.drawable.round_corner_green_r30);
                    holder.btn_search_family_commit.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    holder.btn_search_family_commit.setText("가족");
                    holder.btn_search_family_commit.setBackgroundResource(R.drawable.round_corner_green_r30);
                    holder.btn_search_family_commit.setTextColor(Color.parseColor("#ffffff"));
                }
            }

            if (join_flag == FamilyJoinFlag.USER_TO_FAMILY) {
                // 요청 대기
                holder.btn_search_family_commit.setText("승인 대기");
                holder.btn_search_family_commit.setBackgroundResource(R.drawable.round_corner_border_gray_r30);
                holder.btn_search_family_commit.setTextColor(Color.parseColor("#999999"));
            }


            if (join_flag == FamilyJoinFlag.USERS) {
                // 가입 하기
                holder.btn_search_family_commit.setText("가입하기");
                holder.btn_search_family_commit.setBackgroundResource(R.drawable.round_corner_border_green_r30);
                holder.btn_search_family_commit.setTextColor(Color.parseColor("#51BD86"));

                holder.btn_search_family_commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("creator_id", String.valueOf(familyList.get(position).getUser_id()));
                        map.put("family_id", String.valueOf(familyList.get(position).getId()));
                        map.put("family_name", String.valueOf(familyList.get(position).getName()));

                        userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
                        Call<ResponseBody> call_join_family = userServerConnection.join_family(user_id, map);
                        call_join_family.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(SearchFamilyActivity.this, "가입 신청이 완료되었습니다.", Toast.LENGTH_LONG).show();

                                    holder.btn_search_family_commit.setText("승인 대기");
                                    holder.btn_search_family_commit.setBackgroundResource(R.drawable.round_corner_border_gray_r30);
                                    holder.btn_search_family_commit.setTextColor(Color.parseColor("#999999"));

                                    familyList.get(position).setJoin_flag(FamilyJoinFlag.USER_TO_FAMILY);
                                    familyAdapter.notifyItemChanged(position);
                                } else {
                                    Toast.makeText(SearchFamilyActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                log(t);
                                Toast.makeText(SearchFamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return familyList.size();
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
