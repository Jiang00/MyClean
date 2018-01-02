package com.security.cleaner.madapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.security.cleaner.activity.CpuCoolingActivity;
import com.security.cleaner.activity.GameGboostActivity;
import com.security.cleaner.activity.NotifiPermissActivity;
import com.security.cleaner.activity.NotificationActivity;
import com.security.cleaner.entity.SideInfo;
import com.security.cleaner.activity.AppManagerActivity;
import com.security.cleaner.activity.JunkFileActivity;
import com.security.cleaner.activity.SettingActivity;
import com.security.cleaner.utils.AdUtil;
import com.security.cleaner.utils.Constant;
import com.security.cleaner.utils.UtilGp;
import com.security.mcleaner.mutil.PreData;
import com.security.mcleaner.mutil.Util;
import com.security.cleaner.R;
import com.security.cleaner.activity.FilesManagerActivity;
import com.security.cleaner.activity.SimilarPhotoActivity;
import com.security.cleaner.activity.ARamAvtivity;


public class MSideAdapter extends MybaseAdapter<SideInfo> {

    private int COOLING = -1;
    private int RAM = -1;
    private int JUNK = -1;
    private int MANAGER = -1;
    private int GAME = -1;
    private int FILE = -1;
    private int POWER = -1;

    private int PICTURE = -1;
    private int NOTIFI = -1;
    private int SETTING = -1;
    private int ROTATE = -1;

    public MSideAdapter(Context context) {
        super(context);
        int idx = 0;
        COOLING = idx++;
        RAM = idx++;
        JUNK = idx++;
        MANAGER = idx++;
        GAME = idx++;
//        FILE = idx++;
//        POWER = idx++;
        NOTIFI = idx++;
//        PICTURE = idx++;
        ROTATE = idx++;
        SETTING = idx++;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SideInfo info = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_side_item, null);
            holder = new ViewHolder();
            holder.rl_item = (RelativeLayout) convertView
                    .findViewById(R.id.rl_item);
            holder.iv_le = (ImageView) convertView
                    .findViewById(R.id.iv_le);
            holder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            holder.side_divide = convertView
                    .findViewById(R.id.side_divide);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(info.textId);
        holder.iv_le.setImageResource(info.drawableId);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onC(position);
            }
        });
        if (position == ROTATE) {
            holder.side_divide.setVisibility(View.VISIBLE);
        } else {
            holder.side_divide.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void onC(int position) {
        if (position == JUNK) {
            AdUtil.track("侧边栏", "点击进入垃圾页面", "", 1);
            Intent intent2 = new Intent(context, JunkFileActivity.class);
            ((Activity) context).startActivityForResult(intent2, 1);
        } else if (position == COOLING) {
            AdUtil.track("侧边栏", "点击进入cooling页面", "", 1);
            Intent intent3 = new Intent(context, CpuCoolingActivity.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == RAM) {
            AdUtil.track("侧边栏", "点击进入ram页面", "", 1);
            Intent intent3 = new Intent(context, ARamAvtivity.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == MANAGER) {
            AdUtil.track("侧边栏", "点击进入应用管理页面", "", 1);
            Intent intent4 = new Intent(context, AppManagerActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == GAME) {
            AdUtil.track("侧边栏", "点击进入游戏加速页面", "", 1);
            Intent intent4 = new Intent(context, GameGboostActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == FILE) {
//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//            ((Activity) context).startActivityForResult(intent, 1);
            AdUtil.track("侧边栏", "点击进入文件管理页面", "", 1);
            PreData.putDB(context, Constant.FILE_CLEAN, true);
            Intent intent5 = new Intent(context, FilesManagerActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == NOTIFI) {
            AdUtil.track("侧边栏", "点击进入通知栏清理页面", "", 1);
            PreData.putDB(context, Constant.NOTIFI_CLEAN, true);
            if (!Util.isNotificationListenEnabled(context) || !PreData.getDB(context, Constant.KEY_NOTIFI, true)) {
                Intent intent6 = new Intent(context, NotifiPermissActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
            } else {
                Intent intent6 = new Intent(context, NotificationActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
            }
        } else if (position == PICTURE) {
            AdUtil.track("侧边栏", "点击进入相似图片", "", 1);
            PreData.putDB(context, Constant.PHOTO_CLEAN, true);
            Intent intent = new Intent(context, SimilarPhotoActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == SETTING) {
            AdUtil.track("侧边栏", "点击进入设置页面", "", 1);
            Intent intent9 = new Intent(context, SettingActivity.class);
            ((Activity) context).startActivityForResult(intent9, 1);
        } else if (position == ROTATE) {
            AdUtil.track("侧边栏", "点击好评", "", 1);
            UtilGp.rate(context.getApplicationContext());
        }
    }

    private Bitmap getBitmap(Drawable icon) {
        // TODO Auto-generated method stub
        Bitmap bitmap = null;
        bitmap = ((BitmapDrawable) icon).getBitmap();
        return bitmap;
    }

    public class ViewHolder {
        ImageView checkBox;
        TextView tv_name;
        RelativeLayout rl_item;
        View side_divide;
        ImageView iv_le;
    }

}
