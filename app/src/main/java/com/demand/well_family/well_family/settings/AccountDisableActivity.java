package com.demand.well_family.well_family.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.demand.well_family.well_family.R;

/**
 * Created by ㅇㅇ on 2017-03-06.
 */

public class AccountDisableActivity extends Activity {
    private Button btn_close;
    private Button btn_account_disable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_disabled);

        init();
    }

    private void init(){
        btn_close = (Button)findViewById(R.id.btn_close);
        btn_account_disable = (Button)findViewById(R.id.btn_account_disable);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_account_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}
