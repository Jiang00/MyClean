package com.supers.clean.junk.modle;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 */

public class ListViewAnimationDismiss {
    private ListView labsList;
    private CallBackDissmiss callback;

    public ListViewAnimationDismiss(ListView labsList, CallBackDissmiss callback) {
        // TODO Auto-generated constructor stub
        this.labsList = labsList;
        this.callback = callback;
    }

    /**
     * 获取listview所以item
     *
     * @return
     */
    public List<View> getVisibleViewsForPositions() {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < labsList.getChildCount(); i++) {
            View child = labsList.getChildAt(i);
            views.add(child);
            Log.e("junk", "===view" + i);
        }
        return views;
    }

    public void animateDismiss() {

        if (labsList == null) {
            throw new IllegalStateException(
                    "Call setAbsListView() on this AnimateDismissAdapter before calling setAdapter()!");
        }

        List<View> views = getVisibleViewsForPositions();
        final List<ValueAnimator> animators = new ArrayList<>();
        if (!views.isEmpty()) {
            for (View view : views) {
                animators.add(createAnimatorForView(view));
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (ValueAnimator animator : animators) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        callback.dissmiItem(animator);
                    }
                }
            }).start();
        } else {
            callback.dissmiassAll();
        }
    }

    //创建动画
    public ValueAnimator createAnimatorForView(final View view) {
        final int originalWidth = view.getWidth();
        ValueAnimator animator = ValueAnimator.ofInt(0, originalWidth);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                callback.dissmiItem();
//                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int a = (Integer) valueAnimator.getAnimatedValue();
                view.setTranslationX(a);
            }
        });
        animator.setDuration(1000);
        return animator;
    }

    public interface CallBackDissmiss {
        void dissmiItem(ValueAnimator animator);

        void dissmiItem();

        void dissmiassAll();
    }

}