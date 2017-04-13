package com.demand.well_family.well_family.main.base.view;

import android.view.View;

import com.demand.well_family.well_family.dto.App;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public interface MainView {
    void init();

    void setMenu(View view);

    void setToolbar(View view);

    void setAppItem( ArrayList<App> appList);

    void setFamilyItem(ArrayList<Family> familyList);

    void setFamilyAddView();

    View getDecorView();

    void showToolbarTitle(String message);

    void showMenuUserInfo(User user);

    void showMenuUserBirth(String birth);

    void showBadgeCount(String message);


    void showMessage(String message);

    void updateBadgeCount();

    void navigateToLoginActivity();

    void navigateToCreateFamilyActivity();

    void navigateToMarketMainActivity();

    void navigateToManageFamilyActivity();

    void navigateToSettingActivity();

    void navigateToSongMainActivity();

    void navigateToHappyfeet();

    void navigateToBubblefeet();

    void navigateToNotificationActivity();

    void navigateToBackground();

    void navigateToUserActivity(User user);
}
