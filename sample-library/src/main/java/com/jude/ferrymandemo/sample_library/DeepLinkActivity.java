package com.jude.ferrymandemo.sample_library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jude.ferryman.Ferryman;
import com.jude.ferryman.annotations.Page;
import com.jude.ferryman.annotations.Params;

/**
 * Created by Jude on 2017/12/1.
 */
@Page
public class DeepLinkActivity extends AppCompatActivity{

    @Params
    String name;

    @Params
    int time;

    @Params(ignore = true)
    Animal animal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deeplink);
        Ferryman.inject(this);
        ((TextView)findViewById(R.id.textview)).setText("url: "+getIntent().getData()+"\n\nname: "+name+"\n\ntime: "+time+"\n\nanimal "+animal);
    }
}
