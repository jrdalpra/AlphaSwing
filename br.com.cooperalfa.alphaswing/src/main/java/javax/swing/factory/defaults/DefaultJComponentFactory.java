package javax.swing.factory.defaults;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.JComponent;
import javax.swing.factory.ComponentFactory;
import javax.swing.register.ComponentFactoryRegistra;
import javax.swing.stereotype.Resource;

@Resource
public class DefaultJComponentFactory implements ComponentFactory<JComponent> {

   @Inject
   ComponentFactoryRegistra registra;

   @Override
   public boolean canProvide(Class<JComponent> type) {
      return JComponent.class.isAssignableFrom(type);
   }

   @Override
   public JComponent provide(Class<JComponent> type) {
      try {
         return type.newInstance();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   @PostConstruct
   public void register() {
      registra.register(this);
   }

}
