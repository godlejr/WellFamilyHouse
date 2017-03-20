package com.demand.well_family.well_family;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.connection.MainServerConnection;
import com.demand.well_family.well_family.connection.UserServerConnection;
import com.demand.well_family.well_family.dto.Category;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtils;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-03-03.
 */

public class ReportActivity extends Activity {
    private String comment_user_name; // author
    private int comment_id;
    private int comment_category_id;
    private int report_category_id;
    private String comment_content;
    private int user_id;
    private String access_token;

    private TextView tv_report_content;
    private TextView tv_report_author_name;
    private RecyclerView rv_report;
    private ReportAdapter reportAdapter;
    private SharedPreferences loginInfo;

    private MainServerConnection mainServerConnection;
    private UserServerConnection userServerConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        comment_user_name = getIntent().getStringExtra("comment_user_name");
        comment_content = getIntent().getStringExtra("comment_content");
        comment_category_id = getIntent().getIntExtra("comment_category_id", 0);
        comment_id = getIntent().getIntExtra("comment_id", 0);

        //user_info
        loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
        user_id = loginInfo.getInt("user_id", 0);
        access_token = loginInfo.getString("access_token", null);

        init();
    }

    private void init() {
        tv_report_author_name = (TextView) findViewById(R.id.tv_report_author_name);
        tv_report_content = (TextView) findViewById(R.id.tv_report_content);
        tv_report_author_name.setText(comment_user_name);
        tv_report_content.setText(comment_content);

        rv_report = (RecyclerView) findViewById(R.id.rv_report);
        mainServerConnection = new HeaderInterceptor(access_token).getClientForMainServer().create(MainServerConnection.class);
        final Call<ArrayList<Category>> call_report_category_list = mainServerConnection.report_category_List();
        call_report_category_list.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                reportAdapter = new ReportAdapter(ReportActivity.this, response.body(), R.layout.item_report);
                rv_report.setAdapter(reportAdapter);
                rv_report.setLayoutManager(new LinearLayoutManager(ReportActivity.this, LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                Toast.makeText(ReportActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();

            }
        });

        setToolbar(getWindow().getDecorView());

    }
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
        toolbar_title.setText("신고하기");
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_report;

        public ReportViewHolder(View itemView) {
            super(itemView);

            tv_item_report = (TextView) itemView.findViewById(R.id.tv_item_report);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 신고
                    report_category_id = getAdapterPosition() + 1;

                    HashMap<String, String> map = new HashMap<>();
                    map.put("comment_category_id", String.valueOf(comment_category_id));
                    map.put("report_category_id", String.valueOf(report_category_id));
                    map.put("comment_id", String.valueOf(comment_id));

                    Log.e("신고", user_id + "," + comment_category_id + " ," + report_category_id + "," + comment_id);
                    userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
                    final Call<ResponseBody> call_report = userServerConnection.insert_comment_report(user_id, map);
                    call_report.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()) {
                                Toast.makeText(ReportActivity.this, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else{
                                Toast.makeText(ReportActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(ReportActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

        }
    }

    class ReportAdapter extends RecyclerView.Adapter<ReportViewHolder> {
        private ArrayList<Category> report_category_list;
        private int layout;
        private Context context;

        public ReportAdapter(Context context, ArrayList<Category> report_category_list, int layout) {
            this.report_category_list = report_category_list;
            this.layout = layout;
            this.context = context;

        }

        @Override
        public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ReportViewHolder reportViewHolder = new ReportViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return reportViewHolder;
        }

        @Override
        public void onBindViewHolder(ReportViewHolder holder, int position) {
            holder.tv_item_report.setText(report_category_list.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return report_category_list.size();
        }

    }

}
