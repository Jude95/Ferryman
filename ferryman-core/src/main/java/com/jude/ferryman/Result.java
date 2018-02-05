package com.jude.ferryman;

/**
 * Created by Jude on 2018/2/2.
 */

public class Result<T> {
    boolean changed = false;
    T value;

    public boolean changed(){
        return changed;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
        this.changed = true;
    }
}
