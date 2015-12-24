package com.thinkcool.efficientloadingpicture.Utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by thinkcool on 2015/12/11.
 */
public class BitmapUtils {

    public static int getFitInSampleSize(int reqWidth, int reqHeight, BitmapFactory.Options options) {
        int inSampleSize = 1;
        if (options.outWidth > reqWidth || options.outHeight > reqHeight) {
            int widthRatio = Math.round((float) options.outWidth / (float) reqWidth);
            int heightRatio = Math.round((float) options.outHeight / (float) reqHeight);
            inSampleSize = Math.min(widthRatio, heightRatio);
        }
        return inSampleSize;
    }
    public static Bitmap getFitSampleBitmap(String file_path, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file_path, options);
        options.inSampleSize = getFitInSampleSize(width, height, options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file_path, options);
    }

    public static Bitmap getFitSampleBitmap(Resources resources, int resId, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = getFitInSampleSize(width, height, options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    public static Bitmap getFitSampleBitmap(InputStream inputStream, String catchFilePath,int width, int height) throws Exception {
        return getFitSampleBitmap(catchStreamToFile(catchFilePath, inputStream), width, height);
    }
    /*
       * 将inputStream中字节流保存至文件
       * */
    public static String catchStreamToFile(String catchFile,InputStream inStream) throws Exception {

        File tempFile=new File(catchFile);
        try {
            if (tempFile.exists()) {
                tempFile.delete();
            }
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fileOutputStream=new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
        }
        inStream.close();
        fileOutputStream.close();
        return catchFile;
    }

//    public static Bitmap getFitSampleBitmap(InputStream inputStream, int width, int height) throws Exception {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        byte[] bytes = readStream(inputStream);
//        //BitmapFactory.decodeStream(inputStream, null, options);
//        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
//        options.inSampleSize = getFitInSampleSize(width, height, options);
//        options.inJustDecodeBounds = false;
////        return BitmapFactory.decodeStream(inputStream, null, options);
//        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
//    }
//    /*
//     * 从inputStream中获取字节流 数组大小
//	 * */
//    public static byte[] readStream(InputStream inStream) throws Exception {
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int len = 0;
//        while ((len = inStream.read(buffer)) != -1) {
//            outStream.write(buffer, 0, len);
//        }
//        outStream.close();
//        inStream.close();
//        return outStream.toByteArray();
//    }

}
