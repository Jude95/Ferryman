package com.jude.ferrymandemo.sample_library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jude.ferryman.Ferryman;
import com.jude.ferryman.RouterDriver;
import com.jude.ferryman.annotations.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jude on 2017/11/20.
 */
@Page("library://test_library")
public class LibraryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ferryman.from(LibraryActivity.this).gotoSecondActivity("A","B");
            }
        });
        findViewById(R.id.webview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterDriver.startActivity(LibraryActivity.this,"library://webview?url=http%3a%2f%2fnoodle%3furl%3dhttp%3a%2f%2fbee%2fvisit_rpt_index");
            }
        });
        findViewById(R.id.deeplink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animal animal = new Animal();
                animal.setName("beer");
                animal.setAge(12);
                animal.setSpeed(32.67);
                Map<String,Integer> map = new HashMap<>();
                map.put("A",1);
                map.put("B",2);
                map.put("C",123);
                animal.setAttributes(map);
                Ferryman.from(LibraryActivity.this).gotoDeepLinkActivity("beer",12580,animal);
            }
        });
    }
}
