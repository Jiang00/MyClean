package com.example.accessiblity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.baidu.BaiduMap.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by song on 15/9/1.
 */
public class Default extends Activity {
    static final String[] contents = {
            "wa haha",
            "heihei",
            "good",
            "well",
            "a yi",
            "hei er",
            "tian er",
            "tian e hu",
            "go",
            "go go go",
            "come",
            "qu shi go",
            "lai shi come"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        startService(new Intent(getApplicationContext(), Accessibility.class));

        setContentView(R.layout.main);

        final ListView list = (ListView) findViewById(R.id.listview);
        list.setAdapter(new MyAdapter());
        list.setTextFilterEnabled(true);

        SearchView searchView = (SearchView) findViewById(R.id.srv1);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    list.setFilterText(newText);
                } else {
                    list.clearTextFilter();
                }
                return false;
            }
        });
    }

    class MyAdapter extends BaseAdapter implements Filterable {
        List<String> originContents;
        List<String> contents;
        public MyAdapter() {
            originContents = Arrays.asList(Default.contents);
            contents = new ArrayList<>(originContents);
        }

        @Override
        public int getCount() {
            return contents.size();
        }

        @Override
        public Object getItem(int position) {
            return contents.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(Default.this).inflate(R.layout.list_it, parent, false);
            }
            ((TextView) convertView.findViewById(R.id.text)).setText((CharSequence) getItem(position));
            return convertView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    List<String> resultList = new ArrayList<>();
                    for (int i = 0; i < originContents.size(); ++i) {
                        String s = originContents.get(i);
                        if (s.toLowerCase().contains(constraint)) {
                            resultList.add(s);
                        }
                    }
                    results.count = resultList.size();
                    results.values = resultList;
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    contents.clear();
                    contents.addAll((Collection<? extends String>) results.values);
                    notifyDataSetChanged();
                }
            };
            return filter;
        }
    }

    /**
     * 异步执行：
     *      主命令-背景执行-主等待
     *      主命令-背景执行-主执行-回复-主处理
     */
}
