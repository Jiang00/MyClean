package com.easy.junk.easycustomview;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easy.junk.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Lody
 */
public class GridViewAdAdapter extends BaseAdapter {
    public Context context;
    public static final int MSG_START_AD_ANIMAL = 100;

    public static final int DEFAULT_INTERVAL_TIME = 5000;

    public HashMap<Integer, View> nativeAdMap;
    private PackageManager pm;
    private ArrayList<String> list;
    protected LayoutInflater mInflater;

    public GridViewAdAdapter(Context context, ArrayList<String> list) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        pm = context.getPackageManager();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.layout_gboost_item, null);
        LinearLayout linearlayou_add = (LinearLayout) convertView.findViewById(R.id.linearlayou_add);
//        LinearLayout linearlayout_qita = (LinearLayout) convertView.findViewById(R.id.linearlayout_qita);
        ImageView gboost_item_icon = (ImageView) convertView.findViewById(R.id.gboost_item_icon);
        ImageView gboost_item_add = (ImageView) convertView.findViewById(R.id.gboost_item_add);
        TextView gboost_item_name = (TextView) convertView.findViewById(R.id.gboost_item_name);
        TextView gboost_item_addname = (TextView) convertView.findViewById(R.id.gboost_item_addname);
        if (TextUtils.equals(context.getString(R.string.gboost_7), list.get(position))) {
//            linearlayout_qita.setVisibility(View.GONE);
            gboost_item_add.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.gboost_add));
            gboost_item_addname.setText(list.get(position));
        } else {
            try {
//                linearlayou_add.setVisibility(View.GONE);
                ApplicationInfo info = pm.getApplicationInfo(list.get(position), 0);
                gboost_item_add.setImageDrawable(info.loadIcon(pm));
                gboost_item_addname.setText(info.loadLabel(pm));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }
}

