package com.demand.well_family.well_family.story.detail.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.list.comment.activity.CommentDialogActivity;
import com.demand.well_family.well_family.dialog.list.comment.flag.CommentActFlag;
import com.demand.well_family.well_family.dialog.list.comment.flag.CommentDialogFlag;
import com.demand.well_family.well_family.dialog.list.story.StoryDialogActivity;
import com.demand.well_family.well_family.dialog.popup.photo.activity.PhotoPopupActivity;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.flag.CommentINTENTFlag;
import com.demand.well_family.well_family.flag.PhotoPopupINTENTFlag;
import com.demand.well_family.well_family.repository.StoryServerConnection;
import com.demand.well_family.well_family.story.detail.adapter.comment.CommentAdapter;
import com.demand.well_family.well_family.story.detail.adapter.photo.PhotoAdapter;
import com.demand.well_family.well_family.story.detail.flag.StoryDetailCodeFlag;
import com.demand.well_family.well_family.story.detail.presenter.StoryDetailPresenter;
import com.demand.well_family.well_family.story.detail.presenter.impl.StoryDetailPresenterImpl;
import com.demand.well_family.well_family.story.detail.view.StoryDetailView;
import com.demand.well_family.well_family.util.CalculateDateUtil;

import java.util.ArrayList;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by Dev-0 on 2017-01-23.
 */

