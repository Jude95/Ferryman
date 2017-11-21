package com.jude.ferryman.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.jude.ferryman.annotations.ActivityRelation;
import com.jude.ferryman.annotations.BindActivity;
import com.jude.ferryman.annotations.Page;
import com.jude.ferryman.annotations.Params;
import com.jude.ferryman.annotations.Result;
import com.jude.ferryman.compiler.generator.AddressGenerator;
import com.jude.ferryman.compiler.generator.BoatGenerator;
import com.jude.ferryman.compiler.generator.InjectorGenerator;
import com.jude.ferryman.compiler.generator.PorterGenerator;
import com.jude.ferryman.compiler.generator.ResultGenerator;
import com.jude.ferryman.compiler.generator.SiphonGenerator;
import com.jude.ferryman.compiler.model.ActivityInfo;
import com.jude.ferryman.compiler.model.FieldInfo;
import com.jude.ferryman.compiler.model.InjectClassInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.sun.source.util.Trees;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by zhuchenxi on 2017/1/18.
 */
@AutoService(Processor.class)
public class FerrymanProcessor extends AbstractProcessor {
    private static final String ANDROID_ACTIVITY_CLASS_NAME = "android.app.Activity";

    private Messager messager;
    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;
    private Trees trees;

    ActivityRelations mActivityRelations;

    private ArrayList<ActivityInfo> mActivityInfos = new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();
        filer = env.getFiler();
        messager = processingEnv.getMessager();
        try {
            trees = Trees.instance(processingEnv);
        } catch (IllegalArgumentException ignored) {
        }
        mActivityRelations = new ActivityRelations();
    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();

        // 确保处理器一定会被执行
        annotations.add(Override.class);

