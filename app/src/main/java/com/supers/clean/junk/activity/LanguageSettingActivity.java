package com.supers.clean.junk.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.android.clean.util.PreData;
import com.supers.clean.junk.R;

import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/6/19.
 */

public class LanguageSettingActivity extends BaseActivity {

    private ArrayList<LanguageEntity> languageEntities = new ArrayList<>();

    TextView title_name;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_setting);
        initLanguageList();
        ListView listView = (ListView) findViewById(R.id.listview);
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText(R.string.language);

        findViewById(R.id.title_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final LanguageSettingAdapter languageSettingAdapter = new LanguageSettingAdapter();
        listView.setAdapter(languageSettingAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < languageEntities.size(); i++) {
                    LanguageEntity languageEntity = languageEntities.get(i);
                    if (i == position) {
                        languageEntity.checked = true;
                        setLanguage(languageEntity.id);
                    } else {
                        languageEntity.checked = false;
                    }
                }
                title_name.setText(R.string.language);
                languageEntities.get(0).language = getString(R.string.language_system);
                languageSettingAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public ArrayList<LanguageEntity> initLanguageList() {
        LanguageEntity languageEntity = new LanguageEntity(DEFAULT_SYSTEM_LANGUAGE, getString(R.string.language_system));
        languageEntities.add(languageEntity);
        LanguageEntity languageEntity1 = new LanguageEntity("zh", "繁體中文");
        languageEntities.add(languageEntity1);
        LanguageEntity languageEntity2 = new LanguageEntity("cn", "简体中文");
        languageEntities.add(languageEntity2);
        LanguageEntity languageEntity3 = new LanguageEntity("ar", "العربية");
        languageEntities.add(languageEntity3);
        LanguageEntity languageEntity4 = new LanguageEntity("en", "English");
        languageEntities.add(languageEntity4);
        LanguageEntity languageEntity5 = new LanguageEntity("es", "Español");
        languageEntities.add(languageEntity5);
        LanguageEntity languageEntity6 = new LanguageEntity("pt", "Português");
        languageEntities.add(languageEntity6);
        LanguageEntity languageEntity7 = new LanguageEntity("ja", "日本語");
        languageEntities.add(languageEntity7);
        LanguageEntity languageEntity8 = new LanguageEntity("ko", "한국의");
        languageEntities.add(languageEntity8);
        LanguageEntity languageEntity9 = new LanguageEntity("ru", "русский");
        languageEntities.add(languageEntity9);
        LanguageEntity languageEntity10 = new LanguageEntity("tr", "Türkçe");
        languageEntities.add(languageEntity10);
        LanguageEntity languageEntity11 = new LanguageEntity("in", "Indonesian");
        languageEntities.add(languageEntity11);

        String language = PreData.getDB(this, DEFAULT_SYSTEM_LANGUAGE, DEFAULT_SYSTEM_LANGUAGE);

        if (TextUtils.equals(language, DEFAULT_SYSTEM_LANGUAGE)) {
            languageEntities.get(0).checked = true;
        } else {
            for (LanguageEntity lagEntity : languageEntities) {
                if (TextUtils.equals(lagEntity.id, language)) {
                    lagEntity.checked = true;
                    break;
                }
            }
        }

        return languageEntities;
    }


    class LanguageSettingAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return languageEntities.size();
        }

        @Override
        public LanguageEntity getItem(int position) {
            return languageEntities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lag_setting_item, null);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            LanguageEntity languageEntity = getItem(position);
            viewHolder.textView.setText(languageEntity.language);
            viewHolder.checkBox.setChecked(languageEntity.checked);
            return convertView;
        }

        class ViewHolder {
            TextView textView;
            CheckBox checkBox;
        }

    }

    class LanguageEntity {
        public String id;
        public String language;
        public boolean checked;

        public LanguageEntity(String id, String language) {
            this.id = id;
            this.language = language;
        }

    }
}
