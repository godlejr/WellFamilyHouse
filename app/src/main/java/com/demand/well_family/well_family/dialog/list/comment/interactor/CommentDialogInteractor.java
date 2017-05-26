package com.demand.well_family.well_family.dialog.list.comment.interactor;

import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.Report;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public interface CommentDialogInteractor {
    ArrayList<String> getCommentDialog(int actFlag);

    Report getReport();

    void setReport(Report report);

    Comment getComment();

    void setComment(Comment comment);

    int getAuthorIsMe();

    void setAuthorIsMe(int authorIsMe);
}
