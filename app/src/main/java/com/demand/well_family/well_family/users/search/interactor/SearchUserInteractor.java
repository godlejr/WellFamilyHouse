package com.demand.well_family.well_family.users.search.interactor;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.users.search.adapter.UserAdapter;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public interface SearchUserInteractor {
    void setUserSearched(User user, Family family, String searchKeyword);

    Family getFamily();

    void setFamily(Family family);

    void setUserJoinFamily(User user, UserInfoForFamilyJoin userFound);

    void setUserInvited(User user, UserInfoForFamilyJoin userFound, UserAdapter.UserViewHolder holder);
}
