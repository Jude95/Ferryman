package com.jude.ferryman.compiler.generator;

import com.jude.ferryman.compiler.Constants;
import com.jude.ferryman.compiler.model.FieldInfo;
import com.jude.ferryman.compiler.model.InjectClassInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Map;

import javax.lang.model.element.Modifier;

import static com.jude.ferryman.compiler.Constants.CLASS_ACTIVITY;
import static com.jude.ferryman.compiler.Constants.CLASS_HASHMAP;
import static com.jude.ferryman.compiler.Constants.CLASS_INJECT_PORTER;
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
                        .superclass(ClassName.bestGuess(CLASS_INJECT_PORTER));
        addInjectMethod(result);
        addSiphonMethod(result);
        return JavaFile.builder(getPackageName(), result.build())
                .addFileComment("Generated class from Ferryman. Do not modify!")
                .build();
    }

    /**
     * 添加 Params & Results 参数名定义
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
                .addParameter(ClassName.bestGuess(CLASS_ACTIVITY), PARAMS_ACTIVITY);

        // instance the Param & Result
        for (FieldInfo fieldInfo : info.getParams()) {
            if (fieldInfo.hasWrapper()){
                methodBuilder.addStatement("object.$L = new $T<$T>()",fieldInfo.getName(),ClassName.bestGuess(Constants.CLASS_PARAM),fieldInfo.getClazz());
            }
        }
        for (FieldInfo fieldInfo : info.getResult()) {
            if (fieldInfo.hasWrapper()){
                methodBuilder.addStatement("object.$L = new $T<$T>()",fieldInfo.getName(),ClassName.bestGuess(Constants.CLASS_RESULT),fieldInfo.getClazz());
            }
        }

        methodBuilder.addStatement("$T<String,String> params = readParams(activity)",Map.class);
        int number = 0;
        for (FieldInfo fieldInfo : info.getParams()) {
            generateType(methodBuilder,fieldInfo.getClazz(),number,0,0);
            methodBuilder.beginControlFlow("if (params.containsKey($S))",fieldInfo.getKey());
            if (fieldInfo.hasWrapper()){
                methodBuilder.addStatement("object.$L.set(($T)toObject(type$L$L$L,params.get($S)))",fieldInfo.getName(),fieldInfo.getClazz(),number,0,0,fieldInfo.getKey());
            }else {
                methodBuilder.addStatement("object.$L = toObject(type$L$L$L,params.get($S))",fieldInfo.getName(),number,0,0,fieldInfo.getKey());
            }
            methodBuilder.endControlFlow();
            number++;
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
        int number = 0;
        for (FieldInfo fieldInfo : info.getResult()) {
            if (fieldInfo.hasWrapper()){
                methodBuilder.beginControlFlow("if (object.$L.changed())",fieldInfo.getName());
                generateType(methodBuilder,fieldInfo.getClazz(),number,0,0);
                methodBuilder.addStatement("results.put($S, fromObject(type$L$L$L,object.$L.get()))",fieldInfo.getKey(),number,0,0,fieldInfo.getName());
                methodBuilder.endControlFlow();
            }else {
                generateType(methodBuilder,fieldInfo.getClazz(),number,0,0);
                methodBuilder.addStatement("results.put($S, fromObject(type$L$L$L,object.$L))",fieldInfo.getKey(),number,0,0,fieldInfo.getName());
            }
            number++;
        }
        methodBuilder.addStatement("writeResult(results,activity)");
        builder.addMethod(methodBuilder.build());
    }

    private void generateType(MethodSpec.Builder builder, TypeName typeName,int number, int index, int deep){
        TypeName typeToken = ClassName.bestGuess("com.google.gson.reflect.TypeToken");
        TypeName type = ClassName.bestGuess("java.lang.reflect.Type");
        if (typeName instanceof ParameterizedTypeName){
            for (int i = 0; i < ((ParameterizedTypeName) typeName).typeArguments.size(); i++) {
                generateType(builder, ((ParameterizedTypeName) typeName).typeArguments.get(i),number,i,deep+1);
            }
            String argString = "";
            for (int i = 0; i < ((ParameterizedTypeName) typeName).typeArguments.size(); i++) {
                argString += ", type"+number+i+(deep+1);
            }
            builder.addStatement("$T type$L$L$L = $T.getParameterized($T.class"+argString+").getType()",type,number,index,deep,typeToken,((ParameterizedTypeName) typeName).rawType);
        }else {
            builder.addStatement("$T type$L$L$L = $T.get($T.class).getType()",type,number,index,deep,typeToken,typeName);
        }
    }


}
