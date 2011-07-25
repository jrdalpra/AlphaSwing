package javax.swing.processor;

import javax.swing.api.ComponentDefinition;
import javax.swing.stereotype.Action;

public interface ActionProcessor {

   boolean accepts(Action action,
                   ComponentDefinition definition);

   void process(Action action,
                ComponentDefinition definition);

}
