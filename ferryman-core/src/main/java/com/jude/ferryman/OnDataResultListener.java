package com.jude.ferryman;

import android.support.annotation.NonNull;

import com.jude.ferryman.internal.inject.PageResult;

/**
 * Created by Jude on 2017/5/22.
 */

public interface OnDataResultListener<T extends PageResult> {
    /**
     * 正常的返回
     */
    void fullResult(@NonNull T data);

    /**
     * 比如返回键返回，未载入返回数据返回
     */
    void emptyResult();
}
