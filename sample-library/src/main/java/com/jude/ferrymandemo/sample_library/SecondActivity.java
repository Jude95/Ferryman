package com.jude.ferrymandemo.sample_library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jude.ferryman.Ferryman;
import com.jude.ferryman.annotations.Page;
import com.jude.ferryman.annotations.Params;
import com.jude.ferryman.annotations.Result;

/**
 * Created by Jude on 2017/11/21.
 */
@Page("library://second")
public class SecondActivity extends AppCompatActivity {

    @Params     // 页面参数
    String from;
    @Params     // 页面参数
    String to;
    @Result     // 页面返回值
    int score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        Ferryman.inject(this);


    }
}
