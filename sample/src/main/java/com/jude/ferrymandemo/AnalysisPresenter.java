package com.jude.ferrymandemo;

import android.os.Handler;
import android.view.View;

import com.jude.ferryman.Ferryman;
import com.jude.ferryman.annotations.Params;

/**
 * Created by zhuchenxi on 2017/1/21.
 */
public class AnalysisPresenter {

    @Params
    Person mPerson;

    AnalysisActivity mActivity;

    public AnalysisPresenter(AnalysisActivity activity) {
        mActivity = activity;
        Ferryman.unboxingDataFrom(activity).to(this);
    }

    public void startAnalysis(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.textView.setText(mPerson.getName()+". you are a good man");
                mActivity.progressBar.setVisibility(View.GONE);
            }
        },1000);
    }

    public void finish(){
        Ferryman.boxingDataIn(this).to(mActivity);
        mActivity.finish();
    }
}
