package com.vector.cleaner.madapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vector.cleaner.activity.BatteryCoolingActivity;
import com.vector.cleaner.activity.DyxGboostActivity;
import com.vector.cleaner.activity.NotificationActivity;
import com.vector.cleaner.aservice.MemoryService;
import com.vector.cleaner.entity.SideInfo;
import com.vector.cleaner.activity.AllAppActivity;
import com.vector.cleaner.activity.LajiFileActivity;
import com.vector.cleaner.activity.NotifiYindaoActivity;
import com.vector.cleaner.activity.DeepActivity;
import com.vector.cleaner.activity.SettingActivity;
import com.vector.cleaner.utils.AdUtil;
import com.vector.cleaner.utils.Constant;
import com.vector.cleaner.utils.UtilGp;
import com.vector.mcleaner.mutil.PreData;
import com.vector.mcleaner.mutil.Util;
import com.vector.module.Util.Constants;
import com.vector.module.Util.Utils;
import com.vector.cleaner.R;
import com.vector.cleaner.activity.FilesActivity;
import com.vector.cleaner.activity.SimilarPhotoActivity;
import com.vector.cleaner.activity.ANeicunAvtivity;


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
            Intent intent2 = new Intent(context, LajiFileActivity.class);
            ((Activity) context).startActivityForResult(intent2, 1);
        } else if (position == COOLING) {
            AdUtil.track("侧边栏", "点击进入cooling页面", "", 1);
            Intent intent3 = new Intent(context, BatteryCoolingActivity.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == RAM) {
            AdUtil.track("侧边栏", "点击进入ram页面", "", 1);
            Intent intent3 = new Intent(context, ANeicunAvtivity.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == MANAGER) {
            AdUtil.track("侧边栏", "点击进入应用管理页面", "", 1);
            Intent intent4 = new Intent(context, AllAppActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == GAME) {
            AdUtil.track("侧边栏", "点击进入游戏加速页面", "", 1);
            Intent intent4 = new Intent(context, DyxGboostActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == FILE) {
//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//            ((Activity) context).startActivityForResult(intent, 1);
            AdUtil.track("侧边栏", "点击进入文件管理页面", "", 1);
            PreData.putDB(context, Constant.FILE_CLEAN, true);
            Intent intent5 = new Intent(context, FilesActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == POWER) {
            AdUtil.track("侧边栏", "点击进入深度清理页面", "", 1);
            Intent intent5 = new Intent(context, DeepActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == NOTIFI) {
            AdUtil.track("侧边栏", "点击进入通知栏清理页面", "", 1);
            PreData.putDB(context, Constant.NOTIFI_CLEAN, true);
            if (!Util.isNotificationListenEnabled(context) || !PreData.getDB(context, Constant.KEY_NOTIFI, true)) {
                Intent intent6 = new Intent(context, NotifiYindaoActivity.class);
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
            UtilGp.rate(context);
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
