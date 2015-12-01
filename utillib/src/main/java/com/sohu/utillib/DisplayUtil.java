package com.sohu.utillib;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;

/**
 * Created by jingbiaowang on 2015/8/12.
 */
public class DisplayUtil {

    /**
     * 按比例放大和缩小bitmap.
     *
     * @param bitmap
     * @param scale
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, float scale) {

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    /**
     * 获取屏幕尺寸。
     *
     * @param display
     * @param outSize
     */
    @SuppressLint("NewApi")
    public static void getSize(Display display, Point outSize) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            display.getSize(outSize);
        } else {
            outSize.x = display.getWidth();
            outSize.y = display.getHeight();
        }
    }

}
