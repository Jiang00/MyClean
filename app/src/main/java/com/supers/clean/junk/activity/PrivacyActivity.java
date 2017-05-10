package com.supers.clean.junk.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Downloads;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.supers.clean.junk.R;

/**
 * Created by Ivy on 2017/5/9.
 */

public class PrivacyActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    ImageView privary_small_1, privary_small_2, privary_small_3, privary_small_4, privary_small_5;
    ImageView privary_line_1, privary_line_2;
    ImageView privary_yuan_1, privary_yuan_2, privary_yuan_3;
    ImageView privary_cut_1, privary_cut_2, privary_browser_1, privary_browser_2, privary_call_1, privary_call_2;


    private AnimatorSet animatorSmall, animationLine_1, animationLine_2;
    private ObjectAnimator animator;
    private AnimatorSet animator_cut_2, animator_browser_2, animator_call_2;
    private ObjectAnimator animator_cut, animator_browser, animator_call;
    private Handler mHandler;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        privary_small_1 = (ImageView) findViewById(R.id.privary_small_1);
        privary_small_2 = (ImageView) findViewById(R.id.privary_small_2);
        privary_small_3 = (ImageView) findViewById(R.id.privary_small_3);
        privary_small_4 = (ImageView) findViewById(R.id.privary_small_4);
        privary_small_5 = (ImageView) findViewById(R.id.privary_small_5);
        privary_line_1 = (ImageView) findViewById(R.id.privary_line_1);
        privary_line_2 = (ImageView) findViewById(R.id.privary_line_2);
        privary_yuan_1 = (ImageView) findViewById(R.id.privary_yuan_1);
        privary_yuan_2 = (ImageView) findViewById(R.id.privary_yuan_2);
        privary_yuan_3 = (ImageView) findViewById(R.id.privary_yuan_3);
        privary_cut_1 = (ImageView) findViewById(R.id.privary_cut_1);
        privary_cut_2 = (ImageView) findViewById(R.id.privary_cut_2);
        privary_browser_1 = (ImageView) findViewById(R.id.privary_browser_1);
        privary_browser_2 = (ImageView) findViewById(R.id.privary_browser_2);
        privary_call_1 = (ImageView) findViewById(R.id.privary_call_1);
        privary_call_2 = (ImageView) findViewById(R.id.privary_call_2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_privary);
        mHandler = new Handler();
        title_name.setText(R.string.privary_0);
        title_left.setOnClickListener(clickListener);
        animationSmall();
        animationLine();
        animationYuan();
        //获取剪贴板管理服务
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(null, null));
        if (cm.hasPrimaryClip() && !TextUtils.isEmpty(cm.getPrimaryClip().getItemAt(0).getText())) {
            String a = cm.getPrimaryClip().getItemAt(0).getText().toString();
            int count = cm.getPrimaryClip().getItemCount();
            Log.e("privary", a + "==" + count);
        }
        history();
        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
        Cursor cursor = manager.query(query);
        int count = 0;
        while (cursor != null && cursor.moveToNext()) {
            count++;
            Log.e("privary1", count + "");
        }
        ContentResolver resolver = getContentResolver();
        Cursor cursor1 = resolver.query(Downloads.Impl.ALL_DOWNLOADS_CONTENT_URI,
                new String[]{Downloads.Impl._ID}, null, null, null);
        //获取所有的DOWNLOADS任务
        Cursor cursor2 = resolver.query(Downloads.Impl.ALL_DOWNLOADS_CONTENT_URI,
                null, null, null, null);
        int idColumn1 = cursor1.getColumnIndexOrThrow(Downloads.Impl._ID);
        int idColumn = cursor2.getColumnIndexOrThrow(Downloads.Impl._ID);
        while (cursor2 != null && cursor2.moveToNext()) {
            count++;
            Log.e("privary2", count + "");
        }
        while (cursor1 != null && cursor1.moveToNext()) {
            count++;
            Log.e("privary2", count + "");
        }
    }

    private String history() {
        String string = null;
        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(
                Uri.parse("content://browser/bookmarks"),
                new String[]{"url"}, null, null, null);

        while (cursor != null && cursor.moveToNext()) {
            string = cursor.getString(cursor.getColumnIndex("url"));
            Log.e("privary1", string == null ? "null" : string);
        }
        return string;
    }

    private void animationCut() {
        //获取剪贴板管理服务
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        cm.setPrimaryClip(ClipData.newPlainText(null, "内容"));
        if (cm.hasPrimaryClip()) {
            String a = cm.getPrimaryClip().getItemAt(0).getText().toString();
            int count = cm.getPrimaryClip().getItemCount();
            Log.e("privary", a + "==" + count);
        }

        animator_cut = ObjectAnimator.ofFloat(privary_cut_1, "rotation", 0, 359);
        animator_cut.setRepeatCount(-1);
        animator_cut.setInterpolator(new LinearInterpolator());
        animator_cut.setDuration(1000);
        animator_cut.start();
        privary_cut_2.setVisibility(View.INVISIBLE);
    }

    private void animationCut2() {
        animator_cut_2 = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(privary_cut_2, "scaleX", 0, 1);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(privary_cut_2, "scaleY", 0, 1);
        animator_cut_2.setDuration(500);
        animator_cut_2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator_cut_2.play(animatorX).with(animatorY);
        animator_cut_2.start();
        privary_cut_2.setImageResource(R.drawable.shape_privary_1);
        privary_cut_2.setVisibility(View.VISIBLE);
    }

    private void animationYuan() {
        animator = ObjectAnimator.ofFloat(privary_yuan_3, "rotation", 0, 359);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1000);
        animator.start();
    }

    private void animationLine() {
        animationLine_1 = new AnimatorSet();
        ObjectAnimator animator_ine_1_x = ObjectAnimator.ofFloat(privary_line_1, "scaleX", 0, 2.2f);
        animator_ine_1_x.setRepeatCount(-1);
        ObjectAnimator animator_ine_1_y = ObjectAnimator.ofFloat(privary_line_1, "scaleY", 0, 2.2f);
        animator_ine_1_y.setRepeatCount(-1);
        animationLine_1.setDuration(1000);
        animationLine_1.setInterpolator(new AccelerateDecelerateInterpolator());
        animationLine_1.playTogether(animator_ine_1_x, animator_ine_1_y);

        animationLine_2 = new AnimatorSet();
        ObjectAnimator animator_ine_2_x = ObjectAnimator.ofFloat(privary_line_2, "scaleX", 0, 1.4f);
        animator_ine_2_x.setRepeatCount(-1);
        ObjectAnimator animator_ine_2_y = ObjectAnimator.ofFloat(privary_line_2, "scaleY", 0, 1.4f);
        animator_ine_2_y.setRepeatCount(-1);
        animationLine_2.setDuration(1000);
        animationLine_2.setInterpolator(new AccelerateDecelerateInterpolator());
        animationLine_2.playTogether(animator_ine_2_x, animator_ine_2_y);
        animationLine_1.start();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationLine_2.start();
                privary_line_2.setVisibility(View.VISIBLE);
            }
        }, 500);

    }

    private void animationSmall() {
        animatorSmall = new AnimatorSet();
        ObjectAnimator animatoX_1 = ObjectAnimator.ofFloat(privary_small_1, "scaleX", 0f, 1f, 0f);
        animatoX_1.setRepeatCount(-1);
        ObjectAnimator animatoY_1 = ObjectAnimator.ofFloat(privary_small_1, "scaleY", 0f, 1f, 0f);
        animatoY_1.setRepeatCount(-1);
        ObjectAnimator animatoX_2 = ObjectAnimator.ofFloat(privary_small_2, "scaleX", 0f, 1f, 0f);
        animatoX_2.setRepeatCount(-1);
        ObjectAnimator animatoY_2 = ObjectAnimator.ofFloat(privary_small_2, "scaleY", 0f, 1f, 0f);
        animatoY_2.setRepeatCount(-1);
        ObjectAnimator animatoX_3 = ObjectAnimator.ofFloat(privary_small_3, "scaleX", 0f, 1f, 0f);
        animatoX_3.setRepeatCount(-1);
        ObjectAnimator animatoY_3 = ObjectAnimator.ofFloat(privary_small_3, "scaleY", 0f, 1f, 0f);
        animatoY_3.setRepeatCount(-1);
        ObjectAnimator animatoX_4 = ObjectAnimator.ofFloat(privary_small_4, "scaleX", 0f, 1f, 0f);
        animatoX_4.setRepeatCount(-1);
        ObjectAnimator animatoY_4 = ObjectAnimator.ofFloat(privary_small_4, "scaleY", 0f, 1f, 0f);
        animatoY_4.setRepeatCount(-1);
        ObjectAnimator animatoX_5 = ObjectAnimator.ofFloat(privary_small_5, "scaleX", 0f, 1f, 0f);
        animatoX_5.setRepeatCount(-1);
        ObjectAnimator animatoY_5 = ObjectAnimator.ofFloat(privary_small_5, "scaleY", 0f, 1f, 0f);
        animatoY_5.setRepeatCount(-1);
        animatorSmall.setDuration(1500);
        animatorSmall.setInterpolator(new DecelerateInterpolator());
        animatorSmall.playTogether(animatoX_1, animatoY_1, animatoX_2, animatoY_2, animatoX_3, animatoY_3, animatoX_4, animatoY_4, animatoX_5, animatoY_5);//两个动画同时开始
        animatorSmall.start();
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
            }

        }
    };

    @Override
    protected void onDestroy() {
        animatorSmall.cancel();
        animationLine_1.cancel();
        animator.cancel();
        animationLine_2.cancel();
        super.onDestroy();
    }
}
