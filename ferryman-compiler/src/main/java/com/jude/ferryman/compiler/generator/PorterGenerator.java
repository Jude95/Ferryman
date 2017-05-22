package com.jude.ferryman.compiler.generator;

import com.jude.ferryman.compiler.model.FieldInfo;
import com.jude.ferryman.compiler.model.InjectClassInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Map;

import javax.lang.model.element.Modifier;

import static com.jude.ferryman.compiler.Constants.CLASS_ACTIVITY;
import static com.jude.ferryman.compiler.Constants.CLASS_HASHMAP;
import static com.jude.ferryman.compiler.Constants.CLASS_PORTER;
import static com.jude.ferryman.compiler.Constants.CLASS_PORTER_SUFFIX;

/**
 * Created by zhuchenxi on 2017/1/20.
 * 负责数据装卸的搬运工
 *
 */

public class PorterGenerator extends ClassGenerator {



    public static final String METHOD_INJECT = "inject";
    public static final String METHOD_SIPHON = "siphon";
    public static final String PARAMS_OBJECT = "object";
    public static final String PARAMS_ACTIVITY = "activity";

    private InjectClassInfo info;

    public PorterGenerator(InjectClassInfo info) {
        super(info.getName().packageName(), info.getName().simpleName()+CLASS_PORTER_SUFFIX);
        this.info = info;
    }

    @Override
    public JavaFile build() {
        TypeSpec.Builder result =
                TypeSpec.classBuilder(getClassName())
                        .addModifiers(Modifier.PUBLIC)
                        .superclass(ClassName.bestGuess(CLASS_PORTER));
        addInjectMethod(result);
        addSiphonMethod(result);
        return JavaFile.builder(getPackageName(), result.build())
                .addFileComment("Generated class from Ferryman. Do not modify!")
                .build();
    }

    /**
     * 添加 Params & Result 参数名定义
     *
     *      public static final String NAME = "name";
     *
     * @param builder
     */
    private void addStaticField(TypeSpec.Builder builder){
        for (FieldInfo fieldInfo : info.getParams()) {
            builder.addField(
                    FieldSpec.builder(String.class,fieldInfo.getKey().toUpperCase())
                    .addModifiers(Modifier.PUBLIC,Modifier.STATIC,Modifier.FINAL)
                    .initializer("$S",fieldInfo.getKey())
                    .build()
            );
        }
        for (FieldInfo fieldInfo : info.getResult()) {
            builder.addField(
                    FieldSpec.builder(String.class,fieldInfo.getKey().toUpperCase())
                            .addModifiers(Modifier.PUBLIC,Modifier.STATIC,Modifier.FINAL)
                            .initializer("$S",fieldInfo.getKey())
                            .build()
            );
        }
    }


    /**
     * 添加注入方法
     *
     *     public static void inject(MainActivity object, Activity activity){
     *          Map<String,String> params = readParams(activity);
     *          object.name = toObject(String.class,params.get("name"));
     *     }
     *
     * @param builder
     */
    private void addInjectMethod(TypeSpec.Builder builder){
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(METHOD_INJECT)
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .returns(TypeName.VOID)
                .addParameter(info.getName(), PARAMS_OBJECT)
                .addParameter(ClassName.bestGuess(CLASS_ACTIVITY), PARAMS_ACTIVITY)
                .addStatement("$T<String,String> params = readParams(activity)",Map.class);
        for (FieldInfo fieldInfo : info.getParams()) {
            methodBuilder.addStatement("object.$L = toObject($L.class,params.get($S))",fieldInfo.getName(),fieldInfo.getClazz(),fieldInfo.getKey());
        }
        builder.addMethod(methodBuilder.build());
    }


    /**
     * 添加抽取方法
     *
     *     public static void siphon(MainActivity object, Activity activity){
     *          Url.Builder builder = new Url.Builder();
     *          builder.addParam("age", fromObject(int.class,object.age));
     *          writeResult(builder.build(),activity);
     *     }
     *
     * @param builder
     */
    private void addSiphonMethod(TypeSpec.Builder builder){
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(METHOD_SIPHON)
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .returns(TypeName.VOID)
                .addParameter(info.getName(), PARAMS_OBJECT)
                .addParameter(ClassName.bestGuess(CLASS_ACTIVITY), PARAMS_ACTIVITY)
                .addStatement("$T<String,String> results = new $T<>();",ClassName.bestGuess(CLASS_HASHMAP),ClassName.bestGuess(CLASS_HASHMAP));
        for (FieldInfo fieldInfo : info.getResult()) {
            methodBuilder.addStatement("results.put($S, fromObject($L.class,object.$L))",fieldInfo.getKey(),fieldInfo.getClazz(),fieldInfo.getName());
        }
        methodBuilder.addStatement("writeResult(results,activity)");
        builder.addMethod(methodBuilder.build());
    }

}