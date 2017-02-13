//package com.demand.well_family.well_family.memory_sound;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.design.widget.NavigationView;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.text.SpannableString;
//import android.text.style.TextAppearanceSpan;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.demand.well_family.well_family.LoginActivity;
//import com.demand.well_family.well_family.MainActivity;
//import com.demand.well_family.well_family.R;
//import com.demand.well_family.well_family.connection.Server_Connector;
//import com.demand.well_family.well_family.dto.Song;
//import com.demand.well_family.well_family.market.MarketMainActivity;
//import com.demand.well_family.well_family.users.UserActivity;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//
///**
// * Created by Dev-0 on 2017-02-01.
// */
//
//public class SoundCategoryListActivity extends Activity {
//    private TextView toolbar_title;
//    private String category_id;
//    private String category_name;
//
//    private Server_Connector connector;
//    private ArrayList<Song> songList;
//    private RecyclerView rv_song_list;
//
//    //user_info
//    private String user_id;
//    private String user_name;
//    private String user_level;
//    private String user_avatar;
//    private String user_email;
//    private String user_phone;
//    private String user_birth;
//
//    //toolbar
//    private DrawerLayout dl;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_song_list);
//        user_id = getIntent().getStringExtra("user_id");
//        user_name = getIntent().getStringExtra("user_name");
//        user_level = getIntent().getStringExtra("user_level");
//        user_avatar = getIntent().getStringExtra("user_avatar");
//        user_email = getIntent().getStringExtra("user_email");
//        user_phone = getIntent().getStringExtra("user_phone");
//        user_birth = getIntent().getStringExtra("user_birth");
//
//        category_id = getIntent().getStringExtra("category_id");
//        category_name = getIntent().getStringExtra("category_name");
//
//        init();
//        getSongsData();
//        setToolbar(this.getWindow().getDecorView(), this, "추억소리");
//    }
//
//    // toolbar & menu
//    public void setToolbar(View view, Context context, String title) {
//        Log.e("tttttt", user_avatar);
//        NavigationView nv = (NavigationView) view.findViewById(R.id.nv);
//        nv.setItemIconTintList(null);
//        dl = (DrawerLayout) view.findViewById(R.id.dl);
//
//        // toolbar menu, title, back
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
//        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        toolbar_title.setText(title);
//        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
//        toolbar_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 함수 호출
//                setBack();
//            }
//        });
//        ImageView toolbar_menu = (ImageView) toolbar.findViewById(R.id.toolbar_menu);
//        toolbar_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dl.openDrawer(GravityCompat.START);
//            }
//        });
//
//        // header
//        View nv_header_view = nv.getHeaderView(0);
//        LinearLayout ll_menu_mypage = (LinearLayout)nv_header_view.findViewById(R.id.ll_menu_mypage);
//        ll_menu_mypage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dl.closeDrawers();
//
//                Intent intent = new Intent(SoundCategoryListActivity.this, UserActivity.class);
//                //userinfo
//                intent.putExtra("story_user_id", user_id);
//                intent.putExtra("story_user_email", user_email);
//                intent.putExtra("story_user_birth", user_birth);
//                intent.putExtra("story_user_phone", user_phone);
//                intent.putExtra("story_user_name", user_name);
//                intent.putExtra("story_user_level", user_level);
//                intent.putExtra("story_user_avatar", user_avatar);
//
//                intent.putExtra("user_id", user_id);
//                intent.putExtra("user_name", user_name);
//                intent.putExtra("user_avatar", user_avatar);
//                intent.putExtra("user_email", user_email);
//                intent.putExtra("user_birth", user_birth);
//                intent.putExtra("user_phone", user_phone);
//                intent.putExtra("user_level", user_level);
//
//                startActivity(intent);
//
//            }
//        });
//
//        TextView tv_menu_name = (TextView) nv_header_view.findViewById(R.id.tv_menu_name);
//        tv_menu_name.setText(user_name);
//
//        TextView tv_menu_birth = (TextView) nv_header_view.findViewById(R.id.tv_menu_birth);
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = dateFormat.parse(user_birth);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일생");
//            tv_menu_birth.setText(sdf.format(date));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        ImageView iv_menu_avatar = (ImageView) nv_header_view.findViewById(R.id.iv_menu_avatar);
//        Log.e("ttttt5", view + "");
//
//        Glide.with(context).load(getString(R.string.cloud_front_user_avatar) + user_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_menu_avatar);
//
//
//        // menu
//        Menu menu = nv.getMenu();
//        MenuItem menu_all = menu.findItem(R.id.menu_all);
//        SpannableString s = new SpannableString(menu_all.getTitle());
//        s.setSpan(new TextAppearanceSpan(view.getContext(), R.style.NavigationDrawer), 0, s.length(), 0);
//        menu_all.setTitle(s);
//
//        MenuItem menu_apps = menu.findItem(R.id.menu_apps);
//        s = new SpannableString(menu_apps.getTitle());
//        s.setSpan(new TextAppearanceSpan(view.getContext(), R.style.NavigationDrawer), 0, s.length(), 0);
//        menu_apps.setTitle(s);
//
//        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem item) {
//                item.setChecked(true);
//                dl.closeDrawers();
//
//                Intent startLink;
//                Intent intent;
//                switch (item.getItemId()) {
//                    case R.id.menu_home:
//                        intent  = new Intent(SoundCategoryListActivity.this, MainActivity.class);
//                        intent.putExtra("user_id", user_id);
//                        intent.putExtra("user_email", user_email);
//                        intent.putExtra("user_birth", user_birth);
//                        intent.putExtra("user_phone", user_phone);
//                        intent.putExtra("user_name", user_name);
//                        intent.putExtra("user_level", user_level);
//                        intent.putExtra("user_avatar", user_avatar);
//
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//
//                        break;
//
//                    case R.id.menu_search:
//                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.menu_market:
//                        intent = new Intent(SoundCategoryListActivity.this, MarketMainActivity.class);
//                        intent.putExtra("user_id", user_id);
//                        intent.putExtra("user_email", user_email);
//                        intent.putExtra("user_birth", user_birth);
//                        intent.putExtra("user_phone", user_phone);
//                        intent.putExtra("user_name", user_name);
//                        intent.putExtra("user_level", user_level);
//                        intent.putExtra("user_avatar", user_avatar);
//                        startActivity(intent);
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
//                        SharedPreferences loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = loginInfo.edit();
//                        editor.remove("user_id");
//                        editor.remove("user_name");
//                        editor.remove("user_email");
//                        editor.remove("user_birth");
//                        editor.remove("user_avatar");
//                        editor.remove("user_phone");
//                        editor.remove("user_level");
//                        editor.commit();
//
//                        intent = new Intent(SoundCategoryListActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                        break;
//
////                    App 이용하기
//                    case R.id.menu_selffeet:
//                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
//
//                        break;
//                    case R.id.menu_bubblefeet:
//                        startLink = getPackageManager().getLaunchIntentForPackage(getString(R.string.bubblefeet));
//                        if (startLink == null) {
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_front) + getString(R.string.bubblefeet))));
//                        } else {
//                            startActivity(startLink);
//                        }
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
//                        startLink.putExtra("user_id",user_id);
//                        startLink.putExtra("user_level", user_level);
//                        startLink.putExtra("user_email", user_email);
//                        startLink.putExtra("user_phone", user_phone);
//                        startLink.putExtra("user_name", user_name);
//                        startLink.putExtra("user_avatar", user_avatar);
//                        startLink.putExtra("user_birth", user_birth);
//                        startActivity(startLink);
//                        break;
//                }
//                return true;
//            }
//        });
//    }
//    private void setBack(){
//        finish();
//    }
//
//    private class SongViewHolder extends RecyclerView.ViewHolder {
//        private LinearLayout ll_item_song;
//        private ImageView iv_item_song_avatar;
//        private TextView tv_item_song_title, tv_item_song_singer;
//
//        public SongViewHolder(View itemView) {
//            super(itemView);
//            ll_item_song =(LinearLayout)itemView.findViewById(R.id.ll_item_song);
//            iv_item_song_avatar = (ImageView) itemView.findViewById(R.id.iv_item_song_avatar);
//            tv_item_song_title = (TextView) itemView.findViewById(R.id.tv_item_song_title);
//            tv_item_song_singer = (TextView) itemView.findViewById(R.id.tv_item_song_singer);
//
//            ll_item_song.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    connector = new Server_Connector();
//                    connector.execute(getString(R.string.server_url)+songList.get(getAdapterPosition()).getId()+"/Insert_Song_hit");
//
//                    Intent intent = new Intent(SoundCategoryListActivity.this, SoundPlayer.class);
//                    //user info
//                    intent.putExtra("user_id", user_id);
//                    intent.putExtra("user_email", user_email);
//                    intent.putExtra("user_birth", user_birth);
//                    intent.putExtra("user_phone", user_phone);
//                    intent.putExtra("user_name", user_name);
//                    intent.putExtra("user_level", user_level);
//                    intent.putExtra("user_avatar", user_avatar);
//
//                    //song info
//                    intent.putExtra("song_id", songList.get(getAdapterPosition()).getId());
//                    intent.putExtra("song_name", songList.get(getAdapterPosition()).getName());
//                    intent.putExtra("song_ext", songList.get(getAdapterPosition()).getExt());
//                    intent.putExtra("song_title", songList.get(getAdapterPosition()).getTitle());
//                    intent.putExtra("song_singer", songList.get(getAdapterPosition()).getSinger());
//                    intent.putExtra("song_avatar", songList.get(getAdapterPosition()).getAvatar());
//                    intent.putExtra("song_category_id", songList.get(getAdapterPosition()).getCategory_id());
//                    intent.putExtra("song_created_at", songList.get(getAdapterPosition()).getCreated_at());
//
//                    startActivity(intent);
//                }
//            });
//        }
//
//    }
//
//    private class SongAdapter extends RecyclerView.Adapter<SongViewHolder> {
//        private Context context;
//        private ArrayList<Song> songList;
//        private int layout;
//
//        public SongAdapter(Context context, ArrayList<Song> songList, int layout) {
//            this.context = context;
//            this.songList = songList;
//            this.layout = layout;
//        }
//
//        @Override
//        public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            SongViewHolder songViewHolder = new SongViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
//            return songViewHolder;
//        }
//
//
//        @Override
//        public void onBindViewHolder(SongViewHolder holder, int position) {
//            Glide.with(context).load(getString(R.string.cloud_front_songs_avatar) + songList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_item_song_avatar);
//            holder.tv_item_song_title.setText(songList.get(position).getTitle());
//            holder.tv_item_song_singer.setText(songList.get(position).getSinger());
//        }
//
//        @Override
//        public int getItemCount() {
//            return songList.size();
//        }
//    }
//
//    private void getSongsData() {
//        connector = new Server_Connector();
//        songList = new ArrayList<>();
//
//        rv_song_list = (RecyclerView) findViewById(R.id.rv_song_list);
//        connector.execute(getString(R.string.server_url) + category_id + "/song_list_by_Category");
//        try {
//            JSONArray arr = new JSONArray(connector.get().trim());
//            if (arr.length() == 0) {
//                //parsing error
//            } else {
//                for (int i = 0; i < arr.length(); i++) {
//                    JSONObject obj = arr.getJSONObject(i);
//                    String id = String.valueOf(obj.getInt("id"));
//                    String name = obj.getString("name");
//                    String ext = obj.getString("ext");
//                    String title = obj.getString("title");
//                    String singer = obj.getString("singer");
//                    String avatar = obj.getString("avatar");
//                    String category_id = String.valueOf(obj.getInt("category_id"));
//                    String created_at = obj.getString("created_at");
//
//                    songList.add(new Song(id, name, ext, title, singer, avatar, category_id, created_at));
//                }
//            }
//        } catch (Exception e) {
//
//        }
//
//        rv_song_list.setAdapter(new SongAdapter(this, songList, R.layout.item_song));
//        rv_song_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//    }
//
//    private void init() {
//        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
//        toolbar_title.setText(category_name);
//    }
//}
