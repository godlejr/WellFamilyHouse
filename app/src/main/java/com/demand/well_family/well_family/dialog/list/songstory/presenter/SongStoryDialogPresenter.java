package com.demand.well_family.well_family.dialog.list.songstory.presenter;

import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.Song;
import com.demand.well_family.well_family.dto.SongStory;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-04-20.
 */

public interface SongStoryDialogPresenter {
    void onCreate(SongStory songStory, Song song, Report report);

    void onLoadData();

    void onActivityResultForEditResultOk(String songStoryContent, String songStoryLocation);

    void onSuccessDeleteSongStory();

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onClickCopySongStory();

    void onClickDeleteSongStory();

    void onClickSongStoryDialog(int dialogPosition);

    void onClickBack();

    void onSuccessGetSongStoryDialogList(int isFamily);

    void onSuccessGetFamilyCheckForClick(int isFamily, int position);
}
