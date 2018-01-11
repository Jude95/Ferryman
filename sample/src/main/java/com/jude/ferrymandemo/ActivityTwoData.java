package com.jude.ferrymandemo;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

import com.jude.ferryman.annotations.BindActivity;
import com.jude.ferryman.annotations.Params;
import com.jude.ferryman.annotations.Result;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhuchenxi on 2017/1/20.
 */
@BindActivity(ActivityTwoActivity.class)
public class ActivityTwoData {
    @Params(method = {"A","B"})
    @Nullable
    public List<HashMap<String,List<Integer>>> wtf;
    @Params(method = {"B"})
    public String HUIHJioluHNLI;
    @Params(ignore = true)
    @IdRes
    public int oooo;
    @Result
    public float yee;
    @Result
    @Nullable
    public List<HashMap<String,List<Integer>>> wtfresult;

}
