package me.cangming.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;


/**
 * 1.状态栏设置背景颜色；<br/>
 * 2.背景是图片，设置状态栏透明，图片伸到状态栏 <br/>
 * Created by fuq on 2018/8/20
 */
public class LiveStatusBarTool {

    private static int statusBarHeight;

    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置的activity
     * @param color    状态栏要设置的颜色值
     */
    public static View setColor(@NonNull Activity activity, @ColorInt int color) {
        View statusView = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0及以上
            setStatusBar(activity, color);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 4.4-5.0
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            statusView = createStatusView(activity, color);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity
                    .findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
        return statusView;
    }

    /**
     * 生成一个和状态栏大小相同的矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @return 状态栏 view
     */
    private static View createStatusView(@NonNull Activity activity, int color) {
        int statusBarHeight = getStatusBarHeight(activity);

        View statusView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(@NonNull Activity activity) {
        if (statusBarHeight <= 0) {
            int resourceId = activity.getResources().getIdentifier("status_bar_height",
                    "dimen", "android");
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 如果需要titlebar 延伸到状态栏，需要设置状态栏透明
     * 设置状态栏透明，4.4~5.0，（增加过状态栏view，将先移除view）
     *
     * @param activity   activity
     * @param statusView 之前增加的状态栏view
     */
    public static void setTranslucent(@NonNull Activity activity, @Nullable View statusView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // 4.4~5.0 设置状态栏透明，先移除之前的statusView
            if (statusView != null) {
                ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
                decorView.removeView(statusView);
            }
        }
        setTranslucentLollipop(activity);
    }

    /**
     * 使状态栏透明 <br/>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity 需要设置的activity
     */
    public static void setTranslucentLollipop(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 5.0及以上
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity
                    .findViewById(android.R.id.content)).getChildAt(0);
            // false在状态栏绘制，true不在状态栏绘制
            rootView.setFitsSystemWindows(false);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 直接设置状态栏透明，将顶部view延伸到状态栏 <br/>
     * 将顶部view paddingTop状态栏高度  <br/>
     * 顶部view需要有颜色背景
     *
     * @param topView 界面顶部view
     */
    public static void setStatusTranslucent(@NonNull Activity activity, @NonNull View topView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity
                    .findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(false);
            rootView.setClipToPadding(true);
            int resourceId = activity.getResources().getIdentifier("status_bar_height",
                    "dimen", "android");
            int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
            topView.setPadding(0, statusBarHeight, 0, 0);
        }
    }


    /**
     * Android 6.0 以上设置状态栏颜色
     */
    private static void setStatusBar(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(color);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 如果亮色，设置状态栏文字为黑色
            if (isLightColor(color)) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }

    }

    /**
     * 判断颜色是不是亮色
     *
     * @param color
     * @return
     */
    public static boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

    @ColorInt
    public static int getThemeColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    @ColorInt
    public static int getPrimaryColor(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            return context.getColor(R.color.colorPrimary);
        } else {
            return Color.BLACK;
        }
    }

}
