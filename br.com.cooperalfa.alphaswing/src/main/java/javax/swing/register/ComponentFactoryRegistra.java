package javax.swing.register;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.factory.ComponentFactory;
import javax.swing.stereotype.Resource;
import javax.swing.stereotype.scope.ApplicationScoped;

@Resource
@ApplicationScoped
public class ComponentFactoryRegistra implements Iterable<ComponentFactory<?>> {

   List<ComponentFactory<?>> factories;

   public ComponentFactoryRegistra() {
      factories = new ArrayList<ComponentFactory<?>>();
   }

   @Override
   public Iterator<ComponentFactory<?>> iterator() {
      return factories.iterator();
   }

   public void register(ComponentFactory<?> factory) {
      factories.add(factory);
   }

}
