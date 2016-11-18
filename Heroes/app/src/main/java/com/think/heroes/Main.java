package com.think.heroes;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.think.heroes.base.BaseActivity;
import com.think.heroes.widget.ItemDecorationVerticalDivider;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends BaseActivity {
    private static final String TAG = "TAG_Main";
    private RecyclerView mView;

    @Override
    protected void initView() {
        super.initView();
        Intent intent = getIntent();
        String path = intent.getStringExtra("com.think.android.samples.Path");

        if (path == null) {
            path = "";
        } else {
            setTitle(path);
        }
        mView = (RecyclerView) findViewById(R.id.heroes_main_recyclerview);
        mView.setLayoutManager(new LinearLayoutManager(this));
        mView.addItemDecoration(new ItemDecorationVerticalDivider(this));
        List<Map<String, Object>> datas = getData(path);
        Log.d(TAG, "datas = " + datas);
        mView.setAdapter(new ViewAdapter(this, datas));
    }

    @Override
    protected int contentView() {
        return R.layout.activity_main;
    }

    private List<Map<String, Object>> getData(String prefix) {
        Log.d(TAG, "prefix = " + prefix);
        List<Map<String, Object>> myData = new ArrayList<>();
        Intent mainIntent = new Intent();
        mainIntent.setAction(Intent.ACTION_MAIN);
        mainIntent.addCategory(AppContext.SAMPLES_CATEGORY);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(mainIntent, 0);
        Log.d(TAG, "infos = " + infos);
        if (null == infos) {
            return myData;
        }

        String[] prefixPath;
        String prefixWithSlash = prefix;

        if (prefix.equals("")) {
            prefixPath = null;
        } else {
            prefixPath = prefix.split("/");
            prefixWithSlash = prefix + "/";
        }

        Log.d(TAG, "prefixPath = " + prefixPath + "\nprefixWithSlash = " + prefixWithSlash);
        if (prefixPath != null) {
            for (int i = 0; i < prefixPath.length; i++) {
                Log.d(TAG, "prifixPath[" + i + "] = " + prefixPath[i]);
            }
        }

        Map<String, Boolean> entries = new HashMap<>();

        for (int i = 0, len = infos.size(); i < len; i++) {
            ResolveInfo info = infos.get(i);
            CharSequence labelSeq = info.loadLabel(pm);
            String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;
            Log.d(TAG, "label = " + label);

            if (prefixWithSlash.length() == 0 || label.startsWith(prefixWithSlash)) {
                String[] labelSplit = label.split("/");
                String nextLabel = prefixPath == null ? labelSplit[0] : labelSplit[prefixPath.length];
                Log.d(TAG, "nextLabel = " + nextLabel + " labelSplit.length = " + labelSplit.length);

                if ((prefixPath != null ? prefixPath.length : 0) == labelSplit.length - 1) {
                    Log.d(TAG, "add item nextLabel:" + nextLabel);
                    addItem(myData, nextLabel, activityIntent(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));
                } else {
                    Log.d(TAG, "add item nextLabel:" + nextLabel + " entries.get = " + entries.get(nextLabel));
                    if (entries.get(nextLabel) == null) {
                        addItem(myData, nextLabel, browseIntent(prefix.equals("") ? nextLabel : prefix + "/" + nextLabel));
                        entries.put(nextLabel, true);
                    }
                }
            }
        }

        Collections.sort(myData, sDisplayNameComparator);

        return myData;
    }

    private void addItem(List<Map<String, Object>> data, String name, Intent intent) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

    private Intent browseIntent(String path) {
        Log.d(TAG, "browseIntent path = " + path);
        Intent result = new Intent();
        result.setClass(this, Main.class);
        result.putExtra("com.think.android.samples.Path", path);
        return result;
    }

    private Intent activityIntent(String pkg, String componentName) {
        Log.d(TAG, "activityIntent pkg = " + pkg + " componentName = " + componentName);
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }

    private Comparator<Map<String, Object>> sDisplayNameComparator = new Comparator<Map<String, Object>>() {
        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
            return collator.compare(lhs.get("title"), rhs.get("title"));
        }
    };

    private class ViewAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final List<Map<String, Object>> datas;
        private int itemBackground = 0;

        public ViewAdapter(Context context, List<Map<String, Object>> datas) {
            this.datas = datas;
            int[] attrs = new int[]{android.R.attr.selectableItemBackground};
            TypedArray array = context.obtainStyledAttributes(attrs);
            try {
                itemBackground = array.getResourceId(0, 0);
            } finally {
                if (array != null) {
                    array.recycle();
                }
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Map<String, Object> map = datas.get(position);
            Log.d(TAG, "map = " + map);
            holder.textView.setText((CharSequence) map.get("title"));
            holder.itemView.setBackgroundResource(itemBackground);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent((Intent) map.get("intent"));
                    intent.addCategory(AppContext.SAMPLES_CATEGORY);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas != null ? datas.size() : 0;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}

/**
 * 首次进入界面是Main
 11-18 11:22:43.279 20440 20440 D Heroes_TAG_BaseActivity: initActionBar()
 11-18 11:22:43.279 20440 20440 D Heroes_TAG_BaseActivity: initData()
 11-18 11:22:43.279 20440 20440 D Heroes_TAG_BaseActivity: initView()
 11-18 11:22:43.281 20440 20440 D Heroes_TAG_Main: prefix =
 11-18 11:22:43.285 20440 20440 D Heroes_TAG_Main: infos = [ResolveInfo{d3464cf com.think.heroes/.chapter3.Chapter_3_6_1 m=0x108000}, ResolveInfo{d202e5c com.think.heroes/.chapter3.Chapter_3_6_2 m=0x108000}, ResolveInfo{bfbf565 com.think.heroes/.chapter4.MainActivity m=0x108000}, ResolveInfo{2a723a com.think.heroes/.chapter5.MainActivity m=0x108000}]
 11-18 11:22:43.285 20440 20440 D Heroes_TAG_Main: prefixPath = null
 11-18 11:22:43.285 20440 20440 D Heroes_TAG_Main: prefixWithSlash =
 11-18 11:22:43.285 20440 20440 D Heroes_TAG_Main: label = 第三章/3.6/3.6.1
 11-18 11:22:43.285 20440 20440 D Heroes_TAG_Main: nextLabel = 第三章 labelSplit.length = 3
 11-18 11:22:43.285 20440 20440 D Heroes_TAG_Main: add item nextLabel:第三章 entries.get = null
 11-18 11:22:43.286 20440 20440 D Heroes_TAG_Main: browseIntent path = 第三章
 11-18 11:22:43.286 20440 20440 D Heroes_TAG_Main: label = 第三章/3.6/3.6.2
 11-18 11:22:43.286 20440 20440 D Heroes_TAG_Main: nextLabel = 第三章 labelSplit.length = 3
 11-18 11:22:43.286 20440 20440 D Heroes_TAG_Main: add item nextLabel:第三章 entries.get = true
 11-18 11:22:43.287 20440 20440 D Heroes_TAG_Main: label = 第四章/第一节
 11-18 11:22:43.287 20440 20440 D Heroes_TAG_Main: nextLabel = 第四章 labelSplit.length = 2
 11-18 11:22:43.287 20440 20440 D Heroes_TAG_Main: add item nextLabel:第四章 entries.get = null
 11-18 11:22:43.287 20440 20440 D Heroes_TAG_Main: browseIntent path = 第四章
 11-18 11:22:43.287 20440 20440 D Heroes_TAG_Main: label = 第五章/第一节
 11-18 11:22:43.287 20440 20440 D Heroes_TAG_Main: nextLabel = 第五章 labelSplit.length = 2
 11-18 11:22:43.287 20440 20440 D Heroes_TAG_Main: add item nextLabel:第五章 entries.get = null
 11-18 11:22:43.287 20440 20440 D Heroes_TAG_Main: browseIntent path = 第五章
 11-18 11:22:43.288 20440 20440 D Heroes_TAG_Main: datas = [{intent=Intent { cmp=com.think.heroes/.Main (has extras) }, title=第三章}, {intent=Intent { cmp=com.think.heroes/.Main (has extras) }, title=第四章}, {intent=Intent { cmp=com.think.heroes/.Main (has extras) }, title=第五章}]
 11-18 11:22:43.397 20440 20440 D Heroes_TAG_Main: map = {intent=Intent { cmp=com.think.heroes/.Main (has extras) }, title=第三章}
 11-18 11:22:43.417 20440 20440 D Heroes_TAG_Main: map = {intent=Intent { cmp=com.think.heroes/.Main (has extras) }, title=第四章}
 11-18 11:22:43.419 20440 20440 D Heroes_TAG_Main: map = {intent=Intent { cmp=com.think.heroes/.Main (has extras) }, title=第五章}


 11-18 11:23:04.720 20440 20440 D Heroes_TAG_BaseActivity: initActionBar()
 11-18 11:23:04.720 20440 20440 D Heroes_TAG_BaseActivity: initData()
 11-18 11:23:04.720 20440 20440 D Heroes_TAG_BaseActivity: initView()
 11-18 11:23:04.721 20440 20440 D Heroes_TAG_Main: prefix = 第三章
 11-18 11:23:04.725 20440 20440 D Heroes_TAG_Main: infos = [ResolveInfo{ee7959a com.think.heroes/.chapter3.Chapter_3_6_1 m=0x108000}, ResolveInfo{74bcecb com.think.heroes/.chapter3.Chapter_3_6_2 m=0x108000}, ResolveInfo{4b16a8 com.think.heroes/.chapter4.MainActivity m=0x108000}, ResolveInfo{ed005c1 com.think.heroes/.chapter5.MainActivity m=0x108000}]
 11-18 11:23:04.725 20440 20440 D Heroes_TAG_Main: prefixPath = [Ljava.lang.String;@4997166
 11-18 11:23:04.725 20440 20440 D Heroes_TAG_Main: prefixWithSlash = 第三章/
 11-18 11:23:04.725 20440 20440 D Heroes_TAG_Main: prifixPath[0] = 第三章
 11-18 11:23:04.725 20440 20440 D Heroes_TAG_Main: label = 第三章/3.6/3.6.1
 11-18 11:23:04.725 20440 20440 D Heroes_TAG_Main: nextLabel = 3.6 labelSplit.length = 3
 11-18 11:23:04.725 20440 20440 D Heroes_TAG_Main: add item nextLabel:3.6 entries.get = null
 11-18 11:23:04.725 20440 20440 D Heroes_TAG_Main: browseIntent path = 第三章/3.6
 11-18 11:23:04.725 20440 20440 D Heroes_TAG_Main: label = 第三章/3.6/3.6.2
 11-18 11:23:04.725 20440 20440 D Heroes_TAG_Main: nextLabel = 3.6 labelSplit.length = 3
 11-18 11:23:04.725 20440 20440 D Heroes_TAG_Main: add item nextLabel:3.6 entries.get = true
 11-18 11:23:04.726 20440 20440 D Heroes_TAG_Main: label = 第四章/第一节
 11-18 11:23:04.726 20440 20440 D Heroes_TAG_Main: label = 第五章/第一节
 11-18 11:23:04.726 20440 20440 D Heroes_TAG_Main: datas = [{intent=Intent { cmp=com.think.heroes/.Main (has extras) }, title=3.6}]
 11-18 11:23:04.760 20440 20440 D Heroes_TAG_Main: map = {intent=Intent { cmp=com.think.heroes/.Main (has extras) }, title=3.6}



 11-18 11:23:08.836 20440 20440 D Heroes_TAG_BaseActivity: initActionBar()
 11-18 11:23:08.836 20440 20440 D Heroes_TAG_BaseActivity: initData()
 11-18 11:23:08.836 20440 20440 D Heroes_TAG_BaseActivity: initView()
 11-18 11:23:08.837 20440 20440 D Heroes_TAG_Main: prefix = 第三章/3.6
 11-18 11:23:08.840 20440 20440 D Heroes_TAG_Main: infos = [ResolveInfo{82dabf0 com.think.heroes/.chapter3.Chapter_3_6_1 m=0x108000}, ResolveInfo{1758569 com.think.heroes/.chapter3.Chapter_3_6_2 m=0x108000}, ResolveInfo{5e5fbee com.think.heroes/.chapter4.MainActivity m=0x108000}, ResolveInfo{dc2f88f com.think.heroes/.chapter5.MainActivity m=0x108000}]
 11-18 11:23:08.840 20440 20440 D Heroes_TAG_Main: prefixPath = [Ljava.lang.String;@159971c
 11-18 11:23:08.840 20440 20440 D Heroes_TAG_Main: prefixWithSlash = 第三章/3.6/
 11-18 11:23:08.840 20440 20440 D Heroes_TAG_Main: prifixPath[0] = 第三章
 11-18 11:23:08.840 20440 20440 D Heroes_TAG_Main: prifixPath[1] = 3.6
 11-18 11:23:08.841 20440 20440 D Heroes_TAG_Main: label = 第三章/3.6/3.6.1
 11-18 11:23:08.841 20440 20440 D Heroes_TAG_Main: nextLabel = 3.6.1 labelSplit.length = 3
 11-18 11:23:08.841 20440 20440 D Heroes_TAG_Main: add item nextLabel:3.6.1
 11-18 11:23:08.841 20440 20440 D Heroes_TAG_Main: activityIntent pkg = com.think.heroes componentName = com.think.heroes.chapter3.Chapter_3_6_1
 11-18 11:23:08.841 20440 20440 D Heroes_TAG_Main: label = 第三章/3.6/3.6.2
 11-18 11:23:08.841 20440 20440 D Heroes_TAG_Main: nextLabel = 3.6.2 labelSplit.length = 3
 11-18 11:23:08.841 20440 20440 D Heroes_TAG_Main: add item nextLabel:3.6.2
 11-18 11:23:08.841 20440 20440 D Heroes_TAG_Main: activityIntent pkg = com.think.heroes componentName = com.think.heroes.chapter3.Chapter_3_6_2
 11-18 11:23:08.841 20440 20440 D Heroes_TAG_Main: label = 第四章/第一节
 11-18 11:23:08.842 20440 20440 D Heroes_TAG_Main: label = 第五章/第一节
 11-18 11:23:08.842 20440 20440 D Heroes_TAG_Main: datas = [{intent=Intent { cmp=com.think.heroes/.chapter3.Chapter_3_6_1 }, title=3.6.1}, {intent=Intent { cmp=com.think.heroes/.chapter3.Chapter_3_6_2 }, title=3.6.2}]
 11-18 11:23:08.878 20440 20440 D Heroes_TAG_Main: map = {intent=Intent { cmp=com.think.heroes/.chapter3.Chapter_3_6_1 }, title=3.6.1}
 11-18 11:23:08.880 20440 20440 D Heroes_TAG_Main: map = {intent=Intent { cmp=com.think.heroes/.chapter3.Chapter_3_6_2 }, title=3.6.2}


 11-18 11:23:11.363 20440 20440 D Heroes_TAG_BaseActivity: initActionBar()
 11-18 11:23:11.363 20440 20440 D Heroes_TAG_BaseActivity: initData()
 11-18 11:23:11.363 20440 20440 D Heroes_TAG_BaseActivity: initView()


 */
