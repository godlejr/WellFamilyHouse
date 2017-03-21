package com.demand.well_family.well_family.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.demand.well_family.well_family.R;

/**
 * Created by ㅇㅇ on 2017-03-06.
 */

public class AccountDisableActivity extends Activity {
    private Button btn_account_disable;
    private RadioGroup rg_account_disable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_disable);

        init();
    }

    private void init(){
        rg_account_disable = (RadioGroup)findViewById(R.id.rg_account_disable);
        rg_account_disable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

            }
        });

        btn_account_disable = (Button)findViewById(R.id.btn_account_disable);
        btn_account_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}
