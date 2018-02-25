package com.jude.ferrymandemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jude.ferryman.Ferryman;
import com.jude.ferryman.Param;
import com.jude.ferryman.annotations.Page;
import com.jude.ferryman.annotations.Params;

/**
 * Created by zane on 2017/1/19.
 */

@Page({"activity://two", "activity://two2"})
public class ActivityTwoDataActivity extends Activity{
    @Params
    Param<String> booleanInActvity;

    @Params
    Param<String> stringInActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ferryman.inject(this);
    }
}
