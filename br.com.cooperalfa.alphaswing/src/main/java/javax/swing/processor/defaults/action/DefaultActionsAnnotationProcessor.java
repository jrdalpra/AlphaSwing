package javax.swing.processor.defaults.action;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.api.ComponentDefinition;
import javax.swing.processor.AnnotationProcessor;
import javax.swing.register.AnnotationProcessorRegistra;
import javax.swing.stereotype.Action;
import javax.swing.stereotype.Actions;
import javax.swing.stereotype.Resource;

@Resource
public class DefaultActionsAnnotationProcessor implements AnnotationProcessor {

   @Inject
   AnnotationProcessorRegistra registra;

   @Override
   public <A extends Annotation> boolean accepts(A annotation,
                                                 ComponentDefinition definition) {
      return javax.swing.stereotype.Actions.class.isAssignableFrom(annotation.annotationType());
   }

   @PostConstruct
   public void init() {
      registra.register(this);
   }

   @Override
   public <A extends Annotation> void process(final A annotation,
                                              ComponentDefinition definition) {
      List<Action> actions = Arrays.asList(Actions.class.cast(annotation).value());
      for (Action action : actions) {
         for (AnnotationProcessor processor : registra) {
            if (processor.accepts(action, definition)) {
               processor.process(action, definition);
            }
         }
      }
   }

}
