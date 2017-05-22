package com.jude.ferrymandemo;

import com.jude.ferryman.annotations.BindActivity;
import com.jude.ferryman.annotations.Params;
import com.jude.ferryman.annotations.Result;

/**
 * Created by zhuchenxi on 2017/1/20.
 */
@BindActivity(ActivityTwo.class)
public class ActivityTwoData {
    @Params
    public String wtf;
    @Params
    public String HUIHJioluHNLI;
    @Params
    public int oooo;
    @Result
    public float yee;

}
