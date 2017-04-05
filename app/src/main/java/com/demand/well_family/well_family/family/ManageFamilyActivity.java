package com.demand.well_family.well_family.family;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.demand.well_family.well_family.connection.UserServerConnection;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.search.SearchFamilyActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-04-03.
 */

public class ManageFamilyActivity extends Activity {

    //user_info
    private int user_id;
    private String user_email;
    private String user_name;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private String user_avatar;
    private String access_token;

    private static final Logger logger = LoggerFactory.getLogger(FamilyActivity.class);
    private SharedPreferences loginInfo;

    private TextView tv_find_family;
    private LinearLayout ll_manage_family_owner;

    private RecyclerView rv_manage_family_owner;
    private RecyclerView rv_manage_family_member;

    private ArrayList<Family> ownerFamilyList;
    private ArrayList<Family> memberFamilyList;

    private OwnerFamilyAdapter ownerFamilyAdapter;
    private FamilyAdapter familyAdapter;

    private UserServerConnection userServerConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family);

        setUserInfo();
        init();
        getFamilyData();
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

    // toolbar_main & menu
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
        ll_manage_family_owner = (LinearLayout) findViewById(R.id.ll_manage_family_owner);  // 멤버면 INVISIBLE
        tv_find_family = (TextView) findViewById(R.id.tv_find_family);

        tv_find_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), SearchFamilyActivity.class);
                startActivity(intent);

            }
        });


    }

    private void getFamilyData() {
        rv_manage_family_member = (RecyclerView) findViewById(R.id.rv_manage_family_member);
        rv_manage_family_owner = (RecyclerView) findViewById(R.id.rv_manage_family_owner);

        // 내가 개설한 가족
        ownerFamilyList = new ArrayList<>();
        ownerFamilyList.add(new Family(2, "혜연댁", "내용1", "4.PNG", 67, "2017-01-18 20:08:05"));
        ownerFamilyList.add(new Family(2, "혜연댁", "내용1", "4.PNG", 67, "2017-01-18 20:08:05"));
        ownerFamilyList.add(new Family(2, "혜연댁", "내용1", "4.PNG", 67, "2017-01-18 20:08:05"));

        ownerFamilyAdapter = new OwnerFamilyAdapter(ownerFamilyList, R.layout.item_manage_family_owner, ManageFamilyActivity.this);
        rv_manage_family_owner.setAdapter(ownerFamilyAdapter);
        rv_manage_family_owner.setLayoutManager(new LinearLayoutManager(ManageFamilyActivity.this, LinearLayoutManager.HORIZONTAL, false));


        // 내가 멤버인 가족
        userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
        Call<ArrayList<Family>> call_family_info = userServerConnection.family_Info(user_id);
        call_family_info.enqueue(new Callback<ArrayList<Family>>() {
            @Override
            public void onResponse(Call<ArrayList<Family>> call, Response<ArrayList<Family>> response) {
                familyAdapter = new FamilyAdapter(response.body(), R.layout.item_manage_family_member, ManageFamilyActivity.this);
                rv_manage_family_member.setAdapter(familyAdapter);
                rv_manage_family_member.setLayoutManager(new LinearLayoutManager(ManageFamilyActivity.this, LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onFailure(Call<ArrayList<Family>> call, Throwable t) {
                log(t);
                Toast.makeText(ManageFamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

    }

    private class OwnerFamilyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView iv_manage_family_owner;
        private TextView tv_manage_family_owner;

        public OwnerFamilyViewHolder(View itemView) {
            super(itemView);

            iv_manage_family_owner = (CircleImageView) itemView.findViewById(R.id.iv_manage_family_owner);
            tv_manage_family_owner = (TextView) itemView.findViewById(R.id.tv_manage_family_owner);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), FamilyActivity.class);

                    intent.putExtra("family_id", ownerFamilyList.get(getAdapterPosition()).getId());
                    intent.putExtra("family_name", ownerFamilyList.get(getAdapterPosition()).getName());
                    intent.putExtra("family_content", ownerFamilyList.get(getAdapterPosition()).getContent());
                    intent.putExtra("family_avatar", ownerFamilyList.get(getAdapterPosition()).getAvatar());
                    intent.putExtra("family_user_id", ownerFamilyList.get(getAdapterPosition()).getUser_id());
                    intent.putExtra("family_created_at", ownerFamilyList.get(getAdapterPosition()).getCreated_at());

                    startActivity(intent);

                }
            });
        }
    }

    private class OwnerFamilyAdapter extends RecyclerView.Adapter<OwnerFamilyViewHolder> {
        private ArrayList<Family> familyList;
        private int layout;
        private LayoutInflater inflater;
        private Context context;

        public OwnerFamilyAdapter(ArrayList<Family> familyList, int layout, Context context) {
            this.familyList = familyList;
            this.layout = layout;
            this.context = context;
        }

        @Override
        public OwnerFamilyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            OwnerFamilyViewHolder viewHolder = new OwnerFamilyViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(OwnerFamilyViewHolder holder, int position) {
            holder.tv_manage_family_owner.setText(familyList.get(position).getName());
            Glide.with(context).load(getString(R.string.cloud_front_family_avatar) + familyList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_manage_family_owner);
        }

        @Override
        public int getItemCount() {
            return familyList.size();
        }
    }

    private class FamilyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView iv_manage_family_member;
        private TextView tv_manage_family_member;
        private Button btn_manage_family_member;

        public FamilyViewHolder(View itemView) {
            super(itemView);

            iv_manage_family_member = (CircleImageView) itemView.findViewById(R.id.iv_manage_family_member);
            tv_manage_family_member = (TextView) itemView.findViewById(R.id.tv_manage_family_member);
            btn_manage_family_member = (Button) itemView.findViewById(R.id.btn_manage_family_member);
        }
    }

    private class FamilyAdapter extends RecyclerView.Adapter<FamilyViewHolder> {
        private ArrayList<Family> familyList;
        private int layout;
        private Context context;

        public FamilyAdapter(ArrayList<Family> familyList, int layout, Context context) {
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
        public void onBindViewHolder(FamilyViewHolder holder, int position) {
            holder.tv_manage_family_member.setText(familyList.get(position).getName());
            Glide.with(context).load(getString(R.string.cloud_front_family_avatar) + familyList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_manage_family_member);

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
