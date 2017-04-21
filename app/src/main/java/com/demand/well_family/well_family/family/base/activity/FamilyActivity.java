package com.demand.well_family.well_family.family.base.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.CreateStoryActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.photo.activity.PhotoPopupActivity;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.manage.activity.ManageFamilyActivity;
import com.demand.well_family.well_family.flag.PhotoPopupINTENTFlag;
import com.demand.well_family.well_family.story.StoryDetailActivity;
import com.demand.well_family.well_family.family.base.adapter.content.ContentAdapter;
import com.demand.well_family.well_family.family.base.adapter.user.UserAdapter;
import com.demand.well_family.well_family.family.base.flag.FamilyCodeFlag;
import com.demand.well_family.well_family.family.base.presenter.FamilyPresenter;
import com.demand.well_family.well_family.family.base.presenter.impl.FamilyPresenterImpl;
import com.demand.well_family.well_family.family.base.view.FamilyView;
import com.demand.well_family.well_family.family.edit.activity.EditFamilyActivity;
import com.demand.well_family.well_family.main.base.activity.MainActivity;
import com.demand.well_family.well_family.main.login.activity.LoginActivity;
import com.demand.well_family.well_family.market.MarketMainActivity;
import com.demand.well_family.well_family.memory_sound.SongMainActivity;
import com.demand.well_family.well_family.family.photo.activity.PhotosActivity;
import com.demand.well_family.well_family.search.SearchUserActivity;
import com.demand.well_family.well_family.setting.base.activity.SettingActivity;
import com.demand.well_family.well_family.users.UserActivity;

import java.util.ArrayList;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by Dev-0 on 2017-01-19.
 */

