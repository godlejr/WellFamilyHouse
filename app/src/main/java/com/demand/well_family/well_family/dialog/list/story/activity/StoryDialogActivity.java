package com.demand.well_family.well_family.dialog.list.story.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.main.report.activity.ReportActivity;
import com.demand.well_family.well_family.dialog.list.story.adpater.StoryDialogAdapter;
import com.demand.well_family.well_family.dialog.list.story.flag.StoryDialogCodeFlag;
import com.demand.well_family.well_family.dialog.list.story.presenter.StoryDialogPresenter;
import com.demand.well_family.well_family.dialog.list.story.presenter.impl.StoryDialogPresenterImpl;
import com.demand.well_family.well_family.dialog.list.story.view.StoryDialogView;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.flag.ReportINTETNFlag;
import com.demand.well_family.well_family.flag.StoryINTENTFlag;
import com.demand.well_family.well_family.story.edit.activity.EditStoryActivity;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-03-21.
 */

public class StoryDialogActivity extends Activity implements StoryDialogView {
    private StoryDialogPresenter storyDialogPresenter;

    private RecyclerView rv_popup_comment;
    private StoryDialogAdapter storyDialogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_comment);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(android.view.WindowManager.LayoutParams.WRAP_CONTENT, android.view.WindowManager.LayoutParams.WRAP_CONTENT);

        int storyId = getIntent().getIntExtra("story_id", 0);
        int position = getIntent().getIntExtra("position", 0);
        String storyContent = getIntent().getStringExtra("content");

        Story story = new Story();
        story.setContent(storyContent);
        story.setPosition(position);
        story.setId(storyId);
        story.setUser_id(getIntent().getIntExtra("content_user_id", 0));

        Report report = new Report();
        report.setWriting_id(storyId);
        report.setWriting_content(storyContent);
        report.setWriting_position(position);
        report.setAuthor_name(getIntent().getStringExtra("author_name"));

        storyDialogPresenter = new StoryDialogPresenterImpl(this);
        storyDialogPresenter.onCreate(story, report);
    }

    @Override
    public void init() {
        rv_popup_comment = (RecyclerView) findViewById(R.id.rv_popup_comment);
        storyDialogPresenter.onLoadData();
    }

    @Override
    public void setStoryDialogAdapterInit(ArrayList<String> storyDialogList) {
        storyDialogAdapter = new StoryDialogAdapter(this, storyDialogList, R.layout.item_textview, storyDialogPresenter);
        rv_popup_comment.setAdapter(storyDialogAdapter);
        rv_popup_comment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void setStoryContentCopied() {
        storyDialogPresenter.setStoryContentCopied();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case StoryDialogCodeFlag.EDIT_REQUEST:
                switch (resultCode) {
                    case StoryDialogCodeFlag.RESULT_OK:
                        String editedStoryContent = data.getStringExtra("content");
                        storyDialogPresenter.onActivityResultForModifyResultOk(editedStoryContent);
                        break;
                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
        storyDialogPresenter.onBackPressed();
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void navigateToBackForResultOk(Story story) {
        Intent intent = getIntent();
        intent.putExtra("content", story.getContent());
        intent.putExtra("position", story.getPosition());
        setResult(StoryDialogCodeFlag.RESULT_OK, intent);

        finish();
    }

    @Override
    public void navigateToBackForResultCanceled(Story story) {
        Intent intent = getIntent();
        intent.putExtra("content", story.getContent());
        intent.putExtra("position", story.getPosition());
        setResult(StoryDialogCodeFlag.RESULT_CANCELED, intent);

        finish();
    }

    @Override
    public void setStoryDeleted() {
        storyDialogPresenter.setStoryDeleted();
    }

    @Override
    public void navigateToBackAfterDelete(Story story) {
        Intent intent = getIntent();
        intent.putExtra("position", story.getPosition());
        setResult(StoryDialogCodeFlag.DELETE, intent);
        finish();
    }

    @Override
    public void navigateModifyStoryActivity(Story story) {
        ArrayList<Photo> storyPhotoList = (ArrayList<Photo>) getIntent().getSerializableExtra("photoList");

        Intent edit_intent = new Intent(StoryDialogActivity.this, EditStoryActivity.class);
        edit_intent.putExtra("story_id", story.getId());
        edit_intent.putExtra("content", story.getContent());
        edit_intent.putExtra("position", story.getPosition());
        edit_intent.putExtra("photoList", storyPhotoList);

        startActivityForResult(edit_intent, StoryDialogCodeFlag.EDIT_REQUEST);
    }

    @Override
    public void navigateToReportActivity(Report report) {
        Intent report_intent = new Intent(StoryDialogActivity.this, ReportActivity.class);
        report_intent.putExtra("intent_flag", ReportINTETNFlag.STORY);
        report_intent.putExtra("author_name", report.getAuthor_name());
        report_intent.putExtra("writing_content", report.getWriting_content());
        report_intent.putExtra("writing_category_id", StoryINTENTFlag.STORY);
        report_intent.putExtra("writing_id", report.getWriting_id());

        startActivity(report_intent);
        finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(StoryDialogActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
