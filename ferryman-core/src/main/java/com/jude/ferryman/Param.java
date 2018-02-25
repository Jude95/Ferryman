package com.jude.ferryman;

/**
 * Created by Jude on 2018/2/2.
 */

public class Param<T> {
    boolean hasValue = false;
    T value;

    public boolean has() {
        return hasValue;
    }

    public T get() {
        return value;
    }

    public void set(T value){
        this.value = value;
        hasValue = true;
    }
}
