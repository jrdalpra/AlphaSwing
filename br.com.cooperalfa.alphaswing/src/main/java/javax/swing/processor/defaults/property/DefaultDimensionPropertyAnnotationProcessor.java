package javax.swing.processor.defaults.property;

import java.awt.Component;
import java.awt.Dimension;
import java.lang.annotation.Annotation;

import javax.swing.api.ComponentDefinition;
import javax.swing.stereotype.Resource;

@Resource
public class DefaultDimensionPropertyAnnotationProcessor extends AbstractPropertyAnnotationProcessor {

   @Override
   public <A extends Annotation> boolean accepts(A annotation,
                                                 ComponentDefinition definition) {
      return super.accepts(annotation, definition) && cast(annotation).name().equalsIgnoreCase("dimension");
   }

   @Override
   public <A extends Annotation> void process(A annotation,
                                              ComponentDefinition definition) {
      String[] values = cast(annotation).value().replace("[", "").replace("]", "").split(",");
      ((Component) definition.getTarget()).setSize(new Dimension(Integer.parseInt(values[0]), Integer.parseInt(values[1])));
   }

}
