package com.think.heroes.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.think.heroes.Log;

/**
 * Created by borney on 11/17/16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "TAG_BaseActivity";
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
        Log.d(TAG, "initView()");
    }

    protected void initData() {
        Log.d(TAG, "initData()");
    }

    protected void initActionBar() {
        Log.d(TAG, "initActionBar()");
    }

    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    protected <T extends View> T getView(View parent, int id) {
        return (T) parent.findViewById(id);
    }
}
