package javax.swing.register;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.processor.AnnotationProcessor;
import javax.swing.stereotype.Resource;
import javax.swing.stereotype.scope.ApplicationScoped;

@Resource
@ApplicationScoped
public class AnnotationProcessorRegistra implements Iterable<AnnotationProcessor> {

   List<AnnotationProcessor> processors;

   public AnnotationProcessorRegistra() {
      processors = new ArrayList<AnnotationProcessor>();
   }

   @Override
   public Iterator<AnnotationProcessor> iterator() {
      return processors.iterator();
   }

   public void register(AnnotationProcessor factory) {
      processors.add(factory);
   }

}
