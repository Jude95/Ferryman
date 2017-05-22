package com.jude.ferryman.compiler.generator;

import com.jude.ferryman.compiler.Constants;
import com.jude.ferryman.compiler.model.ActivityInfo;
import com.jude.ferryman.compiler.model.FieldInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;

import javax.lang.model.element.Modifier;

import static com.jude.ferryman.compiler.Constants.CLASS_BOAT;
import static com.jude.ferryman.compiler.Constants.CLASS_CONTEXT;
import static com.jude.ferryman.compiler.Constants.CLASS_PORTER;
import static com.jude.ferryman.compiler.Constants.CLASS_RESULT_SUFFIX;
import static com.jude.ferryman.compiler.Constants.CLASS_URL;
import static com.jude.ferryman.compiler.Constants.CLASS_WARDEN;
import static com.jude.ferryman.compiler.Constants.PACKAGE_MAIN;

/**
 * Created by zhuchenxi on 2017/1/20.
 */

public class BoatGenerator extends ClassGenerator {

    public static final String METHOD_FROM = "from";


    public static final String PARAMETER_CONTEXT = "context";

    public static final String METHOD_API_PREFIX = "goto";



    private ArrayList<ActivityInfo> mActivityInfos;
    public BoatGenerator(ArrayList<ActivityInfo> mActivityInfos) {
        super(PACKAGE_MAIN, CLASS_BOAT);
        this.mActivityInfos = mActivityInfos;
    }

    @Override
    public JavaFile build() {
        TypeSpec.Builder result =
                TypeSpec.classBuilder(getClassName())
                        .addModifiers(Modifier.PUBLIC);
        addContextField(result);
        addConstructor(result);
        addAPIMethod(result);


        return JavaFile.builder(getPackageName(), result.build())
                .addFileComment("Generated class from Ferryman. Do not modify!")
                .build();
    }

    private void addContextField(TypeSpec.Builder result){
        result.addField(
                FieldSpec.builder(ClassName.bestGuess(CLASS_CONTEXT),PARAMETER_CONTEXT)
                        .addModifiers(Modifier.PRIVATE,Modifier.FINAL)
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

    private void addAPIMethod(TypeSpec.Builder result){
        for (ActivityInfo activityInfo : mActivityInfos) {
            String activitySimpleName = activityInfo.getName().simpleName();

            // 如果没有result，就无泛型
            TypeName returnType = null;
            TypeName resultType = null;

            if (activityInfo.getResult().isEmpty()) {
                returnType = ClassName.bestGuess(CLASS_WARDEN);
            }else {
                resultType = ClassName.get(activityInfo.getName().packageName(),activitySimpleName+ CLASS_RESULT_SUFFIX);
                returnType = ParameterizedTypeName.get(ClassName.bestGuess(CLASS_WARDEN),resultType);
            }

            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(METHOD_API_PREFIX+activitySimpleName)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(returnType);

            // 添加启动参数
            for (FieldInfo fieldInfo : activityInfo.getParams()) {
                methodBuilder.addParameter(fieldInfo.getClazz(),fieldInfo.getKey());
            }

            methodBuilder
                    .addStatement("$T.Builder builder = new $T.Builder()",ClassName.bestGuess(CLASS_URL),ClassName.bestGuess(CLASS_URL))
                    .addStatement("builder.setAddress($S)",activityInfo.getUrl()[0]);

            for (FieldInfo fieldInfo : activityInfo.getParams()) {
                methodBuilder.addStatement("builder.addParam($S, $T.fromObject($T.class,$L))",
                        fieldInfo.getKey(),
                        ClassName.bestGuess(CLASS_PORTER),
                        fieldInfo.getClazz(),
                        fieldInfo.getKey());
            }
            if (activityInfo.getResult().isEmpty()){
                methodBuilder.addStatement("$T warden = new $T()",
                        ClassName.bestGuess(Constants.CLASS_WARDEN),
                        ClassName.bestGuess(Constants.CLASS_WARDEN));
            }else {
                methodBuilder.addStatement("$T<$T> warden = new $T<>(new $T())",
                        ClassName.bestGuess(Constants.CLASS_WARDEN),
                        resultType,
                        ClassName.bestGuess(Constants.CLASS_WARDEN),
                        resultType);
            }
            methodBuilder.addStatement("$T.startActivity($L,builder.build().toString(),warden.innerListener)",
                    ClassName.bestGuess(Constants.CLASS_ROUTERDRIVER),
                    PARAMETER_CONTEXT
                    );
            methodBuilder.addStatement("return warden");
            result.addMethod(methodBuilder.build());
        }
    }

}