public class FamilyActivity extends Activity implements FamilyView, NestedScrollView.OnScrollChangeListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private FamilyPresenter familyPresenter;

    private ImageView iv_family_edit;
    private ImageView iv_family_avatar;
    private TextView tv_family_content;
    private Button btn_family_photos;
    private ProgressDialog progressDialog;
    private ImageView iv_family_writer_avatar;
    private LinearLayout ll_user_add_exist;
    private TextView btn_family_write;
    private NestedScrollView nsv_content;

    private RecyclerView rv_family_users;
    private RecyclerView rv_family_content;

    //adapter
    private UserAdapter userAdapter;
    private ContentAdapter contentAdapter;

    //decorview
    private View decorView;

    //toolbar
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView toolbar_menu;
    private ImageView toolbar_back;


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
        setContentView(R.layout.activity_family_main);

        Family family = new Family();
        family.setId(getIntent().getIntExtra("family_id", 0));
        family.setName(getIntent().getStringExtra("family_name"));
        family.setContent(getIntent().getStringExtra("family_content"));
        family.setAvatar(getIntent().getStringExtra("family_avatar"));
        family.setUser_id(getIntent().getIntExtra("family_user_id", 0));
        family.setCreated_at(getIntent().getStringExtra("family_created_at"));

        boolean notificationFlag = getIntent().getBooleanExtra("notification_flag", false);

        familyPresenter = new FamilyPresenterImpl(this);
        familyPresenter.onCreate(family, notificationFlag);

    }

    @Override
    public void init(User user, Family family) {
        finishList.add(this);

        familyPresenter.setToolbarAndMenu(family.getName());

        ll_user_add_exist = (LinearLayout) findViewById(R.id.ll_user_add_exist);
        ll_user_add_exist.setOnClickListener(this);

        btn_family_photos = (Button) findViewById(R.id.btn_family_photos);
        btn_family_photos.setOnClickListener(this);

        btn_family_write = (TextView) findViewById(R.id.btn_family_write);
        btn_family_write.setOnClickListener(this);

        //family info
        iv_family_avatar = (ImageView) findViewById(R.id.iv_family_avatar);
        tv_family_content = (TextView) findViewById(R.id.tv_family_content);
        iv_family_avatar.setOnClickListener(this);

        //edit
        iv_family_edit = (ImageView) findViewById(R.id.iv_family_edit);
        familyPresenter.validateFamilyCreatorForEdit(user.getId(), family.getUser_id());


        Glide.with(this).load(getString(R.string.cloud_front_family_avatar) + family.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_family_avatar);
        tv_family_content.setText(family.getContent());

        // first user in userList == me
        iv_family_writer_avatar = (ImageView) findViewById(R.id.iv_family_writer_avatar);
        Glide.with(this).load(getString(R.string.cloud_front_user_avatar) + user.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_family_writer_avatar);

        //scroll
        nsv_content = (NestedScrollView) findViewById(R.id.nsv);
        nsv_content.setOnScrollChangeListener(this);

        //userList (family members)
        rv_family_users = (RecyclerView) findViewById(R.id.rv_family_users);

        //contentList (stories)
        rv_family_content = (RecyclerView) findViewById(R.id.rv_family_contents);
    }

    @Override
    public void setUserItem(ArrayList<User> userList) {
        userAdapter = new UserAdapter(this, userList, R.layout.item_users_familys, familyPresenter);
        rv_family_users.setAdapter(userAdapter);
        rv_family_users.setLayoutManager(new LinearLayoutManager(FamilyActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void setContentAdapterInit(ArrayList<StoryInfo> storyList) {
        this.contentAdapter = new ContentAdapter(FamilyActivity.this, storyList, R.layout.item_main_story, familyPresenter);
    }

    @Override
    public void setContentAdapter(ContentAdapter contentAdapter) {
        rv_family_content.setAdapter(contentAdapter);
        rv_family_content.setLayoutManager(new LinearLayoutManager(FamilyActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void setContentAdapterNoPhoto(ContentAdapter.ContentViewHolder holder) {
        contentAdapter.setContentNoPhoto(holder);
    }

    @Override
    public void setContentAdpaterOnePhoto(ContentAdapter.ContentViewHolder holder, ArrayList<Photo> photoList) {
        contentAdapter.setContentOnePhoto(holder, photoList);
    }

    @Override
    public void setContentAdapterTwoPhoto(ContentAdapter.ContentViewHolder holder, ArrayList<Photo> photoList) {
        contentAdapter.setContentTwoPhoto(holder, photoList);
    }

    @Override
    public void setContentAdapterMultiPhoto(ContentAdapter.ContentViewHolder holder, ArrayList<Photo> photoList, StoryInfo storyInfo) {
        contentAdapter.setContentMultiPhoto(holder, photoList, storyInfo);
    }

    @Override
    public void setContentAdapterLikeCount(ContentAdapter.ContentViewHolder holder, String count) {
        contentAdapter.setContentLikeCount(holder, count);
    }

    @Override
    public void setContentAdapterCommentCount(ContentAdapter.ContentViewHolder holder, String count) {
        contentAdapter.setContentCommentCount(holder, count);
    }

    @Override
    public void setContentAdapterLikeIsChecked(ContentAdapter.ContentViewHolder holder, int position) {
        contentAdapter.setContentLikeIsChecked(holder, position);
    }

    @Override
    public void setContentAdapterLikeIsNotChecked(ContentAdapter.ContentViewHolder holder, int position) {
        contentAdapter.setContentLikeIsNotChecked(holder, position);
    }

    @Override
    public void setContentAdapterLikeCheck(ContentAdapter.ContentViewHolder holder, int position) {
        contentAdapter.setContentLikeCheck(holder, position);
    }

    @Override
    public void setContentAdapterLikeUncheck(ContentAdapter.ContentViewHolder holder, int position) {
        contentAdapter.setContentLikeUncheck(holder, position);
    }

    @Override
    public void setContentAdapterContentAdd(StoryInfo storyInfo) {
        contentAdapter.setContentAdd(storyInfo);
    }

    @Override
    public void setContentAdapterContentChange(int position, String content, Boolean likeCheck) {
        contentAdapter.setContentChange(position, content, likeCheck);
    }

    @Override
    public void setContentAdapterContentDelete(int position) {
        contentAdapter.setContentDelete(position);
    }

    @Override
    public ContentAdapter getContentAdapter() {
        return this.contentAdapter;
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
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
        toolbar_menu = (ImageView) toolbar.findViewById(R.id.toolbar_menu);
        toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_menu.setOnClickListener(this);
        toolbar_back.setOnClickListener(this);
    }

    @Override
    public void showContentAdapterNotifyItemDelete(int position) {
        contentAdapter.notifyItemRemoved(position);
        contentAdapter.notifyItemRangeChanged(position, contentAdapter.getItemCount());
    }

    @Override
    public void showContentAdapterNotifyItemChanged(int position) {
        contentAdapter.notifyItemChanged(position);
    }

    @Override
    public void showContentAdapterNotifyItemInserted(int position) {
        contentAdapter.notifyItemInserted(position);
    }

    @Override
    public void showEditFamilyButton() {
        iv_family_edit.bringToFront();
        iv_family_edit.invalidate();
        iv_family_edit.setOnClickListener(this);
    }

    @Override
    public void goneEditFamilyButton() {
        iv_family_edit.setVisibility(View.GONE);
    }

    @Override
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_dialog);
    }

    @Override
    public void goneProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
    }

    @Override
    public void showMenuUserInfo(User user) {
        tv_menu_name.setText(user.getName());
        familyPresenter.setUserBirth(user.getBirth());
        Glide.with(this).load(getString(R.string.cloud_front_user_avatar) + user.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_menu_avatar);
    }

    @Override
    public void showMenuUserBirth(String birth) {
        tv_menu_birth.setText(birth);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void navigateToStoryDetailActivity(StoryInfo storyInfo) {
        Intent intent = new Intent(this, StoryDetailActivity.class);

        intent.putExtra("content_user_id", storyInfo.getUser_id());
        intent.putExtra("content", storyInfo.getContent());
        intent.putExtra("story_id", storyInfo.getStory_id());
        intent.putExtra("story_user_name", storyInfo.getName());
        intent.putExtra("story_user_avatar", storyInfo.getAvatar());
        intent.putExtra("story_user_created_at", storyInfo.getCreated_at());
        intent.putExtra("like_checked", storyInfo.getChecked());
        intent.putExtra("position", storyInfo.getPosition());

        startActivityForResult(intent, FamilyCodeFlag.DETAIL_REQUEST);
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
    public void navigateToSearchUserActivity(Family family) {
        Intent intent = new Intent(FamilyActivity.this, SearchUserActivity.class);

        intent.putExtra("family_id", family.getId());
        intent.putExtra("family_name", family.getName());
        intent.putExtra("family_content", family.getContent());
        intent.putExtra("family_avatar", family.getAvatar());
        intent.putExtra("family_user_id", family.getUser_id());
        intent.putExtra("family_created_at", family.getCreated_at());

        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToPhotosActivity(Family family) {
        Intent intent = new Intent(FamilyActivity.this, PhotosActivity.class);

        intent.putExtra("family_id", family.getId());
        intent.putExtra("family_name", family.getName());
        intent.putExtra("family_content", family.getContent());
        intent.putExtra("family_avatar", family.getAvatar());
        intent.putExtra("family_user_id", family.getUser_id());
        intent.putExtra("family_created_at", family.getCreated_at());

        startActivity(intent);
    }

    @Override
    public void navigateToCreateStoryActivity(Family family) {
        Intent intent = new Intent(FamilyActivity.this, CreateStoryActivity.class);

        intent.putExtra("family_id", family.getId());
        intent.putExtra("family_name", family.getName());
        intent.putExtra("family_content", family.getContent());
        intent.putExtra("family_avatar", family.getAvatar());
        intent.putExtra("family_user_id", family.getUser_id());
        intent.putExtra("family_created_at", family.getCreated_at());

        startActivityForResult(intent, FamilyCodeFlag.WRITE_REQUEST);
    }

    @Override
    public void navigateToEditFamilyActivity(Family family) {
        Intent intent = new Intent(FamilyActivity.this, EditFamilyActivity.class);
        intent.putExtra("family_id", family.getId());
        intent.putExtra("family_name", family.getName());
        intent.putExtra("family_content", family.getContent());
        intent.putExtra("family_avatar", family.getAvatar());
        intent.putExtra("family_user_id", family.getUser_id());
        intent.putExtra("family_created_at", family.getCreated_at());
        startActivityForResult(intent, FamilyCodeFlag.EDIT_REQUEST);
    }

    @Override
    public void navigateToPhotoPopupActivity(Family family) {
        Intent intent = new Intent(this, PhotoPopupActivity.class);

        String familyAvatar = family.getAvatar();
        ArrayList<String> photoList = new ArrayList<>();
        photoList.add(familyAvatar);

        intent.putExtra("photoList", photoList);
        intent.putExtra("intent_flag", PhotoPopupINTENTFlag.FAMILYACTIVITY);

        startActivity(intent);
    }

    @Override
    public void navigateToMainActivity() {
        Intent intent = new Intent(FamilyActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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
    public void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToSongMainActivity() {
        Intent intent = new Intent(this, SongMainActivity.class);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawers();

        switch (item.getItemId()) {
            case R.id.menu_home:
                navigateToMainActivity();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
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
                familyPresenter.onClickLogout();
                break;

            case R.id.menu_selffeet:
                showMessage("준비중입니다");
                break;
            case R.id.menu_bubblefeet:
                familyPresenter.onClickAppGames(getString(R.string.bubblefeet));
                break;

            case R.id.menu_happyfeet:
                familyPresenter.onClickAppGames(getString(R.string.happyfeet));
                break;

            case R.id.menu_memory_sound:
                familyPresenter.onClickSongMain();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Family family = new Family();

        family.setId(getIntent().getIntExtra("family_id", 0));
        family.setName(getIntent().getStringExtra("family_name"));
        family.setContent(getIntent().getStringExtra("family_content"));
        family.setAvatar(getIntent().getStringExtra("family_avatar"));
        family.setUser_id(getIntent().getIntExtra("family_user_id", 0));
        family.setCreated_at(getIntent().getStringExtra("family_created_at"));


        switch (v.getId()) {
            //toobar
            case R.id.toolbar_back:
                familyPresenter.onClickBack();
                break;
            case R.id.toolbar_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.ll_menu_mypage:
                drawerLayout.closeDrawers();
                familyPresenter.onClickUser();
                break;

            case R.id.ll_user_add_exist:
                navigateToSearchUserActivity(family);
                break;

            case R.id.btn_family_photos:
                navigateToPhotosActivity(family);
                break;

            case R.id.btn_family_write:
                navigateToCreateStoryActivity(family);
                break;

            case R.id.iv_family_avatar:
                navigateToPhotoPopupActivity(family);
                break;

            case R.id.iv_family_edit:
                navigateToEditFamilyActivity(family);
                break;
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        View view = v.getChildAt(v.getChildCount() - 1);
        int difference = (view.getBottom() - (v.getHeight() + v.getScrollY()));
        familyPresenter.onScrollChange(difference);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FamilyCodeFlag.WRITE_REQUEST:

                switch (resultCode) {
                    case FamilyCodeFlag.RESULT_OK:
                        StoryInfo storyInfo = (StoryInfo) data.getSerializableExtra("storyInfo");
                        familyPresenter.onActivityResultForWriteResultOk(storyInfo);
                        break;
                }

                break;

            case FamilyCodeFlag.DETAIL_REQUEST:
                int position = data.getIntExtra("position", 0);

                switch (resultCode) {
                    case FamilyCodeFlag.RESULT_OK:
                        String content = data.getStringExtra("content");
                        Boolean likeCheck = data.getBooleanExtra("like_checked", false);

                        familyPresenter.onActivityResultForStoryDetailResultOk(position, content, likeCheck);
                        break;
                    case FamilyCodeFlag.DELETE:

                        familyPresenter.onActivityResultForStoryDetailDelete(position);
                        break;
                }

                break;

            case FamilyCodeFlag.EDIT_REQUEST:

                switch (resultCode) {
                    case FamilyCodeFlag.RESULT_OK:
                        String familyName = data.getStringExtra("name");
                        String familyContent = data.getStringExtra("content");
                        String familyAvatar = data.getStringExtra("avatar");
                        familyPresenter.onActivityResultForEditFamilyResultOk(familyName, familyContent, familyAvatar);
                        break;
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        familyPresenter.onClickBack();
        super.onBackPressed();
    }
}
