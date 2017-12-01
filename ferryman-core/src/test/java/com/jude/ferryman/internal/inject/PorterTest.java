package com.jude.ferryman.internal.inject;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Jude on 2017/12/1.
 */
public class PorterTest {

    @Test
    public void toObject() throws Exception {
        String integer = "12368";
        Object i = Porter.toObject(Integer.class,integer);
        System.out.println(i.getClass());
        Assert.assertEquals(12368,i);
    }
}