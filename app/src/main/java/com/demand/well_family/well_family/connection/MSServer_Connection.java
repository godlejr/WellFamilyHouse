package com.demand.well_family.well_family.connection;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dev-0 on 2017-02-13.
 */

public interface MSServer_Connection {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/family/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
