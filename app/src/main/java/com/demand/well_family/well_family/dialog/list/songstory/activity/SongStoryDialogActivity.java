package com.demand.well_family.well_family.dialog.list.songstory.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.ReportActivity;
import com.demand.well_family.well_family.dialog.list.songstory.adapter.SongStoryDialogAdapter;
import com.demand.well_family.well_family.dialog.list.songstory.flag.SongStoryDialogCodeFlag;
import com.demand.well_family.well_family.dialog.list.songstory.presenter.SongStoryDialogPresenter;
import com.demand.well_family.well_family.dialog.list.songstory.presenter.impl.SongStoryDialogPresenterImpl;
import com.demand.well_family.well_family.dialog.list.songstory.view.SongStoryDialogView;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.Song;
import com.demand.well_family.well_family.dto.SongStory;
import com.demand.well_family.well_family.dto.SongStoryEmotionData;
import com.demand.well_family.well_family.flag.ReportINTETNFlag;
import com.demand.well_family.well_family.flag.StoryINTENTFlag;
import com.demand.well_family.well_family.memory_sound.ModifySongStoryActivity;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-03-29.
 */

public class SongStoryDialogActivity extends Activity implements SongStoryDialogView {
    private SongStoryDialogPresenter songStoryDialogPresenter;

    private RecyclerView rv_popup_comment;
    private SongStoryDialogAdapter songStoryDialogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_comment);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(android.view.WindowManager.LayoutParams.WRAP_CONTENT, android.view.WindowManager.LayoutParams.WRAP_CONTENT);

        String songStoryContent = getIntent().getStringExtra("content");
        int position = getIntent().getIntExtra("position", 0);
        int songStoryId = getIntent().getIntExtra("story_id", 0);

        SongStory songStory = new SongStory();
        songStory.setContent(songStoryContent);
        songStory.setId(songStoryId);
        songStory.setPosition(position);
        songStory.setLocation(getIntent().getStringExtra("location"));
        songStory.setUser_id(getIntent().getIntExtra("story_user_id", 0));

        Song song = new Song();
        song.setId(getIntent().getIntExtra("song_id", 0));
        song.setAvatar(getIntent().getStringExtra("song_avatar"));
        song.setTitle(getIntent().getStringExtra("song_title"));
        song.setSinger(getIntent().getStringExtra("song_singer"));

        Report report = new Report();
        report.setWriting_id(songStoryId);
        report.setWriting_content(songStoryContent);
        report.setWriting_position(position);
        report.setAuthor_name(getIntent().getStringExtra("author_name"));

        songStoryDialogPresenter = new SongStoryDialogPresenterImpl(this);
        songStoryDialogPresenter.onCreate(songStory, song, report);
    }

    @Override
    public void init() {
        rv_popup_comment = (RecyclerView) findViewById(R.id.rv_popup_comment);
        songStoryDialogPresenter.setSongStoryDialogAdapterInit();
    }

    @Override
    public void setSongStoryDialogAdapterInit(ArrayList<String> songStoryDialogList) {
        songStoryDialogAdapter = new SongStoryDialogAdapter(this, songStoryDialogList, R.layout.item_textview, songStoryDialogPresenter);
        songStoryDialogPresenter.setSongStoryDialogAdapter(songStoryDialogAdapter);
    }

    @Override
    public void setSongStoryDialogAdapter(SongStoryDialogAdapter songStoryDialogAdapter) {
        rv_popup_comment.setAdapter(songStoryDialogAdapter);
        rv_popup_comment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SongStoryDialogCodeFlag.MODIFY_REQUEST:
                switch (resultCode) {
                    case SongStoryDialogCodeFlag.RESULT_OK:
                        String editedSongStoryContent = data.getStringExtra("content");
                        String editedSongStoryLocation = data.getStringExtra("location");

                        songStoryDialogPresenter.onActivityResultForEditResultOk(editedSongStoryContent, editedSongStoryLocation);
                        break;
                }
                break;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            songStoryDialogPresenter.onClickBack();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        songStoryDialogPresenter.onClickBack();
    }

    @Override
    public void navigateToBack(SongStory songStory) {
        Intent intent = getIntent();
        intent.putExtra("content", songStory.getContent());
        intent.putExtra("position", songStory.getPosition());
        setResult(SongStoryDialogCodeFlag.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void navigateToBackAfterEdit(SongStory songStory) {
        Intent intent = getIntent();
        intent.putExtra("content", songStory.getContent());
        intent.putExtra("location", songStory.getPosition());
        intent.putExtra("position", songStory.getPosition());
        setResult(SongStoryDialogCodeFlag.RESULT_OK, intent);
        finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(SongStoryDialogActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBackAfterDelete(SongStory songStory) {
        Intent intent = getIntent();
        intent.putExtra("position", songStory.getPosition());
        setResult(SongStoryDialogCodeFlag.DELETE, intent);
        finish();
    }

    @Override
    public void navigateToModifySongStoryActivity(SongStory songStory, Song song) {
        ArrayList<SongStoryEmotionData> emotionList = (ArrayList<SongStoryEmotionData>) getIntent().getSerializableExtra("emotionList");
        ArrayList<Photo> photoList = (ArrayList<Photo>) getIntent().getSerializableExtra("photoList");

        Intent intent = new Intent(SongStoryDialogActivity.this, ModifySongStoryActivity.class);
        intent.putExtra("story_id", songStory.getId());
        intent.putExtra("content", songStory.getContent());
        intent.putExtra("position", songStory.getPosition());
        intent.putExtra("location", songStory.getLocation());

        intent.putExtra("song_id", song.getId());
        intent.putExtra("song_avatar", song.getAvatar());
        intent.putExtra("song_title", song.getTitle());
        intent.putExtra("song_singer", song.getSinger());

        intent.putExtra("photoList", photoList);
        intent.putExtra("emotionList", emotionList);

        startActivityForResult(intent, SongStoryDialogCodeFlag.MODIFY_REQUEST);
    }

    @Override
    public void setSongStoryCopied(SongStory songStory) {
        ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(this.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", songStory.getContent());
        clipboardManager.setPrimaryClip(clipData);

        songStoryDialogPresenter.onClickCopySongStory();
    }

    @Override
    public void setSongStoryDeleted() {
        songStoryDialogPresenter.onClickDeleteSongStory();
    }

    @Override
    public void navigateToReportActivity(Report report) {
        Intent report_intent = new Intent(SongStoryDialogActivity.this, ReportActivity.class);
        report_intent.putExtra("intent_flag", ReportINTETNFlag.STORY);
        report_intent.putExtra("author_name", report.getAuthor_name());
        report_intent.putExtra("writing_content", report.getWriting_content());
        report_intent.putExtra("writing_category_id", StoryINTENTFlag.SONG_STORY);
        report_intent.putExtra("writing_id", report.getWriting_id());

        startActivity(report_intent);
        finish();
    }

    @Override
    public void setSongStoryDialogAction(int dialogPosition) {
        songStoryDialogPresenter.setSongStoryDialogAction(dialogPosition);
    }
}
