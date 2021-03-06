package com.supers.clean.junk.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.clean.util.PreData;
import com.android.theme.internal.data.Theme;
import com.eos.eshop.ShopMaster;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.Utils;
import com.sample.lottie.LottieAnimationView;
import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.JunkActivity;
import com.supers.clean.junk.activity.ManagerActivity;
import com.supers.clean.junk.activity.RamAvtivity;
import com.supers.clean.junk.activity.SettingActivity;
import com.supers.clean.junk.activity.ToolActivity;
import com.supers.clean.junk.entity.SideInfo;
import com.supers.clean.junk.service.FloatService;
import com.supers.clean.junk.util.AdUtil;
import com.supers.clean.junk.util.UtilGp;
import com.android.clean.util.Constant;


public class SideAdapter extends MybaseAdapter<SideInfo> {
    private static int idx = 0;
    private static final int BATTERY = idx++;
    private static final int DETECT = idx++;
    private static final int FLOAT = idx++;
    private static final int JUNK = idx++;
    private static final int RAM = idx++;
    private static final int MANAGER = idx++;
    private static final int TOOL = idx++;
    private static final int FAMILY = idx++;
    private static final int THEME = idx++;
    private static final int SETTING = idx++;
    private static final int ROTATE = idx++;
    private AlertDialog dialog;
    private AnimatorSet animatorSet_rotate;
    Handler handler;

    public SideAdapter(Context context, Handler handler) {
        super(context);
        this.handler = handler;
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
            holder.checkBox = (ImageView) convertView
                    .findViewById(R.id.iv_check);
            holder.lot_family = (LottieAnimationView) convertView
                    .findViewById(R.id.lot_family);
            holder.iv_hong = (ImageView) convertView
                    .findViewById(R.id.iv_hong);
            holder.side_ad = (ImageView) convertView
                    .findViewById(R.id.side_ad);
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
        if (position == FAMILY) {
            holder.lot_family.setImageAssetsFolder("images/box/");
            holder.lot_family.setAnimation("box.json");
            holder.lot_family.loop(true);
            holder.lot_family.playAnimation();
            holder.side_ad.setVisibility(View.VISIBLE);
        } else {
            holder.lot_family.setVisibility(View.GONE);
            holder.side_ad.setVisibility(View.GONE);
            holder.iv_le.setImageResource(info.drawableId);
        }
        if (info.isCheck) {
            holder.checkBox.setImageResource(R.mipmap.side_check_passed);
        } else {
            holder.checkBox.setImageResource(R.mipmap.side_check_normal);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.isCheck = !info.isCheck;
                if (info.isCheck) {
                    holder.checkBox.setImageResource(R.mipmap.side_check_passed);
                } else {
                    holder.checkBox.setImageResource(R.mipmap.side_check_normal);
                }
                holder.iv_hong.setVisibility(View.GONE);
                onC(position);
            }
        });
