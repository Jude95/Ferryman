package com.jude.ferrymandemo.sample_library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jude.ferryman.Ferryman;
import com.jude.ferryman.annotations.Page;
import com.jude.ferryman.annotations.Params;

/**
 * Created by Jude on 2017/12/4.
 */
@Page(value = "library://webview",noResult = true)
public class WebViewActivity extends AppCompatActivity {

    @Params
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ferryman.inject(this);
        setContentView(R.layout.second);
        ((TextView)findViewById(R.id.textview)).setText(url);
    }
}
