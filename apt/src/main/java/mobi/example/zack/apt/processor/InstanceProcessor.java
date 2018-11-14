package mobi.example.zack.apt.processor;

import com.squareup.javapoet.*;
import mobi.example.zack.apt.AnnotationProcessor;
import mobi.example.zack.apt.inter.IProcessor;
import mobi.example.zack.lib.InstanceFactory;


import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.squareup.javapoet.TypeSpec.classBuilder;

public class InstanceProcessor implements IProcessor {

    private static final String className = "FruitsFactory";
    private static final String methodName = "create";

    @Override
    public void processor(RoundEnvironment environment, AnnotationProcessor annotationProcessor) {
        TypeSpec.Builder builder = classBuilder(className);
        builder.addJavadoc("@此类是由apt自动生成")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(Object.class)
                .addParameter(Class.class, "mClass")
                .addException(IllegalAccessException.class)
                .addException(InstantiationException.class);
        List<ClassName> classNames = new ArrayList<>();
        CodeBlock.Builder codeBuilder =  CodeBlock.builder();
        codeBuilder.beginControlFlow("switch (mClass.getSimpleName())");
        for (TypeElement element : ElementFilter.typesIn(environment.getElementsAnnotatedWith(InstanceFactory.class))) {
            annotationProcessor.messager.printMessage(Diagnostic.Kind.NOTE, "正在处理: " + element.toString());
            ClassName className = ClassName.get(element);
            if (classNames.contains(className)) {
                continue;
            }else {
                classNames.add(className);
            }
            codeBuilder.addStatement("case $S:\n return new $T()", className.simpleName(), className);
        }
        codeBuilder.addStatement("default:\n return mClass");
        codeBuilder.endControlFlow();
        methodBuilder.addCode(codeBuilder.build());
        builder.addMethod(methodBuilder.build());
        JavaFile javaFile = JavaFile.builder("com.apt", builder.build()).build();
        try {
            javaFile.writeTo(annotationProcessor.filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
