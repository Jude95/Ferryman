package com.jude.ferryman.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhuchenxi on 2017/1/17.
 *
 * 目标Activity所接受的参数注解
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Params {
    String value() default "";
    String[] method() default "";
    boolean ignore() default false;
}
