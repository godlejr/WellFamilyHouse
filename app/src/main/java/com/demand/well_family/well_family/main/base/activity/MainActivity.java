package com.demand.well_family.well_family.main.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.App;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.base.activity.FallDiagnosisActivity;
import com.demand.well_family.well_family.family.base.activity.FamilyActivity;
import com.demand.well_family.well_family.family.create.activity.CreateFamilyActivity;
import com.demand.well_family.well_family.family.manage.activity.ManageFamilyActivity;
import com.demand.well_family.well_family.flag.BadgeFlag;
import com.demand.well_family.well_family.main.base.adapter.app.AppAdapter;
import com.demand.well_family.well_family.main.base.adapter.family.FamilyAdapter;
import com.demand.well_family.well_family.main.base.adapter.viewpager.ViewPageAdapter;
import com.demand.well_family.well_family.main.base.presenter.MainPresenter;
import com.demand.well_family.well_family.main.base.presenter.impl.MainPresenterImpl;
import com.demand.well_family.well_family.main.base.view.MainView;
import com.demand.well_family.well_family.main.login.activity.LoginActivity;
import com.demand.well_family.well_family.market.MarketMainActivity;
import com.demand.well_family.well_family.memory_sound.SongMainActivity;
import com.demand.well_family.well_family.notification.NotificationActivity;
import com.demand.well_family.well_family.setting.base.activity.SettingActivity;
import com.demand.well_family.well_family.users.base.activity.UserActivity;

import java.util.ArrayList;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-01-18.
 */

public class MainActivity extends Activity implements View.OnClickListener, MainView, NavigationView.OnNavigationItemSelectedListener {
    private MainPresenter mainPresenter;

    private ViewPager viewPager;
    private ImageView viewPager_prev, viewPager_next;
    private RecyclerView rv_family, rv_apps;

    //family data
    private LinearLayout ll_family_container_family;
    private LayoutInflater layoutInflater;
    private ImageButton btn_family_add_exist;
    private LinearLayout ll_family_make;

    //decorview
    private View decorView;

    //toolbar
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView toolbar_market;
    private ImageView toolbar_menu;
    private ImageView toolbar_notification;
    private TextView toolbar_noti_count;

    //memnu
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private LinearLayout ll_menu_mypage;
    private View nv_header_view;
    private TextView tv_menu_name;
    private TextView tv_menu_birth;
    private ImageView iv_menu_avatar;

