package com.demand.well_family.well_family.users;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demand.well_family.well_family.R;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-01-19.
 */

public class UsersActivity extends Activity {
    private GridView memberList;
    private ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("구성원");

        init();
    }

    private void init() {
        memberList = (GridView) findViewById(R.id.grid_members);

        sv = (ScrollView) findViewById(R.id.sv_members);
        sv.fullScroll(ScrollView.FOCUS_UP);


//        StoryDetailActivity
        ArrayList<Member> members = new ArrayList<>();
        members.add(new Member("name1"));
        members.add(new Member("name2"));
        members.add(new Member("name3"));
        members.add(new Member("name4"));
        members.add(new Member("name5"));
        members.add(new Member("name6"));
        members.add(new Member("name7"));

        memberList.setAdapter(new GridAdapter(this, R.layout.item_user, members));



     /* scrollview안에 gridview 넣을 시 설정*/
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        memberList.measure(0, expandSpec);
        memberList.getLayoutParams().height = memberList.getMeasuredHeight();

    }

    class GridAdapter extends BaseAdapter {
        private ArrayList<Member> members;
        private int layout;
        private LayoutInflater inflater;
        private Context context;

        public GridAdapter(Context context, int layout, ArrayList<Member> members) {
            this.context = context;
            this.layout = layout;
            this.members = members;

            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return members.size();
        }

        @Override
        public Object getItem(int position) {
            return members.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_user, parent, false);
            }

            TextView name = (TextView) convertView.findViewById(R.id.tv_user_item_name);
            ImageView avatar = (ImageView) convertView.findViewById(R.id.iv_user_item_avatar);

            name.setText(members.get(position).getName());

            return convertView;
        }
    }

    //    StoryDetailActivity
    class Member {
        String name, avatar;

        public Member(String name, String avatar) {
            this.name = name;
            this.avatar = avatar;
        }

        public Member(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
