package javax.swing.processor.defaults.property;

import java.awt.Window;
import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.api.ComponentDefinition;
import javax.swing.processor.AnnotationProcessor;
import javax.swing.register.AnnotationProcessorRegistra;
import javax.swing.stereotype.Icon;
import javax.swing.stereotype.Resource;
import javax.swing.stereotype.scope.ApplicationScoped;

import org.springframework.core.io.support.ResourcePatternResolver;

@Resource
@ApplicationScoped
public class DefaultIconAnnotationProcessor implements AnnotationProcessor {

   @Inject
   private ResourcePatternResolver     resolver;

   @Inject
   private AnnotationProcessorRegistra registra;

   public <A extends Annotation> boolean accepts(A annotation,
                                                 ComponentDefinition definiton) {
      return Icon.class.isAssignableFrom(annotation.annotationType());
   }

   @Override
   public <A extends Annotation> void process(A annotation,
                                              ComponentDefinition definiton) {
      org.springframework.core.io.Resource resource = resolver.getResource(Icon.class.cast(annotation).value());
      try {
         if (Window.class.isInstance(definiton.getTarget())) {
            Window.class.cast(definiton.getTarget()).setIconImage(ImageIO.read(resource.getURL()));
         } else if (AbstractButton.class.isInstance(definiton.getTarget())) {
            AbstractButton.class.cast(definiton.getTarget()).setIcon(new ImageIcon(resource.getURL()));
         }
      } catch (IOException e) {
      }
   }

   @PostConstruct
   public void register() {
      registra.register(this);
   }

}
