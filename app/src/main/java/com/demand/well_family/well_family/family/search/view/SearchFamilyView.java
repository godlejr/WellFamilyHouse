package com.demand.well_family.well_family.family.search.view;

import android.view.View;

import com.demand.well_family.well_family.dto.FamilyInfoForFamilyJoin;
import com.demand.well_family.well_family.family.search.adapter.FamilyAdapter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-24.
 */

public interface SearchFamilyView {
    void init();

    void showMessage(String message);

    View getDecorView();

    void setToolbar(View decorView);

    void showToolbarTitle(String title);

    void setFamilyAdapter(ArrayList<FamilyInfoForFamilyJoin> familyList);



    void setFamilyStateButtonForMe(FamilyAdapter.FamilyViewHolder holder);

    void setFamilyStateButtonForJoin(FamilyAdapter.FamilyViewHolder holder, FamilyInfoForFamilyJoin familyFound);

    void setFamilyStateButtonForStay(FamilyAdapter.FamilyViewHolder holder);

}
