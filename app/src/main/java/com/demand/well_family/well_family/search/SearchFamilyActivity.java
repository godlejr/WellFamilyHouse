package com.demand.well_family.well_family.search;

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
import android.util.Log;
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
import com.demand.well_family.well_family.MainActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.UserServerConnection;
import com.demand.well_family.well_family.dialog.FamilyPopup;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.family.FamilyActivity;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_family);

        setUserInfo();
        getFamilyData();
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

    private void getFamilyData() {
        rv_find_family = (RecyclerView) findViewById(R.id.rv_find_family);

        ArrayList<Family> familyList = new ArrayList<>();
        familyList.add(new Family(2, "혜연댁", "내용1", "4.PNG", 67, "2017-01-18 20:08:05"));
        familyList.add(new Family(2, "혜연댁", "내용1", "4.PNG", 67, "2017-01-18 20:08:05"));
        familyList.add(new Family(2, "혜연댁", "내용1", "4.PNG", 67, "2017-01-18 20:08:05"));
        familyList.add(new Family(2, "혜연댁", "내용1", "4.PNG", 67, "2017-01-18 20:08:05"));

        familyAdapter = new FamilyAdapter(familyList, R.layout.item_find_family, SearchFamilyActivity.this);
        rv_find_family.setAdapter(familyAdapter);
        rv_find_family.setLayoutManager(new LinearLayoutManager(SearchFamilyActivity.this, LinearLayoutManager.VERTICAL, false));

    }

    private class FamilyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_search_family;
        private TextView tv_search_family_name;
        private TextView tv_search_family_content;
        private Button btn_search_family_remove;
        private CircleImageView iv_search_family_avatar;

        public FamilyViewHolder(View itemView) {
            super(itemView);

            iv_search_family_avatar = (CircleImageView) itemView.findViewById(R.id.iv_search_family_avatar);
            tv_search_family_name = (TextView) itemView.findViewById(R.id.tv_search_family_name);
            tv_search_family_content = (TextView) itemView.findViewById(R.id.tv_search_family_content);
            btn_search_family_remove = (Button) itemView.findViewById(R.id.btn_search_family_remove);
            ll_search_family = (LinearLayout) itemView.findViewById(R.id.ll_search_family);

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
        public void onBindViewHolder(FamilyViewHolder holder, final int position) {
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

            holder.btn_search_family_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), FamilyPopup.class);

//                    intent.putExtra("avatar", );
//                    intent.putExtra("flag", );

                    startActivity(intent);
                }
            });

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
