package com.icleaner.junk.mycustomadapter;

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

import com.icleaner.clean.mydb.CleanDBHelper;
import com.icleaner.clean.entity.JunkInfo;
import com.icleaner.junk.R;
import com.icleaner.junk.mytools.SetAdUtil;
import com.icleaner.junk.icleaneractivity.GoodGameActivity;
import com.icleaner.clean.utils.LoadManager;

import java.util.ArrayList;

public class JiaGoodGameAdapter extends MybaseAdapter<JunkInfo> {
    private ArrayList<String> game_list;

    public JiaGoodGameAdapter(Context context, ArrayList<String> game_list) {
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
                    if (gameInfo.equals(info.pkg)) {
                        isContains = true;
                    }
                }
                if (!isContains) {
                    CleanDBHelper.getInstance(context).addItem(CleanDBHelper.TableType.GameBoost, info.pkg);
                    game_list.add(info.pkg);
                    SetAdUtil.track("游戏加速页面", "添加游戏到列表", info.label, 1);
                    Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
                    shortcutIntent.setClass(context, GoodGameActivity.class);
                    shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                }
                list.remove(info);
                notifyDataSetChanged();
            }
        });
        holder.iv_icon.setImageDrawable(LoadManager.getInstance(context).getAppIcon(info.pkg));
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
        TextView tv_lable;
        ImageView checkBox;
        TextView tv_zhuangtai;
        RelativeLayout rl_item;
        ImageView iv_icon;
    }
}