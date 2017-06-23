package com.mutter.clean.imageclean;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static android.media.ThumbnailUtils.OPTIONS_RECYCLE_INPUT;


public class ImageHelper {
    private static final String TAG = "SIMILAR_IMAGE";

    static final int MAX_WIDTH = 1024;//最大宽
    static final int MAX_HEIGHT = 1280;//最大高


    private static final int HAMM_INSTANCE = 10;
    private static final long NEED_COMPARE_IMAGE_INTERVAL = 15 * 1000;


    /**
     * 保存Bitmap到文件
     */
    public void saveBitmap(Bitmap bmp, String filePath) throws FileNotFoundException {
        FileOutputStream outputStream = new FileOutputStream(new File(filePath));
        bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
    }

    /**
     * 创建缩略图
     */
    public Bitmap createThumbnail(Bitmap source, int width, int height) {
        return ThumbnailUtils.extractThumbnail(source, width, height, OPTIONS_RECYCLE_INPUT);
    }


    public void recyclebitmap(Bitmap thumb) {
        if (thumb != null && !thumb.isRecycled()) {
            // 回收并且置为null
            thumb.recycle();
            thumb = null;
//            System.gc();
        }
    }

    /**
     * 灰度值计算
     *
     * @param pixels 像素
     * @return int 灰度值
     */
    public static int rgbToGray(int pixels) {
        // int _alpha = (pixels >> 24) & 0xFF;
        int _red = Color.red(pixels);
        int _green = Color.green(pixels);
        int _blue = Color.blue(pixels);
//        int _red = (pixels >> 16) & 0xFF;
//        int _green = (pixels >> 8) & 0xFF;
//        int _blue = (pixels) & 0xFF;
        return (int) (0.3 * _red + 0.59 * _green + 0.11 * _blue);
    }

    /**
     * 计算数组的平均值
     *
     * @param pixels 数组
     * @return int 平均值
     */
    public static int average(int[] pixels) {
        float m = 0;
        for (int i = 0; i < pixels.length; ++i) {
            m += pixels[i];
        }
        m = m / pixels.length;
        return (int) m;
    }

    /**
     * 二进制转为十六进制
     *
     * @param binary
     * @return char hex
     */
    private static char binaryToHex(int binary) {
        char ch = ' ';
        switch (binary) {
            case 0:
                ch = '0';
                break;
            case 1:
                ch = '1';
                break;
            case 2:
                ch = '2';
                break;
            case 3:
                ch = '3';
                break;
            case 4:
                ch = '4';
                break;
            case 5:
                ch = '5';
                break;
            case 6:
                ch = '6';
                break;
            case 7:
                ch = '7';
                break;
            case 8:
                ch = '8';
                break;
            case 9:
                ch = '9';
                break;
            case 10:
                ch = 'a';
                break;
            case 11:
                ch = 'b';
                break;
            case 12:
                ch = 'c';
                break;
            case 13:
                ch = 'd';
                break;
            case 14:
                ch = 'e';
                break;
            case 15:
                ch = 'f';
                break;
            default:
                ch = ' ';
        }
        return ch;
    }


    /**
     * 计算"汉明距离"（Hamming distance）。 如果不相同的数据位不超过5，就说明两张图片很相似；如果大于10，就说明这是两张不同的图片。
     *
     * @param sourceHashCode 源hashCode
     * @param hashCode       与之比较的hashCode
     */
    public boolean hammingDistance(String sourceHashCode, String hashCode) {
        if (TextUtils.isEmpty(sourceHashCode) || TextUtils.isEmpty(hashCode)) {
            return false;
        }
        int difference = 0;
        int len = sourceHashCode.length();

        for (int i = 0; i < len; i++) {
            if (sourceHashCode.charAt(i) != hashCode.charAt(i)) {
                difference++;
                if (difference > HAMM_INSTANCE) {
                    return false;
                }
            }
        }

        return difference <= HAMM_INSTANCE;
    }

