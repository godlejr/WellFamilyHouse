package com.demand.well_family.well_family.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;

/**
 * Created by ㅇㅇ on 2017-01-18.
 */

public class AgreementActivity extends AppCompatActivity {
    int f = 0;
    Button btn_join;
    CheckBox cb_agree_all, cb_agree1, cb_agree2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        init();
        setToolbar(getWindow().getDecorView());
    }

    private void init() {
        TextView agreement1 = (TextView) findViewById(R.id.agreement1);
        agreement1.setMovementMethod(new ScrollingMovementMethod());
        TextView agreement2 = (TextView) findViewById(R.id.agreement2);
        agreement2.setMovementMethod(new ScrollingMovementMethod());

        btn_join = (Button) findViewById(R.id.btn_agreement_join);
        cb_agree_all = (CheckBox) findViewById(R.id.cb_agreement_all);
        cb_agree1 = (CheckBox) findViewById(R.id.cb_agreement1);
        cb_agree2 = (CheckBox) findViewById(R.id.cb_agreement2);

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(cb_agree1.isChecked() && cb_agree2.isChecked())) {
                    Toast.makeText(AgreementActivity.this, "이용약관에 동의하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        cb_agree_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!cb_agree_all.isChecked()) {
                    if (f == 1) {
                        cb_agree1.setChecked(false);
                        cb_agree2.setChecked(false);
                    }
                } else {
                    cb_agree1.setChecked(true);
                    cb_agree2.setChecked(true);
                }

                f = 1;
            }
        });

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_agree1.isChecked() && cb_agree2.isChecked()) {
                    cb_agree_all.setChecked(true);
                } else if(!cb_agree2.isChecked() || !cb_agree1.isChecked()){
                    f=0;
                    cb_agree_all.setChecked(false);
                }
            }
        };

        cb_agree1.setOnCheckedChangeListener(listener);
        cb_agree2.setOnCheckedChangeListener(listener);

    }

    // toolbar_main & menu
    public void setToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBack();
            }
        });

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("약관 동의");

    }
    public void setBack() {
        finish();
    }

}
