package javax.swing.processor.defaults.property;

import java.lang.annotation.Annotation;

import javax.swing.api.ComponentDefinition;
import javax.swing.stereotype.Property;
import javax.swing.stereotype.Resource;

import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.exception.MirrorException;

@Resource
public class DefaultStringPropertyAnnotationProcessor extends AbstractPropertyAnnotationProcessor {

   @Override
   public <A extends Annotation> boolean accepts(A annotation,
                                                 ComponentDefinition component) {
      return super.accepts(annotation, component) && (isText(annotation) || isString(annotation, component.getTarget()));
   }

   private <A extends Annotation, T> boolean isString(A annotation,
                                                      T target) {
      try {
         return new Mirror().on(target.getClass()).reflect().field(cast(annotation).name()).getType().equals(String.class);
      } catch (NullPointerException npe) {
      }
      return false;
   }

   private <A extends Annotation> boolean isText(A annotation) {
      return cast(annotation).name().equals("text");
   }

   @Override
   public <A extends Annotation> void process(A annotation,
                                              ComponentDefinition component) {
      Property property = cast(annotation);
      try {
         new Mirror().on(component.getTarget()).invoke().setterFor(property.name()).withValue(property.value());
      } catch (MirrorException error) {
         new Mirror().on(component.getTarget()).set().field(property.name()).withValue(property.value());
      }
   }

}
