package javax.swing.processor.defaults.property;

import java.awt.Window;
import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.api.ComponentDefinition;
import javax.swing.stereotype.Property;
import javax.swing.stereotype.Resource;
import javax.swing.stereotype.scope.ApplicationScoped;

import org.springframework.core.io.support.ResourcePatternResolver;

@Resource
@ApplicationScoped
public class DefaultIconPropertyAnnotationProcessor extends AbstractPropertyAnnotationProcessor {

   @Inject
   private ResourcePatternResolver resolver;

   public <A extends Annotation> boolean accepts(A annotation,
                                                 ComponentDefinition definition) {
      return super.accepts(annotation, definition) && cast(annotation).name().startsWith("icon");
   }

   @Override
   public <A extends Annotation> void process(A annotation,
                                              ComponentDefinition definition) {
      org.springframework.core.io.Resource resource = resolver.getResource(Property.class.cast(annotation).value());
      try {
         if (Window.class.isInstance(definition.getTarget())) {
            Window.class.cast(definition.getTarget()).setIconImage(ImageIO.read(resource.getURL()));
         } else if (AbstractButton.class.isInstance(definition.getTarget())) {
            AbstractButton.class.cast(definition.getTarget()).setIcon(new ImageIcon(resource.getURL()));
         }
      } catch (IOException e) {
      }
   }

}
