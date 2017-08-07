package com.myboost.junk.customadapterboost;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myboost.clean.core.CleanManager;
import com.myboost.clean.privacydb.CleanDBHelper;
import com.myboost.clean.utilsprivacy.LoadManager;
import com.myboost.junk.R;
import com.myboost.clean.entity.JunkInfo;
import com.myboost.junk.boosttools.SetAdUtilPrivacy;

/**
 */

public class IgnoreListViewAdapter extends MybaseAdapter<JunkInfo> {
    private Toast toast;

    public IgnoreListViewAdapter(Context context) {
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (info.isWhiteList) {
            holder.checkBox.setImageResource(R.mipmap.white_jian);
        } else {
            holder.checkBox.setImageResource(R.mipmap.white_add);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.isWhiteList = !info.isWhiteList;
                if (info.isWhiteList) {
                    CleanDBHelper.getInstance(context).addItem(CleanDBHelper.TableType.Ram, info.pkg);
                    showToast(info.label + context.getText(R.string.white_list_jiaru));
                    SetAdUtilPrivacy.track("白名单页面", "选中" + info.label, "", 1);
                } else {
                    showToast(info.label + context.getText(R.string.white_list_yichu));
                    boolean a = CleanDBHelper.getInstance(context).deleteItem(CleanDBHelper.TableType.Ram, info.pkg);
                    Log.e("a", a + "a");
                }
                for (JunkInfo junkInfo : CleanManager.getInstance(context).getAppList()) {
                    if (info.pkg.equals(junkInfo.pkg)) {
                        junkInfo.isWhiteList = info.isWhiteList;
                    }
                }
                list.remove(info);
                notifyDataSetChanged();
            }
        });

        holder.iv_icon.setImageBitmap(getBitmap(LoadManager.getInstance(context).getAppIcon(info.pkg)));
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
        TextView tv_lable;
        ImageView checkBox;
        ImageView iv_icon;
    }
}
