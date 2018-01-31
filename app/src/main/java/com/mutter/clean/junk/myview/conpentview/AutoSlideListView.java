package com.mutter.clean.junk.myview.conpentview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

import com.mutter.clean.junk.R;
import com.mutter.clean.junk.entity.SideInfo;
import com.mutter.clean.junk.myAdapter.SideAdapter;
import com.mutter.clean.junk.myview.ListViewForScrollView;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.util.PreData;
import com.mutter.module.charge.saver.Util.Constants;
import com.mutter.module.charge.saver.Util.Utils;

/**
 * Created by ${} on 2018/1/30.
 */

public class AutoSlideListView extends ListViewForScrollView {
    Context mContext;
    private SideAdapter adapter;

    public AutoSlideListView(Context context) {
        this(context, null);
    }

    public AutoSlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initSideData();
    }

    public void initSideData() {
        if (adapter == null) {
            adapter = new SideAdapter(mContext);
            setAdapter(adapter);
        }
        adapter.clear();
        adapter.addData(new SideInfo(R.string.side_charging, R.mipmap.side_charging, (boolean) Utils.readData(mContext, Constants.CHARGE_SAVER_SWITCH, false)));//充电屏保
        adapter.addData(new SideInfo(R.string.side_float, R.mipmap.side_float, PreData.getDB(mContext, Constant.FlOAT_SWITCH, true)));//桌面悬浮球
        adapter.addData(new SideInfo(R.string.side_junk, R.mipmap.side_junk));//垃圾清理
        adapter.addData(new SideInfo(R.string.side_ram, R.mipmap.side_ram));//内存加速
        adapter.addData(new SideInfo(R.string.main_cooling_name, R.mipmap.side_battery));//电池降温
        adapter.addData(new SideInfo(R.string.side_power, R.mipmap.side_power));//深度清理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            adapter.addData(new SideInfo(R.string.side_notifi, R.mipmap.side_nitifi));//通知栏清理
        }
        adapter.addData(new SideInfo(R.string.gboost_0, R.mipmap.gboost_side));//游戏加速
        adapter.addData(new SideInfo(R.string.side_manager, R.mipmap.side_manager));//应用管理
        adapter.addData(new SideInfo(R.string.side_picture, R.mipmap.side_picture));//相似图片
        adapter.addData(new SideInfo(R.string.side_file, R.mipmap.side_file));//文件管理
        adapter.addData(new SideInfo(R.string.main_msg_title, R.mipmap.side_message));//硬件信息
        adapter.addData(new SideInfo(R.string.side_setting, R.mipmap.side_setting));//设置
        adapter.addData(new SideInfo(R.string.side_rotate, R.mipmap.side_rotate));//好评
        adapter.notifyDataSetChanged();
    }

}
