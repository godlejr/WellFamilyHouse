package com.demand.well_family.well_family.dialog.popup.songphoto.interactor.impl;

import com.demand.well_family.well_family.dialog.popup.songphoto.interactor.SongPhotoInteractor;
import com.demand.well_family.well_family.dialog.popup.songphoto.presenter.SongPhotoPresenter;
import com.demand.well_family.well_family.flag.LogFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public class SongPhotoInteractorImpl implements SongPhotoInteractor{
    private SongPhotoPresenter songPhotoPresenter;
    private static final Logger logger = LoggerFactory.getLogger(SongPhotoInteractorImpl.class);

    public SongPhotoInteractorImpl(SongPhotoPresenter songPhotoPresenter) {
        this.songPhotoPresenter = songPhotoPresenter;
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
