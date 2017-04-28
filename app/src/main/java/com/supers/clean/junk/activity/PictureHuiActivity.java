package com.supers.clean.junk.activity;

import android.os.Bundle;
import android.util.Log;

import com.supers.clean.junk.R;
import com.supers.clean.junk.db.RecyclerDbHelper;
import com.supers.clean.junk.similarimage.ImageInfo;

import java.util.ArrayList;

/**
 * Created by Ivy on 2017/4/28.
 */

public class PictureHuiActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_picture_hui);
        ArrayList<ImageInfo> imageInfos = RecyclerDbHelper.getInstance(this).getRecyclerImageList();
        for (ImageInfo imageInfo : imageInfos) {
            Log.e("rqy", "imageInfos=" + imageInfo.restoreFilePath + "--" + imageInfo.backFilePath + "--" + imageInfo.rowId);
            RecyclerDbHelper.getInstance(this).restoreImageFromRecycler(imageInfo);
        }


    }

    @Override
    protected void findId() {
        super.findId();
    }
}
