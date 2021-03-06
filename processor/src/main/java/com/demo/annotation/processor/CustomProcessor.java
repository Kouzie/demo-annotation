package com.demo.annotation.processor;

import com.demo.annotation.Builder;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@AutoService(Processor.class)
public class CustomProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(Builder.class)) {
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            String className = element.getSimpleName() + "Builder";
            FieldSpec buildable = FieldSpec.builder(ClassName.get(element.asType()), "buildable")
                    .addModifiers(Modifier.PRIVATE)
                    .build();
            TypeSpec.Builder outputType = TypeSpec.classBuilder(className)
                    .addField(buildable)
                    .addModifiers(Modifier.PUBLIC);
            if (element.getKind() == ElementKind.CLASS) {
                for (Element innerElement : element.getEnclosedElements())
                    if (innerElement.getKind() == ElementKind.FIELD) {
                        ParameterSpec inputParam = ParameterSpec
                                .builder(ClassName.get(innerElement.asType()), innerElement.getSimpleName().toString())
                                .build();
                        MethodSpec setter = MethodSpec.methodBuilder(innerElement.getSimpleName().toString())
                                .addParameter(inputParam)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(ClassName.get(packageName, className))
                                .addStatement("buildable." + innerElement.getSimpleName() + " = " + innerElement.getSimpleName().toString())
                                .addStatement("return this")
                                .build();
                        outputType.addMethod(setter);
                    }
                MethodSpec privateConstructor = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addStatement("this.buildable= new " + element.getSimpleName() + "()")
                        .build();
                outputType.addMethod(privateConstructor);
                MethodSpec staticInstance = MethodSpec.methodBuilder("having")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(ClassName.get(packageName, className))
                        .addStatement("return new " + className + "()")
                        .build();
                outputType.addMethod(staticInstance);
                MethodSpec getter = MethodSpec.methodBuilder("get")
                        .returns(ClassName.get(element.asType()))
                        .addStatement("return this.buildable")
                        .build();
                outputType.addMethod(getter);
                JavaFile javaFile = JavaFile.builder(packageName, outputType.build())
                        .addFileComment("This file is auto-generated and should not be edited.")
                        .build();
                try {
                    javaFile.writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    /**
     * java 8 이하의 jdk 에 동작
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Builder.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
