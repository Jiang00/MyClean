package com.supers.clean.junk.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.MyApplication;
import com.supers.clean.junk.entity.JunkInfo;
import com.android.clean.util.CommonUtil;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.PreData;

/**
 */

public class WhiteListAdapter extends MybaseAdapter<JunkInfo> {
    private Toast toast;

    public WhiteListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final JunkInfo info = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_white_list_item, null);
            holder = new ViewHolder();
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
        holder.tv_zhuangtai.setVisibility(View.VISIBLE);
        if (info.isWhiteList) {
            holder.checkBox.setImageResource(R.mipmap.white_jian);
            holder.tv_zhuangtai.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setImageResource(R.mipmap.white_jia);
            holder.tv_zhuangtai.setVisibility(View.GONE);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.isWhiteList = !info.isWhiteList;
                if (info.isWhiteList) {

                    PreData.addName(context, info.packageName, Constant.WHILT_LIST);
                    showToast(info.label + context.getText(R.string.white_list_jiaru));
                    CommonUtil.track("白名单页面", "选中" + info.label, "", 1);
                } else {
                    showToast(info.label + context.getText(R.string.white_list_yichu));
                    PreData.removeName(context, info.packageName, Constant.WHILT_LIST);
                }
                for (JunkInfo junkInfo : ((MyApplication) ((Activity) context).getApplication()).getListMng()) {
                    if (info.packageName.equals(junkInfo.packageName)) {
                        junkInfo.isWhiteList = info.isWhiteList;
                    }
                }
                list.remove(info);
                notifyDataSetChanged();
            }
        });
        holder.iv_icon.setImageBitmap(getBitmap(info.icon));
        holder.tv_lable.setText(info.label);

        return convertView;
    }

    public void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();

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
    }
}
