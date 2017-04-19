package com.demand.well_family.well_family.dialog.popup.photo.interactor.impl;

import com.demand.well_family.well_family.dialog.popup.photo.interactor.PhotoPopupInteractor;
import com.demand.well_family.well_family.dialog.popup.photo.presenter.PhotoPopupPresenter;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public class PhotoPopupInteractorImpl implements PhotoPopupInteractor{
    private PhotoPopupPresenter photoPopupPresenter;

    public PhotoPopupInteractorImpl(PhotoPopupPresenter photoPopupPresenter) {
        this.photoPopupPresenter = photoPopupPresenter;
    }
}
