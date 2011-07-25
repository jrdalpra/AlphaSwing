package javax.swing.register;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.processor.ActionProcessor;
import javax.swing.stereotype.Resource;
import javax.swing.stereotype.scope.ApplicationScoped;

@Resource
@ApplicationScoped
public class ActionProcessorRegistra implements Iterable<ActionProcessor> {

   List<ActionProcessor> processors;

   public ActionProcessorRegistra() {
      processors = new ArrayList<ActionProcessor>();
   }

   @Override
   public Iterator<ActionProcessor> iterator() {
      return processors.iterator();
   }

   public void register(ActionProcessor factory) {
      processors.add(factory);
   }

}