        annotations.add(ActivityRelation.class);
        annotations.add(BindActivity.class);
        annotations.add(Params.class);
        annotations.add(Result.class);
        annotations.add(Page.class);
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        collectActivityInfos(env);
        try {
            new AddressGenerator(mActivityInfos).build().writeTo(filer);
            new BoatGenerator(mActivityInfos).build().writeTo(filer);
            new InjectorGenerator(mActivityInfos).build().writeTo(filer);
            new SiphonGenerator(mActivityInfos).build().writeTo(filer);
            writeActivityPorterFile();
            writeResultFile();
        } catch (IOException e) {
        }
        return true;
    }


    public void collectActivityInfos(RoundEnvironment env){
        for (Element element : env.getElementsAnnotatedWith(ActivityRelation.class)) {
            parseRelations(element);
        }
        for (Element element : env.getElementsAnnotatedWith(BindActivity.class)) {
            parseBindActivity(element);
        }
        for (Element element : env.getElementsAnnotatedWith(Page.class)) {
            parseActivity(element);
        }
        for (Element element : env.getElementsAnnotatedWith(Params.class)) {
            paramsParams(element);
        }
        for (Element element : env.getElementsAnnotatedWith(Result.class)) {
            paramsResult(element);
        }
    }

    public void writeActivityPorterFile(){
        for (ActivityInfo activityInfo : mActivityInfos) {
            try {
                for (InjectClassInfo injectClassInfo : activityInfo.getInjectClassInfos()) {
                    new PorterGenerator(injectClassInfo).build().writeTo(filer);
                }
            } catch (IOException e) {
                //error("Unable to write ( " + e.getMessage() + " )");
            }
        }
    }

    public void writeResultFile(){
        try {
            for (ActivityInfo activityInfo : mActivityInfos) {
                if (!activityInfo.getResult().isEmpty()){
                    new ResultGenerator(activityInfo).build().writeTo(filer);

                }
            }
        } catch (IOException e) {

        }
    }


    /**
     * 添加Activity与注入对象的映射
     */
    public void parseBindActivity(Element element){
        BindActivity relation = element.getAnnotation(BindActivity.class);
        TypeElement enclosingElement = (TypeElement) element;
        try {
            relation.value();
        } catch( MirroredTypeException mte) {
            String activityName = ((TypeElement)typeUtils.asElement(mte.getTypeMirror())).getQualifiedName().toString();
            mActivityRelations.addActivityMaping(activityName,enclosingElement.getQualifiedName().toString());
        }
    }

    /**
     * 添加Activity与注入对象的映射规则
     */
    public void parseRelations(Element element){
        ActivityRelation relation = element.getAnnotation(ActivityRelation.class);
        mActivityRelations.addRelation(relation.activityNameRegular(),relation.objectNameRegular());
    }

    public void parseActivity(Element element){
        if (!SuperficialValidation.validateElement(element)) {
            error("Superficial validation error for %s", element.getSimpleName());
            return;
        }
        if (!Validator.isNotAbstractClass(element)) {
            error("%s is abstract", element.getSimpleName());
            return;
        }
        boolean isActivity =
                Validator.isSubType(element, ANDROID_ACTIVITY_CLASS_NAME, processingEnv);

        if (!isActivity) {
            error("%s must extend Activity or AppCompatActivity", element.getSimpleName());
            return;
        }

        TypeElement enclosingElement = (TypeElement) element;
        Page page = element.getAnnotation(Page.class);
        String[] pageUrls = page.value();
        if (pageUrls.length == 0){
            pageUrls = new String[1];
            pageUrls[0] = enclosingElement.getQualifiedName().toString();
        }
        ActivityInfo info = new ActivityInfo(ClassName.bestGuess(enclosingElement.getQualifiedName().toString()),pageUrls);
        mActivityInfos.add(info);
    }

    public void paramsParams(Element element){
        if (!SuperficialValidation.validateElement(element)) {
            error("Superficial validation error for %s", element.getSimpleName());
            return;
        }
        if (Validator.isPrivate(element)) {
            error("%s can't be private", element.getSimpleName());
            return;
        }
        VariableElement variableElement = (VariableElement) element;
        Params params = element.getAnnotation(Params.class);
        FieldInfo info = new FieldInfo(variableElement.getSimpleName().toString(),params.value(),convertClass(element.asType()));
        ActivityInfo activityInfo = findActivityInfo(element);
        activityInfo.addParams(info);
        InjectClassInfo injectClassInfo = findInjectClassInfo(activityInfo,element);
        injectClassInfo.addParams(info);
    }

    public void paramsResult(Element element){
        if (!SuperficialValidation.validateElement(element)) {
            error("Superficial validation error for %s", element.getSimpleName());
            return;
        }
        if (Validator.isPrivate(element)) {
            error("%s can't be private", element.getSimpleName());
            return;
        }
        VariableElement variableElement = (VariableElement) element;
        Result result = element.getAnnotation(Result.class);
        FieldInfo info = new FieldInfo(variableElement.getSimpleName().toString(),result.value(),convertClass(element.asType()));
        ActivityInfo activityInfo = findActivityInfo(element);
        activityInfo.addResult(info);
        InjectClassInfo injectClassInfo = findInjectClassInfo(activityInfo,element);
        injectClassInfo.addResult(info);
    }


    public InjectClassInfo findInjectClassInfo(ActivityInfo activityInfo,Element element){
        TypeElement parent = (TypeElement) element.getEnclosingElement();
        for (InjectClassInfo injectClassInfo : activityInfo.getInjectClassInfos()) {
            if (injectClassInfo.getName().toString().equals(parent.getQualifiedName().toString())){
                return injectClassInfo;
            }
        }
        InjectClassInfo injectClassInfo = new InjectClassInfo(ClassName.bestGuess(parent.getQualifiedName().toString()));
        activityInfo.addInjectClassInfos(injectClassInfo);
        return injectClassInfo;
    }

    public ActivityInfo findActivityInfo(Element element){
        TypeElement parent = (TypeElement) element.getEnclosingElement();
        boolean isInActivity = Validator.isSubType(parent, ANDROID_ACTIVITY_CLASS_NAME, processingEnv);

        ActivityInfo activityInfo = null;
        if (isInActivity){
            String activityName = parent.getQualifiedName().toString();
            activityInfo = findActivityByName(activityName);
            if (activityInfo == null){
                error("activity without com.jude.ferryman.annotations error for %s",activityName);
                throw new IllegalStateException();
            }
        }else {
            String objectName = parent.getQualifiedName().toString();
            for (ActivityInfo info1 : mActivityInfos) {
                if (mActivityRelations.isRelated(info1.getName().toString(),objectName)){
                    activityInfo = info1;
                }
            }
        }
        if (activityInfo == null){
            throw new IllegalStateException(String.format("no associated activity error for %s",parent.getQualifiedName()));
        }
        return activityInfo;
    }




    public ActivityInfo findActivityByName(String name){
            for (ActivityInfo activityInfo : mActivityInfos) {
                if (activityInfo.getName().toString().equals(name)){
                    return activityInfo;
                }
            }
        return null;
    }

    private void error(String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args));
    }

    private void log(String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format(message, args));
    }

    private static TypeName convertClass(TypeMirror typeMirror) {
        return TypeName.get(typeMirror);
    }

}
