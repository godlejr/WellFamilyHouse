package com.demand.well_family.well_family.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.demand.well_family.well_family.R;

/**
 * Created by ㅇㅇ on 2017-03-06.
 */

public class SettingActivity extends Activity{
    private Switch sw_setting_family;
    private Switch sw_setting_popup;
    private Switch sw_setting_sound;
    private LinearLayout ll_setting_disable;
    private LinearLayout ll_setting_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setToolbar(getWindow().getDecorView());
        init();
    }

    private void  setToolbar(View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("설정");
    }

    private void init(){
        sw_setting_family = (Switch)findViewById(R.id.sw_setting_family);
        sw_setting_popup = (Switch)findViewById(R.id.sw_setting_popup);
        sw_setting_sound = (Switch)findViewById(R.id.sw_setting_sound);
        ll_setting_disable = (LinearLayout)findViewById(R.id.ll_setting_disable);
        ll_setting_pwd = (LinearLayout)findViewById(R.id.ll_setting_pwd);

        sw_setting_family.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            }
        });
        sw_setting_popup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        sw_setting_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        ll_setting_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ll_setting_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
