package com.demand.well_family.well_family.main.base.interator;

import com.demand.well_family.well_family.dto.App;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public interface MainInterator {

    void getFamilyData(User user);

    ArrayList<App> getAppData();

    void setUserInfo(User user);

    String getUserBirth(String birth);
}
