package javax.swing.register;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.processor.PostInstantiatorProcessor;
import javax.swing.stereotype.Resource;
import javax.swing.stereotype.scope.ApplicationScoped;

@Resource
@ApplicationScoped
public class PostInstantiatonProcessorRegistra implements Iterable<PostInstantiatorProcessor> {

   List<PostInstantiatorProcessor> processors;

   public PostInstantiatonProcessorRegistra() {
      processors = new ArrayList<PostInstantiatorProcessor>();
   }

   @Override
   public Iterator<PostInstantiatorProcessor> iterator() {
      return processors.iterator();
   }

   public void register(PostInstantiatorProcessor factory) {
      processors.add(factory);
   }

}
