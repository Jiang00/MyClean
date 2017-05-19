package com.supers.clean.junk.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.clean.db.CleanDBHelper;
import com.android.clean.util.Util;
import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.GBoostActivity;
import com.supers.clean.junk.entity.JunkInfo;

import java.util.ArrayList;

/**
 */

public class AddGameAdapter extends MybaseAdapter<JunkInfo> {
    private Toast toast;
    private ArrayList<String> game_list;

    public AddGameAdapter(Context context, ArrayList<String> game_list) {
        super(context);
        this.game_list = game_list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final JunkInfo info = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_white_list_item, null);
            holder = new ViewHolder();
            holder.rl_item = (RelativeLayout) convertView
                    .findViewById(R.id.rl_item);
            holder.checkBox = (ImageView) convertView
                    .findViewById(R.id.iv_check);
            holder.iv_icon = (ImageView) convertView
                    .findViewById(R.id.iv_le);
            holder.tv_lable = (TextView) convertView
                    .findViewById(R.id.tv_name);
            holder.tv_zhuangtai = (TextView) convertView
                    .findViewById(R.id.tv_zhuangtai);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isContains = false;
                for (String gameInfo : game_list) {
                    if (gameInfo.equals(info.packageName)) {
                        isContains = true;
                    }
                }
                if (!isContains) {
                    CleanDBHelper.getInstance(context).addItem(CleanDBHelper.TableType.GameBoost,info.packageName);
                    game_list.add(info.packageName);
                    Util.track("游戏加速页面", "添加游戏到列表", info.label, 1);
                    Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
                    shortcutIntent.setClass(context, GBoostActivity.class);
                    shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);

                }
                list.remove(info);
                notifyDataSetChanged();
            }
        });
        holder.iv_icon.setImageBitmap(getBitmap(info.icon));
        holder.tv_lable.setText(info.label);

        return convertView;
    }

    private Bitmap getBitmap(Drawable drawable) {
        // TODO Auto-generated method stub
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    public class ViewHolder {
        ImageView checkBox;
        ImageView iv_icon;
        TextView tv_lable;
        TextView tv_zhuangtai;
        RelativeLayout rl_item;
    }
}
