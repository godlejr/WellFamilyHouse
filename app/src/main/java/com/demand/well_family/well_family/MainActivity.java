package com.demand.well_family.well_family;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.create_family.CreateFamilyActivity;
import com.demand.well_family.well_family.dto.App;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.FamilyActivity;
import com.demand.well_family.well_family.log.LogFlag;
import com.demand.well_family.well_family.market.MarketMainActivity;
import com.demand.well_family.well_family.memory_sound.SongMainActivity;
import com.demand.well_family.well_family.users.UserActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demand.well_family.well_family.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-01-18.
 */

public class MainActivity extends Activity implements View.OnClickListener {
    private ViewPager viewPager;
    private ImageView viewPager_prev, viewPager_next;
    private RecyclerView rv_family, rv_apps;
    private LinearLayout ll_family_container_family;
    private LayoutInflater layoutInflater;
    private ImageButton btn_family_add_exist;

    private int user_id;
    private String user_email;
    private String user_name;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private String user_avatar;

    private ArrayList<Family> familyList;

    private ArrayList<App> appList;

    //toolbar_main & menu
    private DrawerLayout dl;
    private ActionBarDrawerToggle toggle;
    private Server_Connection server_connection;

    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView iv_img_alarm = (ImageView) findViewById(R.id.iv_img_alarm);
        Glide.with(MainActivity.this).load(getString(R.string.cloud_front_banners) + "notification.jpg").thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_img_alarm);

        finishList.add(this);

        setFCMService();

        setUserInfo();
        init();
        getFamilyData();
        getAppData();
    }

    private void setUserInfo() {
        SharedPreferences loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
        user_id = loginInfo.getInt("user_id", 0);
        user_level = loginInfo.getInt("user_level", 0);

        resetUserInfo();

    }

    private void resetUserInfo() {
        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(user_id));


        Call<ArrayList<User>> call_user_info = server_connection.user_Info(map);
        call_user_info.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                ArrayList<User> user = response.body();
                SharedPreferences loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = loginInfo.edit();

                editor.putString("user_name", user.get(0).getName());
                editor.putString("user_email", user.get(0).getEmail());
                editor.putString("user_birth", user.get(0).getBirth());
                editor.putString("user_avatar", user.get(0).getAvatar());
                editor.putString("user_phone", user.get(0).getPhone());

                editor.apply();

                user_name = user.get(0).getName();
                user_email = user.get(0).getEmail();
                user_birth = user.get(0).getBirth();
                user_avatar = user.get(0).getAvatar();
                user_phone = user.get(0).getPhone();

                setToolbar(MainActivity.this.getWindow().getDecorView());
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                log(t);
                Toast.makeText(MainActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setFCMService() {
        //FirebaseMessaging.getInstance().subscribeToTopic("news");
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("ttt", token);
    }

    private class ViewPageAdapter extends PagerAdapter {
        LayoutInflater inflater;

        public ViewPageAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = inflater.inflate(R.layout.viewpager_childview, null);
            ImageView img = (ImageView) view.findViewById(R.id.iv_viewPager_childView);
            if (position == 0) {
                Glide.with(MainActivity.this).load(getString(R.string.cloud_front_banners) + "demand_banner1.jpg").thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
            } else if (position == 1) {
                Glide.with(MainActivity.this).load(getString(R.string.cloud_front_banners) + "demand_banner2.jpg").thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
            } else {
                Glide.with(MainActivity.this).load(getString(R.string.cloud_front_banners) + "demand_banner3.jpg").thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
            }

            container.addView(view);

            return view;
        }

        @Override
        public boolean isViewFromObject(View v, Object obj) {
            return v == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onClick(View v) {
        int position;

        switch (v.getId()) {
            case R.id.iv_viewPager_prev:
                position = viewPager.getCurrentItem();
                viewPager.setCurrentItem(position - 1, true);

                break;
            case R.id.iv_viewPager_next:
                position = viewPager.getCurrentItem();
                viewPager.setCurrentItem(position + 1, true);

                break;
        }
    }

    private void getFamilyData() {
        familyList = new ArrayList<>();

        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ll_family_container_family = (LinearLayout) findViewById(R.id.ll_family_container_family);

        btn_family_add_exist = (ImageButton) findViewById(R.id.btn_family_add_exist);

        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        Call<ArrayList<Family>> call = server_connection.family_Info(user_id);
        call.enqueue(new Callback<ArrayList<Family>>() {
            @Override
            public void onResponse(Call<ArrayList<Family>> call, Response<ArrayList<Family>> response) {
                familyList = response.body();
                if (familyList.size() == 0) {
                    //가족이 없습니다.
                    btn_family_add_exist.setVisibility(View.GONE);
                    layoutInflater.inflate(R.layout.item_family_make, ll_family_container_family, true);
                    View ll_family_make = findViewById(R.id.ll_family_make);
                    ll_family_make.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), CreateFamilyActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    btn_family_add_exist.setVisibility(View.VISIBLE);
                    layoutInflater.inflate(R.layout.item_family_list, ll_family_container_family, true);
                    rv_family = (RecyclerView) ll_family_container_family.findViewById(R.id.rv_family);
                    rv_family.setAdapter(new FamilyAdapter(MainActivity.this, familyList, R.layout.item_users_familys));
                    rv_family.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Family>> call, Throwable t) {
                log(t);
                Toast.makeText(MainActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });


        btn_family_add_exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateFamilyActivity.class);
                startActivity(intent);
            }
        });
    }

    private class FamilyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_family_item;
        private TextView tv_family_name;

        public FamilyViewHolder(View itemView) {
            super(itemView);
            iv_family_item = (ImageView) itemView.findViewById(R.id.iv_family_item);
            tv_family_name = (TextView) itemView.findViewById(R.id.tv_family_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, FamilyActivity.class);

                    //family info
                    intent.putExtra("family_id", familyList.get(getAdapterPosition()).getId());
                    intent.putExtra("family_name", familyList.get(getAdapterPosition()).getName());
                    intent.putExtra("family_content", familyList.get(getAdapterPosition()).getContent());
                    intent.putExtra("family_avatar", familyList.get(getAdapterPosition()).getAvatar());
                    intent.putExtra("family_user_id", familyList.get(getAdapterPosition()).getUser_id());
                    intent.putExtra("family_created_at", familyList.get(getAdapterPosition()).getCreated_at());

                    startActivity(intent);
                }
            });
        }
    }

    private class FamilyAdapter extends RecyclerView.Adapter<FamilyViewHolder> {
        private Context context;
        private ArrayList<Family> familyList;
        private int layout;

        public FamilyAdapter(Context context, ArrayList<Family> familyList, int layout) {
            this.context = context;
            this.familyList = familyList;
            this.layout = layout;
        }

        @Override
        public FamilyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FamilyViewHolder familyViewHolder = new FamilyViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return familyViewHolder;
        }

        @Override
        public void onBindViewHolder(FamilyViewHolder holder, int position) {

            Glide.with(context).load(getString(R.string.cloud_front_family_avatar) + familyList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_family_item);
            holder.tv_family_name.setText(familyList.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return familyList.size();
        }
    }

    private void getAppData() {
        appList = new ArrayList<>();
        appList.add(new App("추억소리", R.drawable.memory_sound));
        appList.add(new App("셀핏", R.drawable.selffeet));
        appList.add(new App("해핏", R.drawable.happyfeet, "com.demand.happyfeet"));
        appList.add(new App("버블핏", R.drawable.bubblefeet, "com.demand.bubblefeet"));
        appList.add(new App("Good Buddy", R.drawable.goodbuddy, "healthcare.nhis.GoodBuddy"));

        rv_apps = (RecyclerView) findViewById(R.id.rv_apps);
        rv_apps.setAdapter(new AppAdapter(appList, this, R.layout.item_apps));
        rv_apps.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private class AppViewHolder extends RecyclerView.ViewHolder {
        TextView tv_app_name;
        ImageView iv_app_img;

        public AppViewHolder(View itemView) {
            super(itemView);

            tv_app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
            iv_app_img = (ImageView) itemView.findViewById(R.id.iv_app_img);

            iv_app_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (getAdapterPosition() == 0) {
                        Intent intent = new Intent(MainActivity.this, SongMainActivity.class);
                        startActivity(intent);
                    } else if (getAdapterPosition() == 1) {
                        //셀핏
                    } else {
                        Intent startLink = getPackageManager().getLaunchIntentForPackage(appList.get(getAdapterPosition()).getPackageName());
                        if (startLink == null) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appList.get(getAdapterPosition()).getPackageName() + "")));
                        } else {
                            startActivity(startLink);
                        }
                    }
                }
            });
        }
    }

    private class AppAdapter extends RecyclerView.Adapter<AppViewHolder> {
        private ArrayList<App> appList;
        private Context context;
        private int layout;

        public AppAdapter(ArrayList<App> appList, Context context, int layout) {
            this.appList = appList;
            this.context = context;
            this.layout = layout;
        }

        @Override
        public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            AppViewHolder appsViewHolder = new AppViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return appsViewHolder;
        }

        @Override
        public void onBindViewHolder(AppViewHolder holder, int position) {
            holder.tv_app_name.setText(appList.get(position).getName());
            holder.iv_app_img.setImageResource(appList.get(position).getImage());
        }

        @Override
        public int getItemCount() {
            return appList.size();
        }
    }

    // toolbar_main & menu
    public void setToolbar(View view) {
        NavigationView nv = (NavigationView) view.findViewById(R.id.nv);
        nv.setItemIconTintList(null);
        dl = (DrawerLayout) view.findViewById(R.id.dl);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("홈");
        ImageView toolbar_market = (ImageView) toolbar.findViewById(R.id.toolbar_market);
        toolbar_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.closeDrawers();

                Intent intent = new Intent(v.getContext(), MarketMainActivity.class);
                startActivity(intent);

            }
        });
        ImageView toolbar_menu = (ImageView) toolbar.findViewById(R.id.toolbar_menu);
        toolbar_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.openDrawer(GravityCompat.START);
            }
        });

        // header
        View nv_header_view = nv.getHeaderView(0);
        LinearLayout ll_menu_mypage = (LinearLayout) nv_header_view.findViewById(R.id.ll_menu_mypage);
        ll_menu_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.closeDrawers();

                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                //userinfo
                intent.putExtra("story_user_id", user_id);
                intent.putExtra("story_user_email", user_email);
                intent.putExtra("story_user_birth", user_birth);
                intent.putExtra("story_user_phone", user_phone);
                intent.putExtra("story_user_name", user_name);
                intent.putExtra("story_user_level", user_level);
                intent.putExtra("story_user_avatar", user_avatar);

                startActivity(intent);
            }
        });

        TextView tv_menu_name = (TextView) nv_header_view.findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(user_name);


        TextView tv_menu_birth = (TextView) nv_header_view.findViewById(R.id.tv_menu_birth);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(user_birth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일생");
            tv_menu_birth.setText(sdf.format(date));
        } catch (ParseException e) {
            log(e);
        }

        ImageView iv_menu_avatar = (ImageView) nv_header_view.findViewById(R.id.iv_menu_avatar);
        Glide.with(view.getContext()).load(getString(R.string.cloud_front_user_avatar) + user_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_menu_avatar);

        // menu
        Menu menu = nv.getMenu();
        MenuItem menu_all = menu.findItem(R.id.menu_all);
        SpannableString s = new SpannableString(menu_all.getTitle());
        s.setSpan(new TextAppearanceSpan(view.getContext(), R.style.NavigationDrawer), 0, s.length(), 0);
        menu_all.setTitle(s);

        MenuItem menu_apps = menu.findItem(R.id.menu_apps);
        s = new SpannableString(menu_apps.getTitle());
        s.setSpan(new TextAppearanceSpan(view.getContext(), R.style.NavigationDrawer), 0, s.length(), 0);
        menu_apps.setTitle(s);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                dl.closeDrawers();

                Intent startLink;
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.menu_home:

                        break;

                    case R.id.menu_search:
                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_market:

                        intent = new Intent(MainActivity.this, MarketMainActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.menu_setting:
                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_help:
                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_logout:

                        SharedPreferences loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = loginInfo.edit();
                        editor.remove("user_id");
                        editor.remove("user_name");
                        editor.remove("user_email");
                        editor.remove("user_birth");
                        editor.remove("user_avatar");
                        editor.remove("user_phone");
                        editor.remove("user_level");
                        editor.commit();

                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.menu_selffeet:
                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.menu_bubblefeet:

                        startLink = getPackageManager().getLaunchIntentForPackage(getString(R.string.bubblefeet));
                        if (startLink == null) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_front) + getString(R.string.bubblefeet))));
                        } else {
                            startActivity(startLink);
                        }
                        break;

                    case R.id.menu_happyfeet:

                        startLink = getPackageManager().getLaunchIntentForPackage(getString(R.string.happyfeet));
                        if (startLink == null) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_front) + getString(R.string.happyfeet))));
                        } else {
                            startActivity(startLink);
                        }
                        break;

                    case R.id.menu_memory_sound:

                        startLink = new Intent(getApplicationContext(), SongMainActivity.class);
                        startActivity(startLink);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.main_viewPager);
        viewPager_prev = (ImageView) findViewById(R.id.iv_viewPager_prev);
        viewPager_next = (ImageView) findViewById(R.id.iv_viewPager_next);

        viewPager.setAdapter(new ViewPageAdapter(getLayoutInflater()));

        viewPager_prev.setOnClickListener(this);
        viewPager_next.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

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
