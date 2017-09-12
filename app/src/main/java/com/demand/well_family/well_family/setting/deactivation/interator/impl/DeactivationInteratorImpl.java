package com.demand.well_family.well_family.setting.deactivation.interator.impl;

import com.demand.well_family.well_family.setting.deactivation.interator.DeactivationInterator;
import com.demand.well_family.well_family.setting.deactivation.presenter.DeactivationPresenter;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public class DeactivationInteratorImpl implements DeactivationInterator {
    DeactivationPresenter deactivationPresenter;

    public DeactivationInteratorImpl(DeactivationPresenter deactivationPresenter) {
        this.deactivationPresenter = deactivationPresenter;
    }

}
