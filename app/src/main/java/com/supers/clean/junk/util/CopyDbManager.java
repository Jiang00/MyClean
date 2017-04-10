package com.supers.clean.junk.util;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class CopyDbManager {
    public static void copyDB(Context context, String fileName) {
        String path = Environment.getDataDirectory() + "/data/"
                + context.getPackageName() + "/"
                + fileName;
//		String path = context.getFilesDir()
//				+ fileName;
        try {
//			InputStream is = context.getAssets().open(fileName);
            InputStream is = context.getClass().getClassLoader().getResourceAsStream("assets/" + fileName);
            BufferedInputStream bis = new BufferedInputStream(is);
            File file = new File(path);
            if (file.exists()) {
                return;
            }

            OutputStream os = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(os);

            byte[] bytes = new byte[5 * 1024];
            int length = 0;
            while ((length = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, length);

            }
            bos.flush();
            bos.close();
            bis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

    }
}
