package com.jude.ferrymandemo;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

import com.jude.ferryman.Param;
import com.jude.ferryman.Result;
import com.jude.ferryman.annotations.BindActivity;
import com.jude.ferryman.annotations.Params;
import com.jude.ferryman.annotations.Results;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhuchenxi on 2017/1/20.
 */
@BindActivity(ActivityTwoDataActivity.class)
public class ActivityTwoData {

    @Params
    @Nullable
    public List<HashMap<String,List<Integer>>> wtf;


    @Params
    public Param<String> HUIHJioluHNLI;

    @Params(ignore = true)
    public Param<Integer> oooo;

    @Params(ignore = true)
    @IdRes
    public int xxxx;

    @Results
    public float yee;

    @Results
    @Nullable
    public List<HashMap<String,List<Integer>>> wtfresult;

    @Results
    public Result<HashMap<String,Integer>> dash;

}