    /*public static LocalImage CreateLocalImage(Bitmap source, String path, long size, String uri) {
        LocalImage li = new LocalImage(null, path, size, uri);
        int width = 8;
        int height = 8;
        // 第一步，缩小尺寸。
        // 将图片缩小到8x8的尺寸，总共64个像素。这一步的作用是去除图片的细节，只保留结构、明暗等基本信息，摒弃不同尺寸、比例带来的图片差异。
        Bitmap thumb = ImageHelper.createThumbnail(source, width, height);
        //Bitmap thumb = Bitmap.createScaledBitmap(source, width, height, false);
        // 第二步，简化色彩。
        // 将缩小后的图片，转为64级灰度。也就是说，所有像素点总共只有64种颜色。
        int[] pixels = new int[width * height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i * height + j] = ImageHelper.rgbToGray(thumb.getPixel(i, j));
            }
        }

        // 第三步，计算平均值。
        // 计算所有64个像素的灰度平均值。
        int avgPixel = ImageHelper.average(pixels);
        Log.i(TAG, "平均灰度值为:" + String.valueOf(avgPixel));
        // 第四步，比较像素的灰度。
        // 将每个像素的灰度，与平均值进行比较。大于或等于平均值，记为1；小于平均值，记为0。
        int[] comps = new int[width * height];
        for (int i = 0; i < comps.length; i++) {
            if (pixels[i] >= avgPixel) {
                comps[i] = 1;
            } else {
                comps[i] = 0;
            }
        }

        // 第五步，计算哈希值。
        // 将上一步的比较结果，组合在一起，就构成了一个64位的整数，这就是这张图片的指纹。组合的次序并不重要，只要保证所有图片都采用同样次序就行了。
        StringBuffer hashCode = new StringBuffer();
        for (int i = 0; i < comps.length; i += 4) {
            int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1]
                    * (int) Math.pow(2, 2) + comps[i + 2]
                    * (int) Math.pow(2, 1) + comps[i + 3];
            hashCode.append(binaryToHex(result));
        }
        recyclebitmap(thumb);
        recyclebitmap(source);
        System.gc();
        li.setSourceHashCode(hashCode.toString());
        li.setAvgPixel(avgPixel);
        return li;
    }*/
    public Bitmap pathWithScaledBitmap(Context context, String path) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        Bitmap bitmap = loadBitmapFromFile(path, dm.widthPixels,
                dm.heightPixels);
        if (bitmap == null) {
            return null;
        }
        return bitmap;
    }

    public Bitmap pathWithScaledBitmap(Context context, String path, int screenWidth, int screenHeight) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        Bitmap bitmap = loadBitmapFromFile(path, dm.widthPixels,
                dm.heightPixels);
        if (bitmap == null) {
            return null;
        }
        Bitmap thumb = Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, false);
        recyclebitmap(bitmap);
        System.gc();
        return thumb;
    }


    private Bitmap loadBitmapFromFile(String path, int screenWidth, int screenHeight) {
        // 不能超过最大高与最大宽，避免尺寸太大而OOM

        final int width = Math.min(screenWidth, MAX_WIDTH);
        final int height = Math.min(screenHeight, MAX_HEIGHT);
        if (width <= 0 || height <= 0) {
            return null;
        }

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        if (options.outHeight == -1 || options.outWidth == -1) {
            return null;
        }

        // Calculate inSampleSize，尽量接近width与height
        int inSampleSize = Math.max((int) (0.5f + (float) options.outHeight / (float) height),
                (int) (0.5f + (float) options.outWidth / (float) width));
        inSampleSize += 1; // 强制 +1，防止在某些手机上的OOM
        options.inSampleSize = Math.max(inSampleSize, 1);


        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        // DO NOT ZOOM HERE
        // caller must zoom the bitmap to expected size, it might has its own
        // aspect / size requirement
        return BitmapFactory.decodeFile(path, options);
    }

    public long getThumbnailId(Context context, String imagePath) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = {MediaStore.Images.Media._ID};
        String whereClause = MediaStore.Images.Media.DATA + " = '" + imagePath + "'";
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, whereClause, null, null);
        int _id = 0;
        if (cursor == null || cursor.getCount() == 0) {
            return -1;
        } else if (cursor.moveToFirst()) {
            int _idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            do {
                _id = cursor.getInt(_idColumn);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return _id;
    }


    public Bitmap getImageThumbnail(Context context, String imagePath, int kind) {
        long id = getThumbnailId(context, imagePath);
        if (id == -1) {
            return null;
        }
        return getImageThumbnail(context, id, kind);
    }

    public Bitmap getImageThumbnail(Context context, long thumbnailId, int kind) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), thumbnailId, kind, options);

        return bitmap;
    }

    public interface OnQuerySimilarPicCallBack {
        void startQuery();

        void endQuery(ArrayList<ImageInfo> localImageList, ArrayList<ArrayList<ImageInfo>> localImages, long totalSize, long totalCount);

        void startAsyncPic(int i, ArrayList<ImageInfo> localImageList);

        void endAsyncPic(int i, ArrayList<ImageInfo> localImageList);

        void haveQuerySimilarPic(int i, ArrayList<ImageInfo> similarImage, ArrayList<ArrayList<ImageInfo>> totalSimilarImage, long totalSize);
    }

    public ArrayList<ArrayList<ImageInfo>> querySimilarImage(Context context, OnQuerySimilarPicCallBack onQueryCallBack) {
        if (onQueryCallBack != null) {
            onQueryCallBack.startQuery();
        }
        ArrayList<ArrayList<ImageInfo>> similarItems = new ArrayList<>();

        //ArrayList<ImageInfo> localImageList = getCameraImageList();

        ArrayList<ImageInfo> localImageList = queryCameraPhoto(context);

        int size = localImageList.size();

        if (size < 2) {
            if (onQueryCallBack != null) {
                onQueryCallBack.endQuery(localImageList, similarItems, 0, 0);
            }
            return similarItems;
        }

        long totalSize = 0;
        long totalCount = 0;

        ArrayList<ImageInfo> similarItem = new ArrayList<>();

        for (int i = 0; i < size - 1; i++) {
            if (onQueryCallBack != null) {
                onQueryCallBack.startAsyncPic(i, localImageList);
            }

            ImageInfo first = localImageList.get(i);
            ImageInfo second = localImageList.get(i + 1);

            if (isSimilarImage(context, first, second)) {
                if (!similarItem.contains(first)) {
                    similarItem.add(first);
                    totalSize += first.fileSize;
                    totalCount++;
                }
                if (!similarItem.contains(second)) {
                    similarItem.add(second);
                    totalSize += second.fileSize;
                    totalCount++;
                }

                if (!similarItems.contains(similarItem)) {
                    if (similarItem.size() > 1) {
                        similarItem.get(getBestImageIndex(similarItem)).isNormal = true;
                    }
                    similarItems.add(0, similarItem);
                }

                if (onQueryCallBack != null) {
                    onQueryCallBack.haveQuerySimilarPic(i, localImageList, similarItems, totalSize);
                }

            } else {
                similarItem = new ArrayList<>();
            }

            System.gc();

            if (onQueryCallBack != null) {
                onQueryCallBack.endAsyncPic(i, localImageList);
            }
        }
        if (onQueryCallBack != null) {
            onQueryCallBack.endQuery(localImageList, similarItems, totalSize, totalCount);
        }
        return similarItems;

    }

   /* public boolean similarCondition(LocalImage first, LocalImage second) {
        int hammingDistance = hammingDistance(first.getSourceHashCode(), second.getSourceHashCode());
        // double avgPixsProportion = ((double) first.getAvgPixel()) / second.getAvgPixel();
        // boolean avgPixCondition = avgPixsProportion < 1.2 && avgPixsProportion > 0.8;
        // avgPixCondition = true;
        //Log.e("rqy", "hammingDistance=" + hammingDistance + ",avgPixsProportion=" + avgPixsProportion);
        return hammingDistance < 10*//* && avgPixCondition*//*;
    }*/

    public ArrayList<ImageInfo> queryCameraPhoto(Context context) {
        long startTime = System.currentTimeMillis();
        ArrayList<ImageInfo> localImages = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] columns = new String[]{
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.SIZE, MediaStore.MediaColumns.DATE_MODIFIED
        };

        Cursor cursor = context.getContentResolver().query(uri, columns,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/jpg"}, MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                Log.e(TAG, "路径:" + path);
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE));
                int _idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                long id = cursor.getInt(_idColumn);
                String name = new File(path).getName();
                ImageInfo imageInfo = new ImageInfo(path, id, name, size);
                if (imageInfo.tag_datetime != null) {
                    localImages.add(imageInfo);
                }
            }
        }
        Collections.sort(localImages, new ImageInfo.ImageComparator());
        long endTime = System.currentTimeMillis();
        Log.e("rqy", "queryCameraPhoto time:" + (endTime - startTime));
        return localImages;
    }

    public ArrayList<ImageInfo> getCameraImageList() {
        ArrayList<ImageInfo> mList = new ArrayList<>();
        String url = Environment.getExternalStorageDirectory().toString() + "/DCIM";
        File file = new File(url);
        getFileList(file, mList);

        Collections.sort(mList, new ImageInfo.ImageComparator());

        return mList;
    }

    private void getFileList(File file, ArrayList<ImageInfo> imageInfos) {
        if (file.isDirectory()) {
            File[] files = file.listFiles(new ImageInfo.ImageFilter());
            for (File file1 : files) {
                getFileList(file1, imageInfos);
            }
        } else {
            String filePath = file.getPath();
            //文件名
            String fileName = file.getName();
            //添加
            // imageInfos.add(new ImageInfo(filePath, fileName, file.length()));
        }
    }

    public boolean isSimilarImage(Context context, ImageInfo imageInfo1, ImageInfo imageInfo2) {
        if (!TextUtils.equals(imageInfo1.height, imageInfo2.height) || !TextUtils.equals(imageInfo1.width, imageInfo2.width)) {
            return false;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        try {
            Date data1 = formatter.parse(imageInfo1.tag_datetime);
            Date date2 = formatter.parse(imageInfo2.tag_datetime);
            long interval = data1.getTime() - date2.getTime();
            if (interval > NEED_COMPARE_IMAGE_INTERVAL || interval < -1 * NEED_COMPARE_IMAGE_INTERVAL) {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        setImageHashCode(context, imageInfo1);
        setImageHashCode(context, imageInfo2);

//        double avgPixsProportion = ((double) imageInfo1.avgPixel) / imageInfo2.avgPixel;
//        boolean avgPixCondition = avgPixsProportion < 1.2 && avgPixsProportion > 0.8;

        if (hammingDistance(imageInfo1.sourceHashCode, imageInfo2.sourceHashCode)) {
            return true;
        }

        return false;
    }


    public void setImageHashCode(Context context, ImageInfo imageInfo) {
        if (imageInfo.sourceHashCode != null) {
            return;
        }
        /*DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        Bitmap bitmap = loadBitmapFromFile(imageInfo.path, dm.widthPixels,
                dm.heightPixels);*/
        Bitmap bitmap = getImageThumbnail(context, imageInfo.originId, MediaStore.Images.Thumbnails.MINI_KIND);
        if (bitmap == null) {
            return;
        }
        // 第一步，缩小尺寸。
        // 将图片缩小到8x8的尺寸，总共64个像素。这一步的作用是去除图片的细节，只保留结构、明暗等基本信息，摒弃不同尺寸、比例带来的图片差异。
        int width = 8;
        int height = 8;
        Bitmap thumb = createThumbnail(bitmap, width, height);
        //Bitmap thumb = Bitmap.createScaledBitmap(source, width, height, false);
        // 第二步，简化色彩。
        // 将缩小后的图片，转为64级灰度。也就是说，所有像素点总共只有64种颜色。
        int[] pixels = new int[width * height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i * height + j] = ImageHelper.rgbToGray(thumb.getPixel(i, j));
            }
        }

        // 第三步，计算平均值。
        // 计算所有64个像素的灰度平均值。
        int avgPixel = ImageHelper.average(pixels);
        // 第四步，比较像素的灰度。
        // 将每个像素的灰度，与平均值进行比较。大于或等于平均值，记为1；小于平均值，记为0。
        int[] comps = new int[width * height];
        for (int i = 0; i < comps.length; i++) {
            if (pixels[i] >= avgPixel) {
                comps[i] = 1;
            } else {
                comps[i] = 0;
            }
        }

        // 第五步，计算哈希值。
        // 将上一步的比较结果，组合在一起，就构成了一个64位的整数，这就是这张图片的指纹。组合的次序并不重要，只要保证所有图片都采用同样次序就行了。
        StringBuffer hashCode = new StringBuffer();
        for (int i = 0; i < comps.length; i += 4) {
            int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1]
                    * (int) Math.pow(2, 2) + comps[i + 2]
                    * (int) Math.pow(2, 1) + comps[i + 3];
            hashCode.append(binaryToHex(result));
        }
        recyclebitmap(thumb);
        recyclebitmap(bitmap);
        System.gc();
        imageInfo.sourceHashCode = hashCode.toString();
        imageInfo.avgPixel = avgPixel;
    }

    /**
     * @param imageInfos
     * @return best image index
     */
    public int getBestImageIndex(ArrayList<ImageInfo> imageInfos) {
        int bestImageIndex = 0;
        if (imageInfos == null || imageInfos.isEmpty()) {
            Log.e("rqy", "not should happen");
            return bestImageIndex;
        }
        int bestAvgPixel = imageInfos.get(0).avgPixel;
        for (int i = 1; i < imageInfos.size(); i++) {
            int avgPixel = imageInfos.get(i).avgPixel;
            if (avgPixel > bestAvgPixel) {
                bestImageIndex = i;
                bestAvgPixel = avgPixel;
            }
        }
        return bestImageIndex;
    }

    public long getImageGroupSize(ArrayList<ImageInfo> imageInfos) {
        long size = 0;
        if (imageInfos == null || imageInfos.isEmpty()) {
            Log.e("rqy", "not should happen");
            return size;
        }
        for (int i = 0; i < imageInfos.size(); i++) {
            size += imageInfos.get(i).fileSize;
        }
        return size;
    }


}

