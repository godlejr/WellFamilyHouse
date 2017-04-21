package com.demand.well_family.well_family.dialog.list.songstory.view;

import com.demand.well_family.well_family.dialog.list.songstory.adapter.SongStoryDialogAdapter;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.Song;
import com.demand.well_family.well_family.dto.SongStory;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-20.
 */

public interface SongStoryDialogView {
    void init();

    void setSongStoryDialogAdapterInit(ArrayList<String> songStoryDialogList);

    void setSongStoryDialogAdapter(SongStoryDialogAdapter songStoryDialogAdapter);

    void setSongStoryDialogAction(int dialogPosition);

    void setSongStoryDeleted();

    void setSongStoryCopied(SongStory songStory);

    void navigateToReportActivity(Report report);

    void navigateToBackAfterEdit(SongStory songStory);

    void navigateToBack(SongStory songStory);

    void navigateToBackAfterDelete(SongStory songStory);

    void navigateToModifySongStoryActivity(SongStory songStory, Song song);

    void showMessage (String message);
}
