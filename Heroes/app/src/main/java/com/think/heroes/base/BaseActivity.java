package com.think.heroes.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by borney on 11/17/16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected static String MAIN_EXTRY_ACTIVITY_NAME = "main_extry_activity_name";
    protected Context mContext;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(contentView());
        initActionBar();
        initData();
        initView();
    }

    protected abstract int contentView();

    protected void initView() {

    }

    protected void initData() {

    }

    protected void initActionBar() {
        Intent intent = getIntent();
        if (intent != null) {
            String activityName = intent.getStringExtra(MAIN_EXTRY_ACTIVITY_NAME);
            if (!TextUtils.isEmpty(activityName)) {
               setTitle(activityName);
            }
        }
    }

    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    protected <T extends View> T getView(View parent, int id) {
        return (T) parent.findViewById(id);
    }
}
