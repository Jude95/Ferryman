package com.jude.ferryman.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zane on 2017/1/18.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Page {
    /**
     * 某个类的路由信息
     * @return 地址数组
     */
    String[] value() default {};

    boolean noResult() default false;

}