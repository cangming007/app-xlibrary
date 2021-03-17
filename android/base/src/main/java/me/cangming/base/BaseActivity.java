package me.cangming.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    // 状态栏设置的view
    protected View statusBarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setStatusBar();
    }

    /**
     * 手动设置状态栏  <br/>
     * 如果默认白色，不用重写  <br/>
     * 如果要设置不同颜色状态栏，重写改方法，并return true  <br/>
     * 如果设置透明状态栏，就是顶部布局延伸到状态栏，重写该方法，return false
     *
     * @return 是否设置了状态栏，不是透明状态栏
     */
    protected boolean setStatusBar() {
        setStatusBar(LiveStatusBarTool.getThemeColor());
        return true;
    }

    protected void setStatusBar(@ColorInt int color) {
        if (statusBarView == null) {
            statusBarView = LiveStatusBarTool.setColor(this, color);
        } else {
            statusBarView.setBackgroundColor(color);
        }
    }

    protected void setPrimaryStatusBar() {
        setStatusBar(LiveStatusBarTool.getPrimaryColor(this));
    }

    protected void setStatusTranslucent() {
        LiveStatusBarTool.setTranslucent(this, statusBarView);
    }

}
