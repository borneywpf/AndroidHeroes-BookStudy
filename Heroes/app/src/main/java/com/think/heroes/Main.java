package com.think.heroes;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.think.heroes.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class Main extends BaseActivity {
    private RecyclerView mView;

    @Override
    protected void initView() {
        super.initView();
        mView = getView(R.id.heroes_main_recyclerview);
        mView.setLayoutManager(new LinearLayoutManager(mContext));
        mView.setHasFixedSize(true);
        mView.setAdapter(new MainAdapter(getActivityClass()));
    }

    @Override
    protected int contentView() {
        return R.layout.activity_main;
    }

    private class MainAdapter extends RecyclerView.Adapter<MainHolder> {

        private List<ActivityInfo> mActivities;

        MainAdapter(List<ActivityInfo> activities) {
            mActivities = activities;
        }

        @Override
        public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainHolder(LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(MainHolder holder, int position) {
            final ActivityInfo info = mActivities.get(position);
            holder.textView.setText(info.name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, info.clazz);
                    intent.putExtra(MAIN_EXTRY_ACTIVITY_NAME, info.name);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mActivities != null ? mActivities.size() : 0;
        }
    }

    private class MainHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MainHolder(View itemView) {
            super(itemView);
            textView = getView(itemView, android.R.id.text1);
        }
    }

    private List<ActivityInfo> getActivityClass() {
        return new ArrayList<ActivityInfo>() {
            {
                add(new ActivityInfo() {
                    {
                        name = "第三章";
                        clazz = com.think.heroes.chapter3.MainActivity.class;
                    }
                });
                add(new ActivityInfo() {{
                    name = "第四章";
                    clazz = com.think.heroes.chapter4.MainActivity.class;
                }});
                add(new ActivityInfo() {{
                    name = "第五章";
                    clazz = com.think.heroes.chapter5.MainActivity.class;
                }});
            }
        };
    }

    private class ActivityInfo {
        String name;
        Class<? extends BaseActivity> clazz;
    }
}
