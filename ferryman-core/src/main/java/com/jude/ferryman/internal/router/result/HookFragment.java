package com.jude.ferryman.internal.router.result;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.jude.ferryman.internal.router.Router;


/**
 * Created by zane on 2017/1/17.
 */
public class HookFragment extends Fragment {

    private SparseArray<OnActivityResultListener> listeners = new SparseArray<>();

    private int id = 0;

    void startActivityForResult(Intent intent, OnActivityResultListener listener){
        startActivityForResult(intent, id);
        listeners.put(id,listener);
        id++;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        OnActivityResultListener listener = listeners.get(requestCode);
        if (listener != null){
            String result = null;
            if (data!=null){
                result = data.getStringExtra(Router.RESPONSE_DATA);
            }
            listener.onActivityResult(result);
            listeners.remove(requestCode);
        }
    }
}
