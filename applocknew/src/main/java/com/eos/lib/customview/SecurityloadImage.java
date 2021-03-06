package com.eos.lib.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.privacy.lock.R;
import com.eos.manager.App;
import com.eos.manager.lib.io.ImageMaster;
import com.eos.manager.lib.io.LoadIconFromApp;
import com.eos.manager.lib.io.LoadNormalThumbnail;

/**
 * Created by SongHualin on 6/12/2015.
 */
public class SecurityloadImage extends ImageView
        implements LoadIconFromApp.LoadingNotifiable{
    Animation animation;
    String url;
    int fileType;
    long id;

    public SecurityloadImage(Context context) {
        super(context);
        loadAnimation();
    }

    public SecurityloadImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAnimation();
    }

    public SecurityloadImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAnimation();
    }

    private void loadAnimation() {
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_in);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SecurityloadImage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadAnimation();
    }

    public void setImage(String url, long id, int fileType, boolean forceLoading) {
        this.id = id;
        this.fileType = fileType;
        this.url = url;
        if (url == null) {
            setImageDrawable(null);
        } else {
            Bitmap bitmap = ImageMaster.getImage(url);

//              setImageBitmap(Bitmap.createBitmap(urlDrawable.getBitmap(),10,100,100,500));
//            BitmapDrawable bitmapDrawable=(BitmapDrawable)imageview1.getDrawable();
//            imageview2.setImageBitmap(Bitmap.createBitmap(bitmapDrawable.getBitmap(),x,y,width,height));

            setImageBitmap(bitmap);
            if (bitmap == null && forceLoading) {
                LoadNormalThumbnail loadingTask = LoadNormalThumbnail.Instance();
                loadingTask.execute(this);
            }
        }
    }



    public void setImageIcon(String packageName, boolean forceLoading) {
        this.url = packageName;
        Bitmap bitmap = ImageMaster.getImage(packageName);
        setImageBitmap(bitmap);
        if (bitmap == null && forceLoading) {
            LoadIconFromApp task = LoadIconFromApp.Instance();
            task.execute(this);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Point getSize() {
        return new Point(114, 96);
    }

    @Override
    public int getFileType() {
        return fileType;
    }

    @Override
    public void offer(final Bitmap bitmap) {
        App.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {
                    if (!bitmap.isRecycled()) {
                        setImageBitmap(bitmap);
                    }
                }
                startAnimation(animation);
            }
        });
    }

    @Override
    public long getIdLong() {
        return id;
    }
}
