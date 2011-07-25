package javax.swing.processor.defaults.property;

import static java.util.Arrays.*;

import java.lang.annotation.Annotation;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.api.ComponentDefinition;
import javax.swing.processor.AnnotationProcessor;
import javax.swing.register.AnnotationProcessorRegistra;
import javax.swing.stereotype.Properties;
import javax.swing.stereotype.Property;
import javax.swing.stereotype.Resource;

@Resource
public class DefaultPropertiesAnnotationProcessor implements AnnotationProcessor {

   @Inject
   private AnnotationProcessorRegistra registra;

   @Override
   public <A extends Annotation> boolean accepts(A annotation,
                                                 ComponentDefinition component) {
      return isProperties(annotation);
   }

   private <A extends Annotation, T> boolean isProperties(A annotation) {
      return Properties.class.isAssignableFrom(annotation.annotationType());
   }

   @Override
   public <A extends Annotation> void process(A annotation,
                                              ComponentDefinition component) {
      for (Property property : asList(Properties.class.cast(annotation).value())) {
         for (AnnotationProcessor processor : registra) {
            if (processor.accepts(property, component)) {
               processor.process(property, component);
            }
         }
      }
   }

   @PostConstruct
   public void register() {
      registra.register(this);
   }

}