//        convertView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        holder.iv_le.setColorFilter(ContextCompat.getColor(context, R.color.A1));
//                        holder.tv_name.setTextColor(ContextCompat.getColor(context, R.color.A1));
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                    case MotionEvent.ACTION_UP:
//                        holder.iv_le.setColorFilter(0);
//                        holder.tv_name.setTextColor(ContextCompat.getColor(context, R.color.B2));
//                        break;
//                }
//                return false;
//            }
//        });
        if (position == BATTERY || position == FLOAT || position == DETECT) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.iv_hong.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        hongV(holder, position);
        if (position == JUNK || position == FAMILY) {
            holder.side_divide.setVisibility(View.VISIBLE);
        } else {
            holder.side_divide.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void hongV(ViewHolder holder, int position) {
        if (position == JUNK) {
            if (PreData.getDB(context, Constant.HONG_JUNK, true)) {
                holder.iv_hong.setVisibility(View.VISIBLE);
            } else {
                holder.iv_hong.setVisibility(View.GONE);
            }
        }
        if (position == RAM) {
            if (PreData.getDB(context, Constant.HONG_RAM, true)) {
                holder.iv_hong.setVisibility(View.VISIBLE);
            } else {
                holder.iv_hong.setVisibility(View.GONE);
            }
        }
        if (position == MANAGER) {
            if (PreData.getDB(context, Constant.HONG_MANAGER, true)) {
                holder.iv_hong.setVisibility(View.VISIBLE);
            } else {
                holder.iv_hong.setVisibility(View.GONE);
            }
        }
    }

    private void onC(int position) {
        if (position == BATTERY) {
            if ((boolean) Utils.readData(context, Constants.CHARGE_SAVER_SWITCH, false)) {
                AdUtil.track("侧边栏", "点击关闭充电屏保", "", 1);
                Utils.writeData(context, Constants.CHARGE_SAVER_SWITCH, false);
            } else {
                AdUtil.track("侧边栏", "点击开启充电屏保", "", 1);
                Utils.writeData(context, Constants.CHARGE_SAVER_SWITCH, true);
            }
        } else if (position == DETECT) {
            if (PreData.getDB(context, Constant.DETECT_KAIGUAN, true)) {
                AdUtil.track("侧边栏", "点击关闭充电检测", "", 1);
                PreData.putDB(context, Constant.DETECT_KAIGUAN, false);
            } else {
                AdUtil.track("侧边栏", "点击开启充电检测", "", 1);
                PreData.putDB(context, Constant.DETECT_KAIGUAN, true);
            }
        } else if (position == FLOAT) {
            if (PreData.getDB(context, Constant.FlOAT_SWITCH, true)) {
                AdUtil.track("侧边栏", "点击关闭悬浮窗", "", 1);
                PreData.putDB(context, Constant.FlOAT_SWITCH, false);
                Intent intent1 = new Intent(context, FloatService.class);
                context.stopService(intent1);
            } else {
                AdUtil.track("侧边栏", "点击开启悬浮窗", "", 1);
                PreData.putDB(context, Constant.FlOAT_SWITCH, true);
                Intent intent1 = new Intent(context, FloatService.class);
                context.startService(intent1);
            }
        } else if (position == JUNK) {
            AdUtil.track("侧边栏", "点击进入垃圾页面", "", 1);
            Intent intent2 = new Intent(context, JunkActivity.class);
            ((Activity) context).startActivityForResult(intent2, 1);
        } else if (position == RAM) {
            AdUtil.track("侧边栏", "点击进入ram页面", "", 1);
            Intent intent3 = new Intent(context, RamAvtivity.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == MANAGER) {
            AdUtil.track("侧边栏", "点击进入应用管理页面", "", 1);
            Intent intent4 = new Intent(context, ManagerActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == TOOL) {
            AdUtil.track("侧边栏", "点击进入工具箱页面", "", 1);
            Intent intent4 = new Intent(context, ToolActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == FAMILY) {
            AdUtil.track("侧边栏", "点击进入family页面", "", 1);
            ShopMaster.launch(context, "EOS_Family",
                    new Theme(R.raw.battery_0, context.getPackageName()));
        } else if (position == THEME) {
            AdUtil.track("侧边栏", "点击进入主题页面", "", 1);
            ShopMaster.launch(context,
                    new Theme(R.raw.battery_0, context.getPackageName()));
        } else if (position == SETTING) {
            AdUtil.track("侧边栏", "点击进入设置页面", "", 1);
            Intent intent9 = new Intent(context, SettingActivity.class);
            ((Activity) context).startActivityForResult(intent9, 1);
        } else if (position == ROTATE) {
            AdUtil.track("侧边栏", "点击好评", "", 1);
            showExitDialog();
        }
//        else if (position == LAG_SETTING) {
//            AdUtil.track("侧边栏", "点击多语言", "", 1);
//            Intent intent9 = new Intent(context, LanguageSettingActivity.class);
//            ((Activity) context).startActivityForResult(intent9, Constant.LANGUAGE_RESUIL);
//        }
        /* else if (position == PRIVARY) {
            AdUtil.track("侧边栏", "点击隐私清理", "", 1);
            Intent intent9 = new Intent(context, PrivacyActivity.class);
            ((Activity) context).startActivityForResult(intent9, 1);
        }*/
    }

    private void showExitDialog() {
        View view = View.inflate(context, R.layout.dialog_rotate, null);
        ImageView rotate_delete = (ImageView) view.findViewById(R.id.rotate_delete);
        ImageView rotate_ic = (ImageView) view.findViewById(R.id.rotate_ic);
        Button main_rotate_good = (Button) view.findViewById(R.id.main_rotate_good);
        animatorSet_rotate = new AnimatorSet();
        ObjectAnimator objectAnimator_0 = ObjectAnimator.ofFloat(rotate_ic, View.ALPHA, 1, 0);
        objectAnimator_0.setDuration(800);
        ObjectAnimator objectAnimator_1 = ObjectAnimator.ofFloat(rotate_ic, View.SCALE_X, 0, 1);
        objectAnimator_1.setDuration(700);
        ObjectAnimator objectAnimator_2 = ObjectAnimator.ofFloat(rotate_ic, View.SCALE_Y, 0, 1);
        objectAnimator_2.setDuration(700);
        ObjectAnimator objectAnimator_3 = ObjectAnimator.ofFloat(rotate_ic, View.ALPHA, 0, 1);
        objectAnimator_3.setDuration(700);
        animatorSet_rotate.play(objectAnimator_1).with(objectAnimator_2).with(objectAnimator_3).after(objectAnimator_0);
        handler.postDelayed(runnable_rotate, 1000);
        main_rotate_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UtilGp.rate(context);
            }
        });
        rotate_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog = new AlertDialog.Builder(context, R.style.exit_dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (handler != null) {
                    handler.removeCallbacks(runnable_rotate);
                    animatorSet_rotate.cancel();
                }
            }
        });
    }

    Runnable runnable_rotate = new Runnable() {
        @Override
        public void run() {
            if (animatorSet_rotate != null) {
                animatorSet_rotate.start();
                handler.postDelayed(this, 3000);
            }
        }
    };

    private Bitmap getBitmap(Drawable icon) {
        // TODO Auto-generated method stub
        Bitmap bitmap = null;
        bitmap = ((BitmapDrawable) icon).getBitmap();
        return bitmap;
    }

    public class ViewHolder {
        RelativeLayout rl_item;
        ImageView checkBox;
        ImageView iv_le;
        ImageView iv_hong;
        ImageView side_ad;
        LottieAnimationView lot_family;
        TextView tv_name;
        View side_divide;
    }

}
