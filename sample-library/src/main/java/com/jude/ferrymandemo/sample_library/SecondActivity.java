package com.jude.ferrymandemo.sample_library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jude.ferryman.Ferryman;
import com.jude.ferryman.annotations.Page;
import com.jude.ferryman.annotations.Params;
import com.jude.ferryman.annotations.Results;
import com.jude.ferryman.record.PageManager;

/**
 * Created by Jude on 2017/11/21.
 */
@Page("library://second")
public class SecondActivity extends AppCompatActivity {

    @Params     // 页面参数
    String from;
    @Params     // 页面参数
    String to;
    @Results     // 页面返回值
    int score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        Ferryman.inject(this);
        Log.i("FerrymanTest",PageManager.printPageStack());
        Log.i("FerrymanTest",PageManager.getTopActivity().getClass().getName());
        Log.i("FerrymanTest",PageManager.getTopActivity(LibraryActivity.class).getClass().getName());
        Log.i("FerrymanTest",PageManager.getDeep(LibraryActivity.class)+"");
        Log.i("FerrymanTest",PageManager.getDeep("com.jude.ferrymandemo.MainActivity")+"");
        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageManager.closeToLastActivity("com.jude.ferrymandemo.MainActivity");
            }
        });
    }
}
