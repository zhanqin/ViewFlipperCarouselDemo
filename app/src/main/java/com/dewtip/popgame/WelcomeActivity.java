package com.dewtip.popgame;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import java.sql.Timestamp;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by qq on 2017/7/9.
 */

public class WelcomeActivity extends AppCompatActivity{

    private Context mContext;
    private ViewFlipper ViewFlipperWelcome;
    private int[] resId = {R.drawable.img_welcome_1,R.drawable.img_welcome_2,R.drawable.img_welcome_3,R.drawable.img_welcome_4};
    private final static int MIN_MOVE = 100;//手势滑动时最小距离
    private MyGestureListener MyGesture;
    private GestureDetector mDetector;
    private Timestamp OldGestureTime;//存放最后一次用手势操作的时间
    private Timestamp NewGestureTime;//存放当前手势操作的时间

    //延时
    private Handler h = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ViewFlipperWelcome.showNext();
            ViewFlipperWelcome.startFlipping();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mContext = WelcomeActivity.this;
        MyGesture = new MyGestureListener();
        mDetector = new GestureDetector(this,MyGesture);
        ViewFlipperWelcome = (ViewFlipper)findViewById(R.id.ViewFlipperWelcome);
        for (int i = 0; i < resId.length;i ++){
            ViewFlipperWelcome.addView(getImageView(resId[i]));
        }
        //添加动画
        ViewFlipperWelcome.setInAnimation(mContext,R.anim.right_in);
        ViewFlipperWelcome.setOutAnimation(mContext,R.anim.right_out);
        //开始自动滑动
        ViewFlipperWelcome.startFlipping();
    }

    //重写onTouchEvent触发MyGestureListener里的方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    //自定义一个滑动的手势
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        public boolean onFling(MotionEvent e1,MotionEvent e2,float v,float v1){
            if (e1.getX() - e2.getX() > MIN_MOVE){
                //向右滑动，先停止ViewFlipper的自动滑动
                ViewFlipperWelcome.stopFlipping();
                ViewFlipperWelcome.setInAnimation(mContext,R.anim.right_in);
                ViewFlipperWelcome.setOutAnimation(mContext,R.anim.right_out);
                ViewFlipperWelcome.showNext();
                handleMyGestureListener();
            }else if(e2.getX() - e1.getX() > MIN_MOVE){
                //向左滑动，先停止ViewFlipper的自动滑动
                ViewFlipperWelcome.stopFlipping();
                ViewFlipperWelcome.setInAnimation(mContext,R.anim.left_in);
                ViewFlipperWelcome.setOutAnimation(mContext,R.anim.left_out);
                ViewFlipperWelcome.showNext();
                handleMyGestureListener();
            }
            return true;
        }
    }

    private ImageView getImageView(int resId){
        ImageView img = new ImageView(this);
        img.setBackgroundResource(resId);
        return img;
    }

    //由于延迟操作没终止，如果连续用手势滑动，不加判断的调用延迟操作，那么后面的自动滑动会出问题，故在这里算出两个手势操作的时间差，然后看是否调用延迟操作
    private void handleMyGestureListener(){

        OldGestureTime = NewGestureTime;
        NewGestureTime = new Timestamp(System.currentTimeMillis());
        //第一次OldGestureTime为空，若不加此判断，直接调用OldGestureTime.getTime()，会报空指针错误
        if (OldGestureTime == null){
            h.sendEmptyMessageDelayed(0, 5500);
            return;
        }
        //如果两个手势之间的时间差大于延迟操作延时的时间，则调用延时操作
        if (NewGestureTime.getTime() - OldGestureTime.getTime() > 5500) {
            h.sendEmptyMessageDelayed(0, 5500);
        }
    }

}
