package javax.swing.processor;

import java.lang.annotation.Annotation;

import javax.swing.api.ComponentDefinition;

public interface AnnotationProcessor {

   <A extends Annotation> boolean accepts(A annotation,
                                          ComponentDefinition definition);

   <A extends Annotation> void process(A annotation,
                                       ComponentDefinition definition);

}
