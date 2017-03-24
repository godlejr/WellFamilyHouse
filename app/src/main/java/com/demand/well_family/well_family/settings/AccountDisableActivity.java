package com.demand.well_family.well_family.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.demand.well_family.well_family.R;

/**
 * Created by ㅇㅇ on 2017-03-06.
 */

public class AccountDisableActivity extends Activity {
    private Button btn_account_disable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_disable);

        init();
    }

    // toolbar_main & menu
    public void setToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("계정 비활성화");

    }

    private void init(){
        btn_account_disable = (Button)findViewById(R.id.btn_account_disable);
        btn_account_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AccountDisableSelectActivity.class);
                startActivity(intent);
                finish();
            }
        });

        setToolbar(getWindow().getDecorView());
    }

}
