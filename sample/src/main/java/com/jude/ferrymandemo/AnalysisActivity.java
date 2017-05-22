package com.jude.ferrymandemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jude.ferryman.Ferryman;
import com.jude.ferryman.R;
import com.jude.ferryman.annotations.Page;

/**
 * Created by zhuchenxi on 2017/1/21.
 */
@Page("activity://analysis")
public class AnalysisActivity extends AppCompatActivity {

    AnalysisPresenter mSubmitPresenter;
    TextView textView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        Ferryman.unboxingData(this);
        textView = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mSubmitPresenter = new AnalysisPresenter(this);
        mSubmitPresenter.startAnalysis();
    }
}
