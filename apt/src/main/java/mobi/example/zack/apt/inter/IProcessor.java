package mobi.example.zack.apt.inter;


import mobi.example.zack.apt.AnnotationProcessor;

import javax.annotation.processing.RoundEnvironment;


public interface IProcessor {

    void processor(RoundEnvironment environment, AnnotationProcessor annotationProcessor);
}
