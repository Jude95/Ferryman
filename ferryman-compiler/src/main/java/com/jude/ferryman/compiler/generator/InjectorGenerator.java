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
import static com.jude.ferryman.compiler.Constants.CLASS_INJECTOR;
import static com.jude.ferryman.compiler.Constants.PACKAGE_INTERNAL;

/**
 * Created by Jude on 2017/5/22.
 */

public class InjectorGenerator extends ClassGenerator{
    public static final String PARAMETER_CONTEXT = "context";
    public static final String METHOD_TO = "to";
    public static final String PARAMETER_TARGET = "target";

    List<ActivityInfo> datas;
    public InjectorGenerator(List<ActivityInfo> datas) {
        super(PACKAGE_INTERNAL, CLASS_INJECTOR);
        this.datas = datas;
    }

    @Override
    public JavaFile build() {
        TypeSpec.Builder result =
                TypeSpec.classBuilder(getClassName())
                        .addModifiers(Modifier.PUBLIC);
        addContextField(result);
        addConstructor(result);
        addInjectMethod(result);
        return JavaFile.builder(getPackageName(), result.build())
                .addFileComment("Generated class from Ferryman. Do not modify!")
                .build();
    }

    private void addContextField(TypeSpec.Builder result){
        result.addField(
                FieldSpec.builder(ClassName.bestGuess(CLASS_CONTEXT),PARAMETER_CONTEXT)
                        .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                        .build()
        );
    }

    private void addConstructor(TypeSpec.Builder result){
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.bestGuess(CLASS_CONTEXT), PARAMETER_CONTEXT)
                .addStatement("this.$N = $N", PARAMETER_CONTEXT, PARAMETER_CONTEXT)
                .build();
        result.addMethod(constructor);
    }

    private void addInjectMethod(TypeSpec.Builder result){
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(METHOD_TO)
                .addParameter(Object.class, PARAMETER_TARGET)
                .addModifiers(Modifier.PUBLIC);
        boolean isFirst = true;
        for (ActivityInfo data : datas) {
            for (InjectClassInfo injectClassInfo : data.getInjectClassInfos()) {
                if (isFirst){
                    methodSpecBuilder.beginControlFlow("if($N.getClass() == $T.class)",PARAMETER_TARGET,injectClassInfo.getName());
                }else {
                    methodSpecBuilder.nextControlFlow("else if($N.getClass() == $T.class)",PARAMETER_TARGET,injectClassInfo.getName());
                }
                isFirst = false;
                methodSpecBuilder.addStatement("$T.inject(($T)$N,($T)$N)",
                        ClassName.get(injectClassInfo.getName().packageName(),injectClassInfo.getName().simpleName()+"Porter"),
                        injectClassInfo.getName(),
                        PARAMETER_TARGET,
                        ClassName.bestGuess(CLASS_ACTIVITY),
                        PARAMETER_CONTEXT);
            }
        }
        methodSpecBuilder.endControlFlow();
        result.addMethod(methodSpecBuilder.build());
    }

}
