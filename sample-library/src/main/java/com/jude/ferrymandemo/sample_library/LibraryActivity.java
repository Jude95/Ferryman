package com.jude.ferrymandemo.sample_library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jude.ferryman.Ferryman;
import com.jude.ferryman.annotations.Page;

/**
 * Created by Jude on 2017/11/20.
 */
@Page("library://test_library")
public class LibraryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ferryman.from(LibraryActivity.this).gotoFuckerActivity("A","B");
            }
        });
    }
}
