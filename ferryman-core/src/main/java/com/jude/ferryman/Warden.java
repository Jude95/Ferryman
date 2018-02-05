package com.jude.ferryman;

import com.jude.ferryman.internal.inject.PageResult;
import com.jude.ferryman.internal.router.result.OnActivityResultListener;

/**
 * Created by Jude on 2017/5/22.
 */

public class Warden<T extends PageResult> {
    OnActivityResultListener innerListener = new OnActivityResultListener() {
        @Override
        public void onActivityResult(String result) {
            if (listener!=null){
                listener.onResult();
            }
            if (dataListener!=null){
                if (result == null || !resultData.read(result)){
                    dataListener.emptyResult();
                }else {
                    dataListener.fullResult(resultData);
                }
            }
        }
    };

    OnResultListener listener;
    OnDataResultListener<T> dataListener;
    T resultData;

    Warden() {
    }

    Warden(T result) {
        this.resultData = result;
    }

    public void onResult(OnResultListener listener){
        this.listener = listener;
    }

    public void onResultWithData(OnDataResultListener<T> listener){
        this.dataListener = listener;
    }

}
