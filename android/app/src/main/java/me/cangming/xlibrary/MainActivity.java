package me.cangming.xlibrary;

import android.os.Bundle;

import me.cangming.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBar(getResources().getColor(R.color.common_bg_color));
    }
}