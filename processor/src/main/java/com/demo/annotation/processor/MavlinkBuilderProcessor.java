package com.demo.annotation.processor;

import com.demo.annotation.MavBuilder;
import com.demo.annotation.MavFieldInfo;
import com.google.auto.service.AutoService;
import io.dronefleet.mavlink.annotations.MavlinkMessageInfo;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class MavlinkBuilderProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.size() == 0)
            return false;
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MavBuilder.class);
        for (Element element : elements) {
            MavlinkMessageInfo mavlinkMessageInfo = element.getAnnotation(MavlinkMessageInfo.class);
            if (mavlinkMessageInfo == null) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "MavBuilder must have MavlinkMessageInfo annotation", element);
                return false;
            }
            generateMethod(element);
            generateBuilder(element);

        }
        return true;
    }

    private void generateMethod(Element element) {
        List<? extends Element> fields = element.getEnclosedElements();
        for (Element field : fields) {
            MavFieldInfo mavFieldInfo = field.getAnnotation(MavFieldInfo.class);
            if (mavFieldInfo == null) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "MavBuilder's field must have MavFieldInfo annotation", field);
                return;
            }
            field.getSimpleName();
        }
    }

    private void generateBuilder(Element element) {
        List<? extends Element> fields = element.getEnclosedElements();


    }

    /**
     * java 8 이하의 jdk 에 동작
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(MavBuilder.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
