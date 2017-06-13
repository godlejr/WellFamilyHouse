package com.demand.well_family.well_family.dialog.list.songstory.interactor.impl;

import com.demand.well_family.well_family.dialog.list.songstory.interactor.SongStoryDialogInteractor;
import com.demand.well_family.well_family.dialog.list.songstory.presenter.SongStoryDialogPresenter;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.Song;
import com.demand.well_family.well_family.dto.SongStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.SongStoryServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-04-20.
 */

public class SongStoryDialogInteractorImpl implements SongStoryDialogInteractor {
    private SongStoryDialogPresenter songStoryDialogPresenter;

    private SongStoryServerConnection songStoryServerConnection;
    private User user;
    private SongStory songStory;
    private Song song;
    private Report report;
    private boolean storyUserIsMe;

    private static final Logger logger = LoggerFactory.getLogger(SongStoryDialogInteractorImpl.class);

    public SongStoryDialogInteractorImpl(SongStoryDialogPresenter songStoryDialogPresenter) {
        this.songStoryDialogPresenter = songStoryDialogPresenter;
    }

    @Override
    public ArrayList<String> getSongStoryDialogList() {
        int userId = user.getId();
        int songStoryUserId = songStory.getUser_id();

        ArrayList<String> songStoryDialogList = new ArrayList<>();
        songStoryDialogList.add("본문 복사");
        if (userId == songStoryUserId) {
            songStoryDialogList.add("수정");
            songStoryDialogList.add("삭제");
        } else {
            songStoryDialogList.add("신고 하기");
        }
        songStoryDialogList.add("취소");

        return songStoryDialogList;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setSongStory(SongStory songStory) {
        this.songStory = songStory;
    }

    @Override
    public SongStory getSongStory() {
        return songStory;
    }

    @Override
    public void setSong(Song song) {
        this.song = song;
    }

    @Override
    public Song getSong() {
        return song;
    }

    @Override
    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    public Report getReport() {
        return report;
    }

    @Override
    public void setStoryUserIsMe(boolean storyUserIsMe) {
        this.storyUserIsMe = storyUserIsMe;
    }

    @Override
    public boolean getStoryUserIsMe() {
        return storyUserIsMe;
    }

    @Override
    public void setSongStoryDeleted(int songStoryId) {
        String accessToken = user.getAccess_token();

        songStoryServerConnection = new NetworkInterceptor(accessToken).getClientForSongStoryServer().create(SongStoryServerConnection.class);
        Call<Void> call_delete_story = songStoryServerConnection.delete_story(songStoryId);
        call_delete_story.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    songStoryDialogPresenter.onSuccessDeleteSongStory();
                } else {
                    songStoryDialogPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log(t);
                songStoryDialogPresenter.onNetworkError(null);
            }
        });

    }

    private static void log(Throwable throwable) {
        StackTraceElement[] ste = throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if (LogFlag.printFlag) {
            if (logger.isInfoEnabled()) {
                logger.error("Exception: " + throwable.getMessage());
                logger.error(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }
}
