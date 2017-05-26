package com.demand.well_family.well_family.dialog.list.songstory.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.dialog.list.songstory.flag.SongStoryDialogFlag;
import com.demand.well_family.well_family.dialog.list.songstory.interactor.SongStoryDialogInteractor;
import com.demand.well_family.well_family.dialog.list.songstory.interactor.impl.SongStoryDialogInteractorImpl;
import com.demand.well_family.well_family.dialog.list.songstory.presenter.SongStoryDialogPresenter;
import com.demand.well_family.well_family.dialog.list.songstory.view.SongStoryDialogView;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.Song;
import com.demand.well_family.well_family.dto.SongStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-20.
 */

public class SongStoryDialogPresenterImpl implements SongStoryDialogPresenter {
    private SongStoryDialogView songStoryDialogView;
    private SongStoryDialogInteractor songStoryDialogInteractor;
    private PreferenceUtil preferenceUtil;

    public SongStoryDialogPresenterImpl(Context context) {
        this.songStoryDialogView = (SongStoryDialogView) context;
        this.songStoryDialogInteractor = new SongStoryDialogInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(SongStory songStory, Song song, Report report) {
        User user = preferenceUtil.getUserInfo();

        songStoryDialogInteractor.setUser(user);
        songStoryDialogInteractor.setSongStory(songStory);
        songStoryDialogInteractor.setSong(song);
        songStoryDialogInteractor.setReport(report);

        songStoryDialogView.init();
    }

    @Override
    public void onLoadData() {
        ArrayList<String> songStoryDialogList = songStoryDialogInteractor.getSongStoryDialogList();
        songStoryDialogView.setSongStoryDialogAdapterInit(songStoryDialogList);
    }

    @Override
    public void onActivityResultForEditResultOk(String songStoryContent, String songStoryLocation) {
        SongStory songStory = songStoryDialogInteractor.getSongStory();
        songStory.setContent(songStoryContent);
        songStory.setLocation(songStoryLocation);
        songStoryDialogView.navigateToBackAfterEdit(songStory);
    }

    @Override
    public void onClickBack() {
        SongStory songStory = songStoryDialogInteractor.getSongStory();
        songStoryDialogView.navigateToBack(songStory);
    }

    @Override
    public void onSuccessDeleteSongStory() {
        SongStory songStory = songStoryDialogInteractor.getSongStory();
        songStoryDialogView.navigateToBackAfterDelete(songStory);
        songStoryDialogView.showMessage("게시물이 삭제되었습니다.");
    }


    @Override
    public void onClickDeleteSongStory() {
        SongStory songStory = songStoryDialogInteractor.getSongStory();
        songStoryDialogInteractor.setSongStoryDeleted(songStory.getId());
    }

    @Override
    public void onClickCopySongStory() {
        SongStory songStory = songStoryDialogInteractor.getSongStory();
        songStoryDialogView.navigateToBack(songStory);
        songStoryDialogView.showMessage("클립보드에 복사되었습니다.");
    }

    @Override
    public void onClickSongStoryDialog(int dialogPosition) {
        SongStory songStory = songStoryDialogInteractor.getSongStory();
        Song song = songStoryDialogInteractor.getSong();
        Report report = songStoryDialogInteractor.getReport();
        User user = songStoryDialogInteractor.getUser();

        int userId = user.getId();
        int storyUserId = songStory.getUser_id();

        if (dialogPosition == SongStoryDialogFlag.COPY) {
            songStoryDialogView.setSongStoryCopied(songStory);
        }

        if (userId == storyUserId) {
            if (dialogPosition == SongStoryDialogFlag.EDIT) {
                songStoryDialogView.navigateToModifySongStoryActivity(songStory, song);
            }

            if (dialogPosition == SongStoryDialogFlag.DELETE) {
                songStoryDialogView.setSongStoryDeleted();
            }

            if (dialogPosition == SongStoryDialogFlag.CANCEL) {
                songStoryDialogView.navigateToBack(songStory);
            }
        } else {
            if (dialogPosition == SongStoryDialogFlag.REPORT) {
                songStoryDialogView.navigateToReportActivity(report);
            }

            if (dialogPosition == 2) {
                songStoryDialogView.navigateToBack(songStory);
            }
        }
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            songStoryDialogView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            songStoryDialogView.showMessage(apiErrorUtil.message());
        }
    }

}
