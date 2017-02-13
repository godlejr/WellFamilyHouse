//package com.demand.well_family.well_family.search;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.demand.well_family.well_family.R;
//import com.demand.well_family.well_family.dto.User;
//import com.demand.well_family.well_family.invite.InviteActivity;
//
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//
///**
// * Created by ㅇㅇ on 2017-02-12.
// */
//
//public class SearchUserActivity extends Activity {
//    private RecyclerView rv_search_user;
//    private Button btn_search_user;
//    private EditText et_search_user;
//    private UserAdapter userAdapter;
//
//    //user_info
//    private String user_id;
//    private String user_name;
//    private String user_avatar;
//    private String user_email;
//    private String user_birth;
//    private String user_phone;
//    private String user_level;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_user);
//
//        init();
//        setToolbar(getWindow().getDecorView());
//    }
//
//    private void init() {
//        user_id = getIntent().getStringExtra("user_id");
//        user_name = getIntent().getStringExtra("user_name");
//        user_avatar = getIntent().getStringExtra("user_avatar");
//        user_email = getIntent().getStringExtra("user_email");
//        user_birth = getIntent().getStringExtra("user_birth");
//        user_phone = getIntent().getStringExtra("user_phone");
//        user_level = getIntent().getStringExtra("user_level");
//
//        et_search_user = (EditText) findViewById(R.id.et_search_user);
//        btn_search_user = (Button) findViewById(R.id.btn_search_user);
//        btn_search_user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (et_search_user.length() != 0) {
//                    // 검색
//                    Toast.makeText(SearchUserActivity.this, "검색~~", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//
//        ArrayList<User> userList = new ArrayList<>();
//        userList.add(new User("1", "email1", "name1", "birth1", "phone1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTZcXRi3FUxjS2tSIDtmOSq3MsK9OeUrEpcy8QyJUlgV-bC090eltwQGOR6", "1"));
//
//        rv_search_user = (RecyclerView) findViewById(R.id.rv_search_user);
//        userAdapter = new UserAdapter(userList, this, R.layout.item_search_user);
//        rv_search_user.setAdapter(userAdapter);
//        rv_search_user.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//    }
//
//    // toolbar_main & menu
//    public void setToolbar(View view) {
//        // toolbar_main
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
//        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
//        toolbar_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 함수 호출
//                setBack();
//            }
//        });
//
//        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        toolbar_title.setText("가족 찾기");
//
//        toolbar.setBackgroundColor(Color.WHITE);
//
//    }
//
//    public void setBack() {
//        finish();
//    }
//
//    private class UserViewHolder extends RecyclerView.ViewHolder {
//        TextView tv_search_user_name;
//        TextView tv_search_user_birth;
//        ImageView iv_search_user_avatar;
//        Button btn_search_user;
//
//        public UserViewHolder(View itemView) {
//            super(itemView);
//
//            tv_search_user_name = (TextView) itemView.findViewById(R.id.tv_search_user_name);
//            tv_search_user_birth = (TextView) itemView.findViewById(R.id.tv_search_user_birth);
//
//            iv_search_user_avatar = (ImageView) itemView.findViewById(R.id.iv_search_user_avatar);
//            btn_search_user = (Button) itemView.findViewById(R.id.btn_search_user);
//
//            btn_search_user.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(SearchUserActivity.this, "버튼~~", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }
//    }
//
//    private class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
//        private ArrayList<User> userList;
//        private Context context;
//        private int layout;
//
//        public UserAdapter(ArrayList<User> userList, Context context, int layout) {
//            this.userList = userList;
//            this.context = context;
//            this.layout = layout;
//        }
//
//        @Override
//        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            UserViewHolder userViewHolder = new UserViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
//            return userViewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(UserViewHolder holder, int position) {
//            Glide.with(SearchUserActivity.this).load(userList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_search_user_avatar);
//            holder.tv_search_user_name.setText(userList.get(position).getName());
//            holder.tv_search_user_birth.setText(userList.get(position).getBirth());
//
//            if (position / 2 == 0) { // 가족인 경우 (조건 걍 했음~~~)
//                holder.btn_search_user.setText("가족 취소");
//                holder.btn_search_user.setTextColor(Color.parseColor("#999999"));
//                holder.btn_search_user.setBackgroundResource(R.drawable.round_corner_border_gray);
//            } else { // 가족 아닌경우
//                holder.btn_search_user.setText("초대하기");
//                holder.btn_search_user.setTextColor(Color.parseColor("#542920"));
//                holder.btn_search_user.setBackgroundResource(R.drawable.round_corner_border_brown);
//            }
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return userList.size();
//        }
//    }
//}
