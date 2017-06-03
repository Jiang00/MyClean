package com.sample.lottie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;

class ImageAssetBitmapManager {
  private final Context context;
  private String imagesFolder;
  @Nullable private ImageAssetDelegate assetDelegate;
  private final Map<String, LottieImageAsset> imageAssets;
  private final Map<String, Bitmap> bitmaps = new HashMap<>();

  private boolean isTheme = false;
  private boolean isSdCard = false;
  private Context themeContext;

  ImageAssetBitmapManager(Context themeContext, Drawable.Callback callback, String imagesFolder,
                          ImageAssetDelegate assetDelegate, Map<String, LottieImageAsset> imageAssets) {
    assertNotNull(callback);

    this.themeContext = themeContext;

    if (imagesFolder.startsWith("sdCard://")) {
      isTheme = false;
      isSdCard = true;
      this.imagesFolder = imagesFolder.replace("sdCard://", "");
    } else if (imagesFolder.startsWith("theme://")) {
      isTheme = true;
      isSdCard = false;
      this.imagesFolder = imagesFolder.replace("theme://", "");
    } else {
      isTheme = false;
      isSdCard = false;
      this.imagesFolder = imagesFolder;
    }

    if (!TextUtils.isEmpty(this.imagesFolder) &&
            this.imagesFolder.charAt(this.imagesFolder.length() - 1) != '/') {
      this.imagesFolder += '/';
    }

    if (!(callback instanceof View)) {
      Log.w(L.TAG, "LottieDrawable must be inside of a view for images to work.");
      this.imageAssets = new HashMap<>();
      context = null;
      return;
    }

    context = ((View) callback).getContext();
    this.imageAssets = imageAssets;
    setAssetDelegate(assetDelegate);
  }

  void setAssetDelegate(@Nullable ImageAssetDelegate assetDelegate) {
    this.assetDelegate = assetDelegate;
  }

  Bitmap bitmapForId(String id) {
    Bitmap bitmap = bitmaps.get(id);
    if (isSdCard) {
      if (bitmap == null) {
        File parent = new File(this.imagesFolder);
        if (!parent.exists()) {
          return null;
        }

        File child;
        if (id.startsWith("image_")) {
          String name = id.replace("image_", "img_");
          child = new File(this.imagesFolder + name + ".png");
        } else if (id.startsWith("img_")) {
          child = new File(this.imagesFolder + id + ".png");
        } else {
          return null;
        }
        if (!child.exists()) {
          return null;
        }
        try {
          InputStream input = new FileInputStream(child);
          BitmapFactory.Options opts = new BitmapFactory.Options();
          opts.inScaled = false;
          opts.inDensity = 160;
          bitmap = BitmapFactory.decodeStream(input, null, opts);
          bitmaps.put(id, bitmap);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } else if (isTheme) {
      if (bitmap == null) {
        LottieImageAsset imageAsset = imageAssets.get(id);
        if (imageAsset == null) {
          return null;
        }
        if (assetDelegate != null) {
          bitmap = assetDelegate.fetchBitmap(imageAsset);
          bitmaps.put(id, bitmap);
          return bitmap;
        }
        InputStream input;
        try {
          if (TextUtils.isEmpty(imagesFolder) || themeContext == null) {
            throw new IllegalStateException("You must set an images folder before loading an image." +
                    " Set it with LottieComposition#setImagesFolder or LottieDrawable#setImagesFolder");
          }
          input = themeContext.getAssets().open(imagesFolder + imageAsset.getFileName());
          BitmapFactory.Options opts = new BitmapFactory.Options();
          opts.inScaled = false;
          opts.inDensity = 160;
          bitmap = BitmapFactory.decodeStream(input, null, opts);
          bitmaps.put(id, bitmap);
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      }
    } else {
      if (bitmap == null) {
        LottieImageAsset imageAsset = imageAssets.get(id);
        if (imageAsset == null) {
          return null;
        }
        if (assetDelegate != null) {
          bitmap = assetDelegate.fetchBitmap(imageAsset);
          bitmaps.put(id, bitmap);
          return bitmap;
        }

        InputStream is;
        try {
          if (TextUtils.isEmpty(imagesFolder)) {
            throw new IllegalStateException("You must set an images folder before loading an image." +
                    " Set it with LottieComposition#setImagesFolder or LottieDrawable#setImagesFolder");
          }
          is = context.getAssets().open(imagesFolder + imageAsset.getFileName());
          BitmapFactory.Options opts = new BitmapFactory.Options();
          opts.inScaled = false;
          opts.inDensity = 160;
          bitmap = BitmapFactory.decodeStream(is, null, opts);
          bitmaps.put(id, bitmap);
        } catch (Exception e) {
          Log.w(L.TAG, "Unable to open asset.", e);
          return null;
        }
      }
    }
    return bitmap;
  }

  void recycleBitmaps() {
    Iterator<Map.Entry<String, Bitmap>> it = bitmaps.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, Bitmap> entry = it.next();
      entry.getValue().recycle();
      it.remove();
    }
  }

  boolean hasSameContext(Context context) {
    return context == null && this.context == null ||
            context != null && this.context.equals(context);
  }
}