public class StoryDetailActivity extends Activity implements StoryDetailView, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private StoryDetailPresenter storyDetailPresenter;


    private Boolean like_checked;
    private EditText et_story_detail_write_comment;
    private Button btn_story_detail_send_comment;

    //first like check
    private Boolean first_checked = false;


    private ImageView iv_item_story_avatar;
    private TextView tv_item_story_name;
    private TextView tv_item_story_date;
    private TextView tv_story_detail_content;
    private TextView tv_story_detail_like_count;
    private TextView tv_story_detail_comment_count;
    private CheckBox btn_item_main_story_detail_like;

    //recycler
    private RecyclerView rv_story_comments;
    private RecyclerView rv_content_detail;

    private NestedScrollView nestedScrollView;

    //adapter
    private PhotoAdapter photoAdapter;
    private CommentAdapter commentAdapter;

    //toolbar
    private DrawerLayout dl;
    private StoryServerConnection storyServerConnection;


    private ImageView iv_item_story_menu;

    //toolbar
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        StoryInfo storyInfo = new StoryInfo();
        storyInfo.setUser_id(getIntent().getIntExtra("content_user_id", 0));
        storyInfo.setContent(getIntent().getStringExtra("content"));
        storyInfo.setStory_id(getIntent().getIntExtra("story_id", 0));
        storyInfo.setName(getIntent().getStringExtra("story_user_name"));
        storyInfo.setAvatar(getIntent().getStringExtra("story_user_avatar"));
        storyInfo.setCreated_at(getIntent().getStringExtra("story_user_created_at"));
        storyInfo.setPosition(getIntent().getIntExtra("position", 0));

        boolean likeChecked = getIntent().getBooleanExtra("like_checked", false);

        storyDetailPresenter = new StoryDetailPresenterImpl(this);
        storyDetailPresenter.onCreate(storyInfo, likeChecked);

        finishList.add(this);


    }

    @Override
    public void init(StoryInfo storyInfo) {

        //user_info
        iv_item_story_avatar = (ImageView) findViewById(R.id.iv_item_story_avatar);
        tv_item_story_name = (TextView) findViewById(R.id.tv_item_story_name);
        tv_item_story_date = (TextView) findViewById(R.id.tv_item_story_date);
        tv_story_detail_content = (TextView) findViewById(R.id.tv_story_detail_content);

        //count
        tv_story_detail_like_count = (TextView) findViewById(R.id.tv_story_detail_like_count);
        tv_story_detail_comment_count = (TextView) findViewById(R.id.tv_story_detail_comment_count);

        //like
        btn_item_main_story_detail_like = (CheckBox) findViewById(R.id.btn_item_main_story_detail_like);

        //menu
        iv_item_story_menu = (ImageView) findViewById(R.id.iv_item_story_menu);

        //photo
        rv_content_detail = (RecyclerView) findViewById(R.id.rv_content_detail);
        rv_story_comments = (RecyclerView) findViewById(R.id.rv_story_comments);

        // comment
        et_story_detail_write_comment = (EditText) findViewById(R.id.et_story_detail_write_comment);
        btn_story_detail_send_comment = (Button) findViewById(R.id.btn_story_detail_send_comment);

        tv_item_story_name.setText(storyInfo.getName());
        tv_item_story_date.setText(CalculateDateUtil.calculateDate(storyInfo.getCreated_at()));
        tv_story_detail_content.setText(storyInfo.getContent());

        nestedScrollView = (NestedScrollView) findViewById(R.id.ns_story_detail);

        Glide.with(this).load(getString(R.string.cloud_front_user_avatar) + storyInfo.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_story_avatar);

        //loading
        storyDetailPresenter.onLoadStoryData();

        iv_item_story_menu.setOnClickListener(this);
        btn_story_detail_send_comment.setOnClickListener(this);
        btn_item_main_story_detail_like.setOnCheckedChangeListener(this);
    }


    @Override
    public void setToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);

        toolbar_back.setOnClickListener(this);
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void showCommentCount(String count) {
        tv_story_detail_comment_count.setText(count);
    }

    @Override
    public void showLikeCount(String count) {
        tv_story_detail_like_count.setText(count);
    }

    @Override
    public void showLikeChecked(boolean isChecked) {
        btn_item_main_story_detail_like.setChecked(isChecked);
    }

    @Override
    public void setCommentItemSpace() {
        int spacing = getResources().getDimensionPixelSize(R.dimen.comment_divider_height);
        rv_story_comments.addItemDecoration(new CommentAdapter.SpaceItemDecoration(spacing));
    }

    @Override
    public void setCommentItem(ArrayList<CommentInfo> commentInfoList) {
        commentAdapter = new CommentAdapter(StoryDetailActivity.this, commentInfoList, R.layout.item_comment, storyDetailPresenter);
        rv_story_comments.setLayoutManager(new LinearLayoutManager(StoryDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        rv_story_comments.setAdapter(commentAdapter);
    }

    @Override
    public void setCommentAdapterAdded(CommentInfo commentInfo) {
        commentAdapter.setCommentAdded(commentInfo);
    }

    @Override
    public void showLikeCheck(int value) {
        tv_story_detail_like_count.setText(String.valueOf(Integer.parseInt(tv_story_detail_like_count.getText().toString()) + value));
    }

    @Override
    public void setCommentAdapterSetContent(int position, String content) {
        commentAdapter.setCommentSetContent(position, content);
    }

    @Override
    public void setCommentAdapterDelete(int position) {
        commentAdapter.setCommentDelete(position);
    }

    @Override
    public void showCommentAdapterNotifyItemInserted() {
        commentAdapter.notifyItemInserted(commentAdapter.getItemCount() - 1);
    }

    @Override
    public void showCommentAdapterNotifyItemDelete(int position) {
        commentAdapter.notifyItemRemoved(position);
        commentAdapter.notifyItemRangeChanged(position, commentAdapter.getItemCount());
    }

    @Override
    public void showCommentAdapterNotifyItemChanged(int position) {
        commentAdapter.notifyItemChanged(position);
    }


    @Override
    public void showCommentEdit(String message) {
        et_story_detail_write_comment.setText(message);
    }

    @Override
    public void showCommentScrollDown() {
        nestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
    }

    @Override
    public void gonePhotoItem() {
        rv_content_detail.setVisibility(View.GONE);
    }

    @Override
    public void setPhotoItem(ArrayList<Photo> photoList) {
        photoAdapter = new PhotoAdapter(StoryDetailActivity.this, photoList, R.layout.item_detail_photo, storyDetailPresenter);
        rv_content_detail.setAdapter(photoAdapter);
        rv_content_detail.setLayoutManager(new LinearLayoutManager(StoryDetailActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBack(StoryInfo storyInfo, boolean likeChecked) {
        Intent intent = getIntent();
        intent.putExtra("content", storyInfo.getContent());
        intent.putExtra("position", storyInfo.getPosition());
        intent.putExtra("like_checked", likeChecked);
        setResult(StoryDetailCodeFlag.RESULT_OK, intent);
        finish();
    }

    @Override
    public void navigateToBackForPopupDelete(StoryInfo storyInfo) {
        Intent intent = getIntent();
        intent.putExtra("position", storyInfo.getPosition());
        setResult(StoryDetailCodeFlag.DELETE, intent);
        finish();
    }

    @Override
    public void navigateToStoryDialogActivity(StoryInfo storyInfo) {
        Intent intent = new Intent(this, StoryDialogActivity.class);

        intent.putExtra("story_id", storyInfo.getStory_id());
        intent.putExtra("content", storyInfo.getContent());
        intent.putExtra("photoList", photoAdapter.getPhotoList());
        intent.putExtra("content_user_id", storyInfo.getUser_id());
        intent.putExtra("position", storyInfo.getPosition());
        intent.putExtra("author_name", storyInfo.getName());

        startActivityForResult(intent, StoryDetailCodeFlag.POPUP_REQUEST);
    }

    @Override
    public void navigateToStoryCommentDialogActivityForOwner(CommentInfo commentInfo) {
        Intent intent = new Intent(StoryDetailActivity.this, CommentDialogActivity.class);
        intent.putExtra("comment_id", commentInfo.getComment_id());
        intent.putExtra("comment_content", commentInfo.getContent());
        intent.putExtra("position", commentInfo.getPosition());
        intent.putExtra("act_flag", CommentActFlag.WELL_FAMILY);
        startActivityForResult(intent, StoryDetailCodeFlag.COMMENT_EDIT_REQUEST);
    }

    @Override
    public void navigateToStoryDialogActivityForMember(CommentInfo commentInfo) {
        Intent intent = new Intent(StoryDetailActivity.this, CommentDialogActivity.class);
        intent.putExtra("comment_id", commentInfo.getComment_id());
        intent.putExtra("comment_user_name", commentInfo.getUser_name());
        intent.putExtra("comment_content", commentInfo.getContent());
        intent.putExtra("act_flag", CommentActFlag.PUBLIC);
        intent.putExtra("comment_category_id", CommentINTENTFlag.STORY);
        startActivity(intent);
    }

    @Override
    public void navigateToPhotoPopupActivity(int position) {
        Intent intent = new Intent(this, PhotoPopupActivity.class);
        intent.putExtra("intent_flag", PhotoPopupINTENTFlag.STORYDETAILACTIVITY);
        intent.putExtra("photo_position", position);
        intent.putExtra("photoList", photoAdapter.getPhotoList());
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        storyDetailPresenter.onCheckedChangeForLike(isChecked);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case StoryDetailCodeFlag.POPUP_REQUEST:

                switch (resultCode) {
                    case StoryDetailCodeFlag.RESULT_OK:
                        String content = data.getStringExtra("content");
                        storyDetailPresenter.onActivityResultForPopupResultOk(content);
                        break;

                    case StoryDetailCodeFlag.DELETE:
                        storyDetailPresenter.onActivityResultForPopupDelete();
                        break;
                }

                break;

            case StoryDetailCodeFlag.COMMENT_EDIT_REQUEST:

                switch (resultCode) {
                    case StoryDetailCodeFlag.RESULT_OK:
                        int flag = data.getIntExtra("flag", 0);
                        int position = data.getIntExtra("position", -1);

                        switch (flag) {
                            case CommentDialogFlag.MODIFY_OR_REPORT:
                                String content = data.getStringExtra("content");
                                storyDetailPresenter.onActivityResultForCommentEditResultOkModifyOrReport(position, content);
                                break;

                            case CommentDialogFlag.DELETE:
                                storyDetailPresenter.onActivityResultForCommentEditResultOkDELETE(position);
                                break;
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        storyDetailPresenter.onBackPressed();
        super.onBackPressed();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                storyDetailPresenter.onClickToolbarBack();
                break;
            case R.id.iv_item_story_menu:
                storyDetailPresenter.onClickStoryMenu();
                break;
            case R.id.btn_story_detail_send_comment:
                String content = et_story_detail_write_comment.getText().toString();
                storyDetailPresenter.onClickCommentAdd(content);
                break;
        }
    }
}
