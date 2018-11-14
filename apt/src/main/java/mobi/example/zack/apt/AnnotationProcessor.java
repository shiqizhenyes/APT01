package mobi.example.zack.apt;

import com.google.auto.service.AutoService;
import mobi.example.zack.apt.processor.InstanceProcessor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.Set;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"mobi.example.zack.lib.InstanceFactory"})
public class AnnotationProcessor extends AbstractProcessor {

    public Filer filer;
    public Messager messager;
    private Elements elements;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elements = processingEnv.getElementUtils();
        new InstanceProcessor().processor(roundEnv, this);
        return true;
    }
}
