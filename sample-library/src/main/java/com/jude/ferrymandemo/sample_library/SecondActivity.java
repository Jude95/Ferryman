package com.jude.ferrymandemo.sample_library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jude.ferryman.Ferryman;
import com.jude.ferryman.annotations.Page;
import com.jude.ferryman.annotations.Params;
import com.jude.ferryman.annotations.Result;
import com.jude.ferryman.record.PageManager;

/**
 * Created by Jude on 2017/11/21.
 */
@Page("library://second")
public class SecondActivity extends AppCompatActivity {
    @Params
    String from;
    @Params
    int to;
    @Result
    int score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        Ferryman.unboxingData(this);
        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageManager.clearAllStack();
            }
        });
        ((TextView) findViewById(R.id.textview)).setText("url: " + getIntent().getData() +
                "\n\nfrom: " + from
                + "\n\nto: " + to
                + "\n\nscore: " + score
                + "\n\nTop" + PageManager.getTopActivity().getClass().getCanonicalName()
                + "\n\nStack" + PageManager.printPageStack());
    }
}
