package com.jude.ferryman.compiler.generator;

import com.jude.ferryman.compiler.model.ActivityInfo;
import com.jude.ferryman.compiler.model.FieldInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Map;

import javax.lang.model.element.Modifier;

import static com.jude.ferryman.compiler.Constants.CLASS_INJECT_PORTER;
import static com.jude.ferryman.compiler.Constants.CLASS_INJECT_RESULT;
import static com.jude.ferryman.compiler.Constants.CLASS_RESULT_SUFFIX;
import static com.jude.ferryman.compiler.Constants.CLASS_URL;

/**
 * Created by zhuchenxi on 2017/1/20.
 */

public class ResultGenerator extends ClassGenerator {

    public static final String METHOD_READ = "read";
    public static final String PARAMETER_DATA = "data";

    ActivityInfo info;

    public ResultGenerator(ActivityInfo info) {
        super(info.getName().packageName(), info.getName().simpleName()+CLASS_RESULT_SUFFIX);
        this.info = info;
    }



    @Override
    public JavaFile build() {
        TypeSpec.Builder result =
                TypeSpec.classBuilder(getClassName())
                        .addModifiers(Modifier.PUBLIC)
                        .superclass(ClassName.bestGuess(CLASS_INJECT_RESULT));
        addResultField(result);
        addReadMethod(result);
        addGetterMethod(result);
        addSetterMethod(result);
        return JavaFile.builder(getPackageName(), result.build())
                .addFileComment("Generated class from Ferryman. Do not modify!")
                .build();
    }

    private void addResultField(TypeSpec.Builder result){
        for (FieldInfo fieldInfo : info.getResult()) {
            result.addField(
                    FieldSpec.builder(fieldInfo.getClazz(),fieldInfo.getKey())
                            .addModifiers(Modifier.PRIVATE)
                            .build()
            );
        }
    }

    private void addReadMethod(TypeSpec.Builder result){
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(METHOD_READ)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.BOOLEAN)
                .addParameter(String.class, PARAMETER_DATA)
                .addStatement("$T<String,String> params = $T.parse($L).getParams()",Map.class,ClassName.bestGuess(CLASS_URL),PARAMETER_DATA);
        methodBuilder.beginControlFlow("if (params.isEmpty())")
                .addStatement("return false")
                .nextControlFlow("else");
        int number = 0;
        for (FieldInfo fieldInfo : info.getResult()) {
            generateType(methodBuilder,fieldInfo.getClazz(),number,0,0);
            methodBuilder.addStatement("this.$L = $T.toObject(type$L$L$L,params.get($S));",
                    fieldInfo.getKey(),
                    ClassName.bestGuess(CLASS_INJECT_PORTER),
                    number,0,0,
                    fieldInfo.getKey());
            number++;
        }
        methodBuilder.addStatement("return true");
        methodBuilder.endControlFlow();
        result.addMethod(methodBuilder.build());
    }

    private void addGetterMethod(TypeSpec.Builder result){
        for (FieldInfo fieldInfo : info.getResult()) {
            String fieldName = fieldInfo.getKey();
            String getMethodName = "get" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1,fieldName.length());
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getMethodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(fieldInfo.getClazz())
                    .addStatement("return $L",fieldInfo.getKey());
            result.addMethod(methodBuilder.build());
        }
    }

    private void addSetterMethod(TypeSpec.Builder result){
        for (FieldInfo fieldInfo : info.getResult()) {
            String fieldName = fieldInfo.getKey();
            String getMethodName = "set" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1,fieldName.length());
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getMethodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addParameter(fieldInfo.getClazz(),fieldName)
                    .addStatement("this.$L = $L",fieldInfo.getKey(),fieldInfo.getKey());
            result.addMethod(methodBuilder.build());
        }
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
