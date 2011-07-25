package javax.swing.processor.defaults.action;

import java.lang.annotation.Annotation;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.api.ComponentDefinition;
import javax.swing.processor.ActionProcessor;
import javax.swing.processor.AnnotationProcessor;
import javax.swing.register.ActionProcessorRegistra;
import javax.swing.register.AnnotationProcessorRegistra;
import javax.swing.stereotype.Action;
import javax.swing.stereotype.Resource;

@Resource
public class DefaultActionAnnotationProcessor implements AnnotationProcessor {

   @Inject
   private AnnotationProcessorRegistra registra;

   @Inject
   private ActionProcessorRegistra     actions;

   @Override
   public <A extends Annotation> boolean accepts(A annotation,
                                                 ComponentDefinition definition) {
      return javax.swing.stereotype.Action.class.isAssignableFrom(annotation.annotationType());
   }

   @PostConstruct
   public void init() {
      registra.register(this);
   }

   @Override
   public <A extends Annotation> void process(A annotation,
                                              ComponentDefinition definition) {
      for (ActionProcessor processor : actions) {
         if (processor.accepts(Action.class.cast(annotation), definition)) {
            processor.process(Action.class.cast(annotation), definition);
            break;
         }
      }
   }

}
