package com.jude.ferryman.internal.router.result;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.jude.ferryman.internal.router.Router;


/**
 * Created by zane on 2017/1/17.
 */

public class HookFragment extends Fragment {

    private OnActivityResultListener listener;

    void startActivityForResult(int requestCode, Intent intent, OnActivityResultListener listener){
        startActivityForResult(intent, requestCode);
        this.listener = listener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = null;
        if (data!=null){
            result = data.getStringExtra(Router.RESPONSE_DATA);
        }
        listener.onActivityResult(result);
    }
}
