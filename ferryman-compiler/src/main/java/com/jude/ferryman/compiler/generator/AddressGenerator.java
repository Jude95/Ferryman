package com.jude.ferryman.compiler.generator;

import com.jude.ferryman.compiler.model.ActivityInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.lang.model.element.Modifier;

import static com.jude.ferryman.compiler.Constants.CLASS_ADDRESS;
import static com.jude.ferryman.compiler.Constants.PACKAGE_INTERNAL;

/**
 * Created by zane on 2017/1/18.
 */

public class AddressGenerator extends ClassGenerator{

    private List<ActivityInfo> datas;

    public AddressGenerator(List<ActivityInfo> activityInfos){
        super(PACKAGE_INTERNAL, CLASS_ADDRESS);
        datas = activityInfos;
    }

    @Override
    public JavaFile build() {

        FieldSpec routerMap = FieldSpec.builder(Map.class, "routerMap")
                .addModifiers(Modifier.PRIVATE)
                .build();

        /**
         * @Override public Class<? extends Activity> queryTable(String address) {
        if (routerMap.contines(address)) {
        return routerMap.get(address);
        }
        return null;
        }
         */
        ParameterSpec address = ParameterSpec.builder(String.class, "address").build();
        ClassName activity = ClassName.get("android.app", "Activity");
        MethodSpec queryTable = MethodSpec.methodBuilder("queryTable")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(address)
                .beginControlFlow("if (routerMap.containsKey(address))")
                .addStatement("return (Class<? extends $T>) routerMap.get(address)", activity)
                .returns(Class.class)
                .endControlFlow()
                .addStatement("throw new $T(address)", NoSuchElementException.class)
                .build();

        /**
         * private void initTable(){
         //添加信息到routerMap中
         }
         */
        MethodSpec.Builder initTableBuilder = MethodSpec.methodBuilder("initTable")
                .addModifiers(Modifier.PRIVATE);
        for (ActivityInfo activityInfo : datas) {
            String[] urls = activityInfo.getUrl();
            for (int i = 0;i < urls.length;i++) {
                initTableBuilder.addStatement("routerMap.put($S, $T.class)", urls[i], activityInfo.getName());
            }
        }
        MethodSpec initTable = initTableBuilder.build();

        /**
         * 构造函数
         * private routerMapExample() {
         routerMap = new HashMap<>();
         initTable();
         }
         */
        ClassName hashMap = ClassName.get(HashMap.class);
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$N = new $T<String, Class<? extends $T>>()", routerMap, hashMap, activity)
                .addStatement("$N()", initTable)
                .build();

        //开始组装类
        TypeSpec routerMapClass = TypeSpec.classBuilder(getClassName())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constructor)
                .addMethod(initTable)
                .addMethod(queryTable)
                .addField(routerMap)
                .build();

        JavaFile javaFile = JavaFile.builder(getPackageName(), routerMapClass)
                .build();

        return javaFile;
    }
}
