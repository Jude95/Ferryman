package com.jude.ferryman.internal.serialization;

import java.lang.reflect.Type;

/**
 * Created by zhuchenxi on 2017/1/17.
 */

public interface Converter {
    /**
     * 装箱
     * 将内存对象序列化为String类型
     * @param type 待序列化的对象类型
     * @param object 对象
     * @return
     */
    String encode(Type type, Object object);

    /**
     * 拆箱
     * 将String反序列化为内存对象
     * @param type 待生成对象类型
     * @param string 对象文本数据
     * @return
     */
    Object decode(Type type, String string);

    /**
     * 根据不同的类型选择不同的转换器来序列化
     */
    public abstract class Factory{
        public abstract Converter createConverter(Type type);
    }
}
