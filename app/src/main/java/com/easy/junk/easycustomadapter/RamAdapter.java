package com.easy.junk.easycustomadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easy.clean.easydb.CleanDBHelper;
import com.easy.clean.easyutils.LoadManager;
import com.easy.clean.easyutils.MyUtils;
import com.easy.junk.R;
import com.easy.clean.entity.JunkInfo;
import com.easy.junk.easypresenter.EasyPresenterRam;

import java.util.List;

public class RamAdapter extends EasyBaseAdapter<JunkInfo> {
    private AlertDialog dialog;
    private List<String> white_list;
    EasyPresenterRam ramPresenter;
    AllListener listener;

    public RamAdapter(Context context) {
        super(context);
    }

    public RamAdapter(Context context, EasyPresenterRam ramPresenter) {
        super(context);
        this.ramPresenter = ramPresenter;
        white_list = CleanDBHelper.getInstance(context).getWhiteList(CleanDBHelper.TableType.Ram);
        lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024) / 4) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 返回用户定义的item的大小，默认返回1代表item的数量.重写此方法来衡量每张图片的大小。
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void setOnlistener(AllListener listener) {
        this.listener = listener;
    }

    @Override
    public void upList(List<JunkInfo> list) {
        super.upList(list);
        white_list = CleanDBHelper.getInstance(context).getWhiteList(CleanDBHelper.TableType.Ram);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final JunkInfo info = getItem(position);

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater
                    .inflate(R.layout.layout_ram_item, null);
            holder.name = (TextView) convertView
                    .findViewById(R.id.junk_item_lable);
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.junk_item_icon);
            holder.checkBox = (ImageView) convertView
                    .findViewById(R.id.check_iv);
            holder.size = (TextView) convertView
                    .findViewById(R.id.size_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (info.label == null) {
            info.label = LoadManager.getInstance(context).getAppLabel(info.pkg);
        }
        holder.name.setText(info.label);
        holder.icon.setImageDrawable(LoadManager.getInstance(context).getAppIcon(info.pkg));
        if (white_list.contains(info.pkg)) {
            info.isChecked = false;
        }
        if (info.isChecked) {
            holder.checkBox.setImageResource(R.mipmap.ram_passed);
        } else {
            holder.checkBox.setImageResource(R.mipmap.ram_normal);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            boolean isAdd;

            @Override
            public void onClick(View v) {
                info.isChecked = !info.isChecked;
                if (info.isChecked) {
                    isAdd = true;
                    holder.checkBox.setImageResource(R.mipmap.ram_passed);
                    ramPresenter.addCleandata(true, info.size);
                } else {
                    if (!isAdd) {
                        showDialog(holder.icon.getDrawable(), info.label, info.pkg);
                    }
                    holder.checkBox.setImageResource(R.mipmap.ram_normal);
                    ramPresenter.addCleandata(false, info.size);
                }
            }
        });
        holder.size.setText(MyUtils.convertStorage(info.size, true));

        return convertView;
    }

    private void showDialog(Drawable drawable, String label, final String pkg) {
        View view = View.inflate(context, R.layout.dialog_file, null);
        ImageView dialog_icon = (ImageView) view.findViewById(R.id.dialog_icon);
        TextView message = (TextView) view.findViewById(R.id.message);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        dialog_icon.setImageDrawable(drawable);
        message.setText(context.getString(R.string.add_white, label));
        dialog = new AlertDialog.Builder(context).create();
        dialog.setView(view);
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ramPresenter.addWhiteList(pkg);
                CleanDBHelper.getInstance(context).addItem(CleanDBHelper.TableType.Ram, pkg);
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public class ViewHolder {
        TextView name;
        ImageView icon;
        ImageView checkBox;
        TextView size;
    }

    public interface AllListener {
        void onChecked(boolean check);
    }
}
