package com.jude.ferryman.compiler.generator;

import com.jude.ferryman.compiler.model.ActivityInfo;
import com.jude.ferryman.compiler.model.InjectClassInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.lang.model.element.Modifier;

import static com.jude.ferryman.compiler.Constants.CLASS_ACTIVITY;
import static com.jude.ferryman.compiler.Constants.CLASS_CONTEXT;
import static com.jude.ferryman.compiler.Constants.CLASS_SIPHON;
import static com.jude.ferryman.compiler.Constants.PACKAGE_INTERNAL;

/**
 * Created by Jude on 2017/5/22.
 */

public class SiphonGenerator extends ClassGenerator{
    public static final String PARAMETER_OBJECT = "object";
    public static final String METHOD_TO = "to";
    public static final String PARAMETER_CONTEXT = "context";

    List<ActivityInfo> datas;
    public SiphonGenerator(List<ActivityInfo> datas) {
        super(PACKAGE_INTERNAL, CLASS_SIPHON);
        this.datas = datas;
    }

    @Override
    public JavaFile build() {
        TypeSpec.Builder result =
                TypeSpec.classBuilder(getClassName())
                        .addModifiers(Modifier.PUBLIC);
        addContextField(result);
        addConstructor(result);
        addSiphonMethod(result);
        return JavaFile.builder(getPackageName(), result.build())
                .addFileComment("Generated class from Ferryman. Do not modify!")
                .build();
    }

    private void addContextField(TypeSpec.Builder result){
        result.addField(
                FieldSpec.builder(Object.class, PARAMETER_OBJECT)
                        .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                        .build()
        );
    }

    private void addConstructor(TypeSpec.Builder result){
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Object.class, PARAMETER_OBJECT)
                .addStatement("this.$N = $N", PARAMETER_OBJECT, PARAMETER_OBJECT)
                .build();
        result.addMethod(constructor);
    }

    private void addSiphonMethod(TypeSpec.Builder result){
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(METHOD_TO)
                .addParameter(ClassName.bestGuess(CLASS_CONTEXT), PARAMETER_CONTEXT)
                .addModifiers(Modifier.PUBLIC);
        boolean isFirst = true;
        for (ActivityInfo data : datas) {
            for (InjectClassInfo injectClassInfo : data.getInjectClassInfos()) {
                if (isFirst){
                    methodSpecBuilder.beginControlFlow("if($N.getClass() == $T.class)", PARAMETER_OBJECT,injectClassInfo.getName());
                }else {
                    methodSpecBuilder.nextControlFlow("else if($N.getClass() == $T.class)", PARAMETER_OBJECT,injectClassInfo.getName());
                }
                isFirst = false;
                methodSpecBuilder.addStatement("$T.siphon(($T)$N,($T)$N)",
                        ClassName.get(injectClassInfo.getName().packageName(),injectClassInfo.getName().simpleName()+"Porter"),
                        injectClassInfo.getName(),
                        PARAMETER_OBJECT,
                        ClassName.bestGuess(CLASS_ACTIVITY),
                        PARAMETER_CONTEXT);
            }
        }
        methodSpecBuilder.endControlFlow();
        result.addMethod(methodSpecBuilder.build());
    }

}
