package com.jude.ferrymandemo;

import android.app.Activity;

import com.jude.ferryman.annotations.Page;
import com.jude.ferryman.annotations.Params;

/**
 * Created by zane on 2017/1/19.
 */

@Page({"activity://two", "activity://two2"})
public class ActivityTwoActivity extends Activity{
    @Params
    boolean booleanInActvity;

    @Params
    String stringInActivity;
}
