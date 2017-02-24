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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dto.Check;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.FamilyActivity;
import com.demand.well_family.well_family.log.LogFlag;
import com.demand.well_family.well_family.users.UserActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demand.well_family.well_family.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-02-12.
 */

public class SearchUserActivity extends Activity {
    private RecyclerView rv_search_user;
    private Button btn_search_user;
    private EditText et_search_user;
    private UserAdapter userAdapter;

    //user_info
    private int user_id;
    private String user_name;
    private String user_avatar;
    private String user_email;
    private String user_birth;
    private String user_phone;
    private int user_level;

    //family_info
    private int family_id;
    private String family_name;
    private String family_content;
    private String family_avatar;
    private int family_user_id;
    private String family_created_at;

    private Server_Connection server_connection;
    private ArrayList<User> userList;

    private static final Logger logger = LoggerFactory.getLogger(SearchUserActivity.class);
    private SharedPreferences loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        setUserInfo();
        finishList.add(this);
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
        setToolbar(getWindow().getDecorView());
    }

    private void init() {
        family_id = getIntent().getIntExtra("family_id", 0);
        family_name = getIntent().getStringExtra("family_name");
        family_content = getIntent().getStringExtra("family_content");
        family_avatar = getIntent().getStringExtra("family_avatar");
        family_user_id = getIntent().getIntExtra("family_user_id", 0);
        family_created_at = getIntent().getStringExtra("family_created_at");

        et_search_user = (EditText) findViewById(R.id.et_search_user);
        btn_search_user = (Button) findViewById(R.id.btn_search_user);
        btn_search_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_search_user.getText().toString().length() != 0) {
                    // 검색
                    server_connection = Server_Connection.retrofit.create(Server_Connection.class);

                    HashMap<String, String> map = new HashMap<>();
                    map.put("search", et_search_user.getText().toString());
                    Call<ArrayList<User>> call_user = server_connection.find_user(user_id, map);
                    call_user.enqueue(new Callback<ArrayList<User>>() {
                        @Override
                        public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                            userList = response.body();
                            rv_search_user = (RecyclerView) findViewById(R.id.rv_search_user);
                            userAdapter = new UserAdapter(userList, SearchUserActivity.this, R.layout.item_search_user);
                            rv_search_user.setAdapter(userAdapter);
                            rv_search_user.setLayoutManager(new LinearLayoutManager(SearchUserActivity.this, LinearLayoutManager.VERTICAL, false));
                        }

                        @Override
                        public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                            log(t);
                            Toast.makeText(SearchUserActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    // toolbar_main & menu
    public void setToolbar(View view) {
        // toolbar_main
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBack();
            }
        });

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("가족 찾기");
        toolbar.setBackgroundColor(Color.WHITE);
    }

    public void setBack() {
        Intent intent = new Intent(SearchUserActivity.this, FamilyActivity.class);

        //family info
        intent.putExtra("family_id", family_id);
        intent.putExtra("family_name", family_name);
        intent.putExtra("family_content", family_content);
        intent.putExtra("family_avatar", family_avatar);
        intent.putExtra("family_user_id", family_user_id);
        intent.putExtra("family_created_at", family_created_at);

        startActivity(intent);
        finish();
    }

    private class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_search_user_name;
        TextView tv_search_user_birth;
        ImageView iv_search_user_avatar;
        Button btn_search_user;

        public UserViewHolder(View itemView) {
            super(itemView);

            tv_search_user_name = (TextView) itemView.findViewById(R.id.tv_search_user_name);
            tv_search_user_birth = (TextView) itemView.findViewById(R.id.tv_search_user_birth);

            iv_search_user_avatar = (ImageView) itemView.findViewById(R.id.iv_search_user_avatar);
            btn_search_user = (Button) itemView.findViewById(R.id.btn_search_user);

            tv_search_user_name.setOnClickListener(this);
            iv_search_user_avatar.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_search_user_name:
                case R.id.iv_search_user_avatar:
                    Intent intent = new Intent(SearchUserActivity.this, UserActivity.class);
                    //userinfo
                    intent.putExtra("story_user_id", userList.get(getAdapterPosition()).getId());
                    intent.putExtra("story_user_email", userList.get(getAdapterPosition()).getEmail());
                    intent.putExtra("story_user_birth", userList.get(getAdapterPosition()).getBirth());
                    intent.putExtra("story_user_phone", userList.get(getAdapterPosition()).getPhone());
                    intent.putExtra("story_user_name", userList.get(getAdapterPosition()).getName());
                    intent.putExtra("story_user_level", userList.get(getAdapterPosition()).getLevel());
                    intent.putExtra("story_user_avatar", userList.get(getAdapterPosition()).getAvatar());

                    startActivity(intent);
                    break;
            }
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
        private ArrayList<User> userList;
        private Context context;
        private int layout;

        public UserAdapter(ArrayList<User> userList, Context context, int layout) {
            this.userList = userList;
            this.context = context;
            this.layout = layout;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            UserViewHolder userViewHolder = new UserViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return userViewHolder;
        }

        @Override
        public void onBindViewHolder(final UserViewHolder holder, final int position) {
            Glide.with(SearchUserActivity.this).load(getString(R.string.cloud_front_user_avatar) + userList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_search_user_avatar);
            holder.tv_search_user_name.setText(userList.get(position).getName());
            holder.tv_search_user_birth.setText(userList.get(position).getBirth());


            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", String.valueOf(user_id));
            map.put("family_id", String.valueOf(family_id));

            Call<ArrayList<Check>> call_family_check = server_connection.family_user_check(userList.get(position).getId(), map);
            call_family_check.enqueue(new Callback<ArrayList<Check>>() {
                @Override
                public void onResponse(Call<ArrayList<Check>> call, Response<ArrayList<Check>> response) {
                    if (response.body().get(0).getChecked() > 0) {
                        //family
                        holder.btn_search_user.setText("가족 취소");
                        holder.btn_search_user.setTextColor(Color.parseColor("#999999"));
                        holder.btn_search_user.setBackgroundResource(R.drawable.round_corner_border_gray);
                        holder.btn_search_user.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("user_id", String.valueOf(userList.get(position).getId()));
                                Call<ResponseBody> call_invite = server_connection.delete_user_from_family(family_id, map);
                                call_invite.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast.makeText(SearchUserActivity.this, userList.get(position).getName() + "님이 가족에서 탈퇴되었습니다.", Toast.LENGTH_LONG).show();
                                        userAdapter.notifyItemChanged(position);
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        log(t);
                                        Toast.makeText(SearchUserActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });

                    } else {
                        if (user_id == userList.get(position).getId()) {
                            //Me
                            holder.btn_search_user.setText("나");
                            holder.btn_search_user.setTextColor(Color.parseColor("#542920"));
                            holder.btn_search_user.setBackgroundResource(R.drawable.round_corner_border_gray);

                            holder.btn_search_user.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                        } else {
                            //public
                            holder.btn_search_user.setText("초대하기");
                            holder.btn_search_user.setTextColor(Color.parseColor("#542920"));
                            holder.btn_search_user.setBackgroundResource(R.drawable.round_corner_border_brown);
                            holder.btn_search_user.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("user_id", String.valueOf(userList.get(position).getId()));
                                    Call<ResponseBody> call_invite = server_connection.insert_user_into_family(family_id, map);
                                    call_invite.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            Toast.makeText(SearchUserActivity.this, userList.get(position).getName() + "님이 가족이 되었습니다.", Toast.LENGTH_LONG).show();
                                            userAdapter.notifyItemChanged(position);
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            log(t);
                                            Toast.makeText(SearchUserActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Check>> call, Throwable t) {
                    log(t);
                    Toast.makeText(SearchUserActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchUserActivity.this, FamilyActivity.class);

        //family info
        intent.putExtra("family_id", family_id);
        intent.putExtra("family_name", family_name);
        intent.putExtra("family_content", family_content);
        intent.putExtra("family_avatar", family_avatar);
        intent.putExtra("family_user_id", family_user_id);
        intent.putExtra("family_created_at", family_created_at);

        startActivity(intent);
        finish();
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
