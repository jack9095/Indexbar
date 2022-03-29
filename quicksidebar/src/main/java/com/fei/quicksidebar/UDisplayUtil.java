package com.fei.quicksidebar;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Pair;

import androidx.annotation.NonNull;

public class UDisplayUtil {
    private static final float SCREEN_SIZE_OFFSET= 0.5f;

    /**
     * Screen size pixel convert to dp
     *
     * @param context the context
     * @param pxValue size in pixel
     * @return the size in pixels
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + SCREEN_SIZE_OFFSET);
    }

    /**
     * Screen size dp convert to pixel
     *
     * @param context the context
     * @param dpValue size in dp
     * @return the size in pixels
     */
    public static int dp2Px(@NonNull Context context, int dpValue) {
        float density = getDensity(context);
        return (int) (dpValue * density + SCREEN_SIZE_OFFSET);
    }

    /**
     * Get screen size in pixels
     *
     * @param context the context
     * @return Pair first:width, second:height
     */
    public static Pair<Integer, Integer> getScreenSize(@NonNull Context context) {
        DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
        return new Pair<>(metrics.widthPixels, metrics.heightPixels);
    }

    /**
     * Get screen density
     *
     * @param context the context
     * @return the density
     */
    public static float getDensity(@NonNull Context context) {
        return context.getApplicationContext().getResources().getDisplayMetrics().density;
    }

    /**
     *
     * @param context
     * @return
     */
    public static Pair<Integer, Integer> getScreenSizeInDp(@NonNull Context context) {
        Pair<Integer, Integer> pair = getScreenSize(context);
        int width = pair.first;
        int height = pair.second;
        float density = getDensity(context);
        int widthInDp = (int) (width / density);
        int heightInDp = (int) (height / density);
        return new Pair<>(widthInDp, heightInDp);
    }

    /**
     * Screen size dp convert to pixel
     *
     * @param context the context
     * @param pxValue size in sp
     * @return the size in pixels
     */
    public static int px2sp(@NonNull Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + SCREEN_SIZE_OFFSET);
    }
}
