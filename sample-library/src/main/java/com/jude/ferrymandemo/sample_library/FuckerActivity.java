package com.jude.ferrymandemo.sample_library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jude.ferryman.annotations.Page;
import com.jude.ferryman.annotations.Params;
import com.jude.ferryman.annotations.Result;

/**
 * Created by Jude on 2017/11/21.
 */
@Page("library://fuck")
public class FuckerActivity extends AppCompatActivity {
    @Params
    String from;
    @Params
    String to;
    @Result
    int score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuck);
    }
}