    //toggle
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter = new MainPresenterImpl(this);
        mainPresenter.onCreate();
    }

    @Override
    public void init() {
        finishList.add(this);

        viewPager = (ViewPager) findViewById(R.id.main_viewPager);
        viewPager_prev = (ImageView) findViewById(R.id.iv_viewPager_prev);
        viewPager_next = (ImageView) findViewById(R.id.iv_viewPager_next);

        viewPager.setAdapter(new ViewPageAdapter(getLayoutInflater(), this, mainPresenter));
        viewPager_prev.setOnClickListener(this);
        viewPager_next.setOnClickListener(this);

        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ll_family_container_family = (LinearLayout) findViewById(R.id.ll_family_container_family);
        btn_family_add_exist = (ImageButton) findViewById(R.id.btn_family_add_exist);

        //app recyclerview
        rv_apps = (RecyclerView) findViewById(R.id.rv_apps);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        int position;
        switch (v.getId()) {
            //view pager
            case R.id.iv_viewPager_prev:
                position = viewPager.getCurrentItem();
                viewPager.setCurrentItem(position - 1, true);
                break;

            case R.id.iv_viewPager_next:
                position = viewPager.getCurrentItem();
                viewPager.setCurrentItem(position + 1, true);
                break;

            //add family for exist
            case R.id.btn_family_add_exist:
                navigateToCreateFamilyActivity();
                break;

            //add family
            case R.id.ll_family_make:
                navigateToCreateFamilyActivity();
                break;

            //toobar
            case R.id.toolbar_market:
                drawerLayout.closeDrawers();
                navigateToMarketMainActivity();
                break;

            case R.id.toolbar_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.toolbar_notification:
                mainPresenter.onClickNotification();
                break;

            case R.id.ll_menu_mypage:
                drawerLayout.closeDrawers();
                mainPresenter.onClickUser();
                break;


        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawers();

        switch (item.getItemId()) {
            case R.id.menu_home:
                break;

            case R.id.menu_family:
                navigateToManageFamilyActivity();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                break;

            case R.id.menu_market:
                navigateToMarketMainActivity();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                break;

            case R.id.menu_setting:
                navigateToSettingActivity();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                break;

            case R.id.menu_help:
                showMessage("준비중입니다");
                break;

            case R.id.menu_logout:
                mainPresenter.onClickLogout();
                break;

            case R.id.menu_selffeet:
                showMessage("준비중입니다");
                break;
            case R.id.menu_bubblefeet:
                mainPresenter.onClickAppGames(getString(R.string.bubblefeet));
                break;

            case R.id.menu_happyfeet:
                mainPresenter.onClickAppGames(getString(R.string.happyfeet));
                break;

            case R.id.menu_memory_sound:
                mainPresenter.onClickSongMain();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        mainPresenter.onBackPressed();
        super.onBackPressed();
    }

    @Override
    public void setAppItem(ArrayList<App> appList) {
        rv_apps.setAdapter(new AppAdapter(appList, this, R.layout.item_apps, mainPresenter));
        rv_apps.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void setFamilyItem(ArrayList<Family> familyList) {
        btn_family_add_exist.setVisibility(View.VISIBLE);
        layoutInflater.inflate(R.layout.item_family_list, ll_family_container_family, true);
        rv_family = (RecyclerView) ll_family_container_family.findViewById(R.id.rv_family);
        rv_family.setAdapter(new FamilyAdapter(MainActivity.this, familyList, R.layout.item_users_familys, mainPresenter));
        rv_family.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

        btn_family_add_exist.setOnClickListener(this);
    }

    @Override
    public void setFamilyAddView() {
        btn_family_add_exist.setVisibility(View.GONE);
        layoutInflater.inflate(R.layout.item_family_make, ll_family_container_family, true);
        ll_family_make = (LinearLayout) findViewById(R.id.ll_family_make);
        ll_family_make.setOnClickListener(this);
    }


    @Override
    public void setMenu(View view) {
        drawerLayout = (DrawerLayout) view.findViewById(R.id.dl);
        navigationView = (NavigationView) view.findViewById(R.id.nv);
        navigationView.setItemIconTintList(null);
        nv_header_view = navigationView.getHeaderView(0);

        ll_menu_mypage = (LinearLayout) nv_header_view.findViewById(R.id.ll_menu_mypage);
        tv_menu_name = (TextView) nv_header_view.findViewById(R.id.tv_menu_name);
        tv_menu_birth = (TextView) nv_header_view.findViewById(R.id.tv_menu_birth);
        iv_menu_avatar = (ImageView) nv_header_view.findViewById(R.id.iv_menu_avatar);

        // menu
        Menu menu = navigationView.getMenu();
        MenuItem menu_all = menu.findItem(R.id.menu_all);
        SpannableString spannableString = new SpannableString(menu_all.getTitle());
        spannableString.setSpan(new TextAppearanceSpan(view.getContext(), R.style.NavigationDrawer), 0, spannableString.length(), 0);
        menu_all.setTitle(spannableString);

        MenuItem menu_apps = menu.findItem(R.id.menu_apps);
        spannableString = new SpannableString(menu_apps.getTitle());
        spannableString.setSpan(new TextAppearanceSpan(view.getContext(), R.style.NavigationDrawer), 0, spannableString.length(), 0);
        menu_apps.setTitle(spannableString);

        navigationView.setNavigationItemSelectedListener(this);
        ll_menu_mypage.setOnClickListener(this);
    }

    @Override
    public void setToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_market = (ImageView) toolbar.findViewById(R.id.toolbar_market);
        toolbar_menu = (ImageView) toolbar.findViewById(R.id.toolbar_menu);
        toolbar_notification = (ImageView) toolbar.findViewById(R.id.toolbar_notification);
        toolbar_noti_count = (TextView) toolbar.findViewById(R.id.toolbar_noti_count);

        toolbar_menu.setOnClickListener(this);
        toolbar_market.setOnClickListener(this);
        toolbar_notification.setOnClickListener(this);
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
    }

    @Override
    public void showMenuUserInfo(User user) {
        tv_menu_name.setText(user.getName());
        mainPresenter.setUserBirth(user.getBirth());
        Glide.with(this).load(getString(R.string.cloud_front_user_avatar) + user.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_menu_avatar);
    }

    @Override
    public void showMenuUserBirth(String birth) {
        tv_menu_birth.setText(birth);
    }

    @Override
    public void showBadgeCount(String message) {
        toolbar_noti_count.setText(message);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateBadgeCount() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count_package_name", "com.demand.well_family.well_family");
        intent.putExtra("badge_count_class_name", "com.demand.well_family.well_family.intro.IntroActivity");
        intent.putExtra("badge_count", BadgeFlag.BADGE_INITIALIZAION);
        getApplicationContext().sendBroadcast(intent);
    }

    @Override
    public void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToCreateFamilyActivity() {
        Intent intent = new Intent(this, CreateFamilyActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToMarketMainActivity() {
        Intent intent = new Intent(this, MarketMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToManageFamilyActivity() {
        Intent intent = new Intent(this, ManageFamilyActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToSettingActivity() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToSongMainActivity() {
        Intent intent = new Intent(this, SongMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToFamilyActivity(Family family) {
        Intent intent = new Intent(this, FamilyActivity.class);

        intent.putExtra("family_id", family.getId());
        intent.putExtra("family_name", family.getName());
        intent.putExtra("family_content", family.getContent());
        intent.putExtra("family_avatar", family.getAvatar());
        intent.putExtra("family_user_id", family.getUser_id());
        intent.putExtra("family_created_at", family.getCreated_at());

        startActivity(intent);
    }

    @Override
    public void navigateToAppGame(String appPackageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);
        if (intent == null) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_front) + appPackageName)));
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void navigateToNotificationActivity() {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToBackground() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void navigateToUserActivity(User user) {
        Intent intent = new Intent(this, UserActivity.class);


        intent.putExtra("story_user_id", user.getId());
        intent.putExtra("story_user_email", user.getEmail());
        intent.putExtra("story_user_birth", user.getBirth());
        intent.putExtra("story_user_phone", user.getPhone());
        intent.putExtra("story_user_name", user.getName());
        intent.putExtra("story_user_level", user.getLevel());
        intent.putExtra("story_user_avatar", user.getAvatar());

        startActivity(intent);
    }

    @Override
    public void navigateToFallDiagnosisActivity() {
        Intent intent = new Intent(MainActivity.this, FallDiagnosisActivity.class);
        startActivity(intent);
    }
}
