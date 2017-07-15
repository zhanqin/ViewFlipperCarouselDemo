package com.dewtip.popgame;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dewtip.popgame.R;

import static android.R.attr.start;

/**
 * Created by qq on 2017/7/8.
 */

//启动页面
public class LaunchActivity extends AppCompatActivity {

    private Handler h = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(LaunchActivity.this, WelcomeActivity.class));
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动页面
        setContentView(R.layout.activity_launch);
        //启动页显示3秒后跳转到引导页
        h.sendEmptyMessageDelayed(0, 1000);
    }

}
