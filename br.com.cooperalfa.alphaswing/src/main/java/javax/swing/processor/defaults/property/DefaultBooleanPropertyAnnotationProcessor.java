package javax.swing.processor.defaults.property;

import java.lang.annotation.Annotation;

import javax.swing.api.ComponentDefinition;
import javax.swing.stereotype.Property;
import javax.swing.stereotype.Resource;

import net.vidageek.mirror.dsl.Mirror;

@Resource
public class DefaultBooleanPropertyAnnotationProcessor extends AbstractPropertyAnnotationProcessor {

   public <A extends Annotation> boolean accepts(A annotation,
                                                 ComponentDefinition definition) {
      return super.accepts(annotation, definition) && isBoolean(cast(annotation), definition.getTarget());
   }

   public <T> boolean isBoolean(Property property,
                                T target) {
      try {
         Class<?> fieldType = new Mirror().on(target.getClass()).reflect().field(property.name()).getType();
         return fieldType.equals(boolean.class) || fieldType.equals(Boolean.class);
      } catch (NullPointerException e) {
      }
      return false;
   }

   @Override
   public <A extends Annotation> void process(A annotation,
                                              ComponentDefinition definition) {
      process(cast(annotation), definition.getTarget(), Boolean.valueOf(cast(annotation).value()));
   }
}
