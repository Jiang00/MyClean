package com.security.manager.page;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.privacy.lock.R;


/**
 * Created by SongHualin on 4/21/2015.
 */
public class WidgetSwitch {
    public ImageView icon;

    public TextView title;

    public int idx;

    static int iconNormalColor;
    static int iconSelectedColor;

    public WidgetSwitch(View root) {

       icon= (ImageView) root.findViewById(R.id.icon);
       title= (TextView) root.findViewById(R.id.title);
        if (iconNormalColor == 0){
            iconNormalColor = root.getContext().getResources().getColor(R.color.security_gray_background);
            iconSelectedColor = root.getContext().getResources().getColor(R.color.security_theme_primary);
        }
    }

    public void selected(boolean filter){
        if (filter){
            icon.setColorFilter(iconSelectedColor);
        } else {
            icon.setSelected(true);
        }
        title.setSelected(true);
    }

    public void unselected(boolean filter){
        if (filter){
            icon.setColorFilter(iconNormalColor);
        } else {
            icon.setSelected(false);
        }
        title.setSelected(false);
    }

    public static class Data {
        public String title;
        public int icon;
        public boolean active;
        public boolean filter;

        public Data(String title, int icon, boolean active, boolean filter) {
            this.title = title;
            this.icon = icon;
            this.active = active;
            this.filter = filter;
        }
    }
}
