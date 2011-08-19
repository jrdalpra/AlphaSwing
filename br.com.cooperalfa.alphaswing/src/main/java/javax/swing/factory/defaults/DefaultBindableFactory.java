package javax.swing.factory.defaults;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.bind.BindingSupport;
import javax.swing.factory.ComponentFactory;
import javax.swing.register.ComponentFactoryRegistra;
import javax.swing.stereotype.Bindable;
import javax.swing.stereotype.Resource;
import javax.swing.stereotype.scope.ApplicationScoped;

@Resource
@ApplicationScoped
public class DefaultBindableFactory implements ComponentFactory<Object> {

   @Inject
   private ComponentFactoryRegistra registra;

   @Override
   public boolean canProvide(Class<Object> type) {
      return type.isAnnotationPresent(Bindable.class);
   }

   @PostConstruct
   public void init() {
      registra.register(this);
   }

   @Override
   public Object provide(Class<Object> type) {
      return BindingSupport.bindable(type);
   }

}
