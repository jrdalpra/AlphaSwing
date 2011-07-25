package javax.swing.processor.defaults.property;

import java.lang.annotation.Annotation;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.api.ComponentDefinition;
import javax.swing.processor.AnnotationProcessor;
import javax.swing.register.AnnotationProcessorRegistra;
import javax.swing.stereotype.Property;

import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.exception.MirrorException;

public abstract class AbstractPropertyAnnotationProcessor implements AnnotationProcessor {

   @Inject
   private AnnotationProcessorRegistra registra;

   public <A extends Annotation> boolean accepts(A annotation,
                                                 ComponentDefinition component) {
      return isProperty(annotation);
   }

   protected <A extends Annotation> Property cast(A annotation) {
      return Property.class.cast(annotation);
   };

   protected <A extends Annotation> boolean isProperty(A annotation) {
      return Property.class.isAssignableFrom(annotation.annotationType());
   }

   protected <T> void process(Property property,
                              T target,
                              Object value) {
      try {
         new Mirror().on(target).invoke().setterFor(property.name()).withValue(value);
      } catch (MirrorException error) {
         new Mirror().on(target).set().field(property.name()).withValue(value);
      }
   }

   @PostConstruct
   public void register() {
      registra.register(this);
   }

}
