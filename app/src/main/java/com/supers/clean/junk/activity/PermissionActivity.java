package com.supers.clean.junk.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.supers.clean.junk.R;

/**
 * Created by Ivy on 2017/5/19.
 */

public class PermissionActivity extends Activity {
    private ImageView check_jurisdiction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_permission);
        check_jurisdiction = (ImageView) findViewById(R.id.check_jurisdiction);
        this.findViewById(R.id.onclick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ObjectAnimator animator = ObjectAnimator.ofFloat(check_jurisdiction, "alpha", 1f, 0.5f,1f);
        animator.setDuration(1000);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                check_jurisdiction.setImageResource(R.mipmap.side_check_passed);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.side_check_passed);
                            check_jurisdiction.setImageBitmap(gameStatusBitmap);
//                            check_jurisdiction.setImageResource(R.mipmap.side_check_passed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();*/
    }
}
