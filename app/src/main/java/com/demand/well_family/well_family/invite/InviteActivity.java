//package com.demand.well_family.well_family.invite;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.NavigationView;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.demand.well_family.well_family.R;
//import com.demand.well_family.well_family.dto.User;
//import com.demand.well_family.well_family.memory_sound.SoundMainActivity;
//
//import java.util.ArrayList;
//
///**
// * Created by ㅇㅇ on 2017-01-19.
// */
//
//public class InviteActivity extends Activity {
//    private RecyclerView rv_invite;
//    private EditText et_invite_search;
//    private ImageView iv_invite_search;
//    private ArrayList<User> userList;
//
//    //toolbar_main & menu
//    private DrawerLayout dl;
//    private ActionBarDrawerToggle toggle;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_invite_family);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
//        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        toolbarTitle.setText("가족초대");
//
//        init();
//        setToolbar();
//    }
//
//    private void init() {
//        rv_invite = (RecyclerView) findViewById(R.id.rv_invite);
//        iv_invite_search = (ImageView) findViewById(R.id.iv_invite_search);
//        et_invite_search = (EditText) findViewById(R.id.et_invite_search);
//
//        // test
//        userList = new ArrayList<>();
//        userList.add(new User("id1", "name1", "content1", "avatar1","5","6","7"));
//        userList.add(new User("id1", "name2", "content2", "avatar1","5","6","7"));
//        userList.add(new User("id1", "name3", "content3", "avatar1","5","6","7"));
//        userList.add(new User("id1", "name4", "content4", "avatar1","5","6","7"));
//        userList.add(new User("id1", "name5", "content5", "avatar1","5","6","7"));
//        userList.add(new User("id1", "name6", "content6", "avatar1","5","6","7"));
//        userList.add(new User("id1", "name6", "content6", "avatar1","5","6","7"));
//        userList.add(new User("id1", "name6", "content6", "avatar1","5","6","7"));
//        userList.add(new User("id1", "name6", "content6", "avatar1","5","6","7"));
//        userList.add(new User("id1", "name6", "content6", "avatar1","5","6","7"));
//
//
//        // 검색 버튼(돋보기)
//        iv_invite_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (et_invite_search.getText().length() != 0) {
//                    Log.e("tt","Search EditText 비어있음");
//
//                    rv_invite.setAdapter(new UserAdapter(userList, v.getContext(), R.layout.item_invite_family));
//                }
//            }
//        });
//
//        rv_invite.setLayoutManager(new GridLayoutManager(this, 3));
//    }
//
//    private class UserViewHolder extends RecyclerView.ViewHolder {
//        TextView tv_item_invite_name;
//        ImageView iv_item_invite_avatar;
//        Button btn_item_invite;
//
//        public UserViewHolder(View itemView) {
//            super(itemView);
//
//            btn_item_invite = (Button) itemView.findViewById(R.id.btn_item_invite);
//            tv_item_invite_name = (TextView) itemView.findViewById(R.id.tv_item_invite_name);
//            iv_item_invite_avatar = (ImageView) itemView.findViewById(R.id.iv_item_invite_avatar);
//
//            // 초대 버튼 클릭
//            btn_item_invite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                }
//            });
//
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
//            holder.tv_item_invite_name.setText(userList.get(position).getName());
//        }
//
//        @Override
//        public int getItemCount() {
//            return userList.size();
//        }
//    }
//
//
//    // toolbar_main & menu
//    private void setToolbar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
//        toolbar.setNavigationIcon(R.drawable.memu);
//        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
//
//        dl = (DrawerLayout) findViewById(R.id.dl);
//        toggle = new ActionBarDrawerToggle(this, dl, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        toggle.setDrawerIndicatorEnabled(false);
//        toggle.setHomeAsUpIndicator(R.mipmap.ic_launcher);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dl.openDrawer(Gravity.LEFT);
//            }
//        });
//
//        NavigationView nv = (NavigationView) findViewById(R.id.nv);
//        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                // slide drawer menuItem action
//                item.setChecked(true);
//                dl.closeDrawers();
//
//                Intent startLink;
//                switch (item.getItemId()) {
//                    case R.id.menu_home:
//
//                        break;
//
//                    case R.id.menu_search:
//                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.menu_market:
//
//                        break;
//
//                    case R.id.menu_setting:
//                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.menu_help:
//                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.menu_logout:
//
//                        break;
//
////                    App 이용하기
//                    case R.id.menu_selffeet:
//                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
//
//                        break;
//                    case R.id.menu_bubblefeet:
//                        startLink = getPackageManager().getLaunchIntentForPackage(getString(R.string.bubblefeet));
//                        startActivity(startLink);
//                        break;
//
//                    case R.id.menu_happyfeet:
//                        startLink = getPackageManager().getLaunchIntentForPackage(getString(R.string.happyfeet));
//                        if (startLink == null) {
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_front) + getString(R.string.happyfeet))));
//                        } else {
//                            startActivity(startLink);
//                        }
//                        break;
//
//                    case R.id.menu_memory_sound:
//                        startLink = new Intent(getApplicationContext(), SoundMainActivity.class);
//                        startLink.putExtra("user_id", getIntent().getStringExtra("user_id"));
//                        startLink.putExtra("user_level", getIntent().getStringExtra("user_level"));
//                        startLink.putExtra("user_email", getIntent().getStringExtra("user_email"));
//                        startLink.putExtra("user_phone", getIntent().getStringExtra("user_phone"));
//                        startLink.putExtra("user_name", getIntent().getStringExtra("user_name"));
//                        startLink.putExtra("user_avatar", getIntent().getStringExtra("user_avatar"));
//                        startLink.putExtra("user_birth", getIntent().getStringExtra("user_birth"));
//                        startActivity(startLink);
//                        break;
//                }
//                return true;
//            }
//        });
//
//    }
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//// Sync the toggle state after onRestoreInstanceState has occurred.
//        toggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        toggle.onConfigurationChanged(newConfig);
//    }
//
//}
