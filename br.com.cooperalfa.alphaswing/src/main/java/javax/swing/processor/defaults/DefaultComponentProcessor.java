package javax.swing.processor.defaults;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Inject;
import javax.swing.JComponent;
import javax.swing.api.ComponentDefinition;
import javax.swing.api.impl.DefaultComponentDefinition;
import javax.swing.bind.BindManager;
import javax.swing.bind.Binder;
import javax.swing.factory.ComponentFactory;
import javax.swing.processor.AnnotationProcessor;
import javax.swing.processor.ComponentProcessor;
import javax.swing.processor.PostInstantiatorProcessor;
import javax.swing.register.AnnotationProcessorRegistra;
import javax.swing.register.ComponentFactoryRegistra;
import javax.swing.register.PostInstantiatonProcessorRegistra;
import javax.swing.script.MirrorPropertyAccessor;
import javax.swing.stereotype.Resource;
import javax.swing.util.AnnotationUtils;
import javax.swing.util.FieldUtils;

import net.vidageek.mirror.dsl.AccessorsController;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.get.dsl.GetterHandler;
import net.vidageek.mirror.invoke.dsl.InvocationHandler;
import net.vidageek.mirror.set.dsl.SetterHandler;

import org.springframework.context.ApplicationContext;

@Resource("defaultComponentProcessor")
public class DefaultComponentProcessor implements ComponentProcessor {

   @Inject
   private ApplicationContext                spring;

   @Inject
   private ComponentFactoryRegistra          factories;

   @Inject
   private AnnotationProcessorRegistra       processors;

   @Inject
   private PostInstantiatonProcessorRegistra postInstantiatonProcessors;

   private Map<Object, ComponentDefinition>  definitions = Collections.synchronizedMap(new WeakHashMap<Object, ComponentDefinition>());

   @Override
   public <C> C apply(C component) {
      if (isManaged(component)) {
         definitions.put(component, inspect(component));
         applyEargerAnnotations(component);
         applyBindManager(component);
         invokeInitMethods(component);
         applyLazyAnnotations(component);
      }
      return component;
   }

   protected <C> void applyBindManager(C component) {
      ComponentDefinition definition = definitions.get(component);
      if (definition.isBindable() || definition.hasBindableChildren()) {
         spring.getBean(BindManager.class).apply(definition);
      }
   }

   protected <C, P> void applyEargerAnnotations(C component) {
      if (component == null) {
         return;
      }
      processEargerAnnotations(definitions.get(component));
   }

   protected <C, P> void applyLazyAnnotations(C component) {
      if (component == null) {
         return;
      }
      processLazyAnnotations(definitions.get(component));
   }

   protected <C> ComponentDefinition inspect(C component) {
      return new DefaultComponentDefinition(component, component);
   }

   @SuppressWarnings({
            "rawtypes",
            "unchecked"
   })
   @Override
   public <C> C instanciateOn(C component) {
      if (isManaged(component)) {
         AccessorsController _component = new Mirror().on(component);
         GetterHandler _get = _component.get();
         SetterHandler _set = _component.set();
         List<Field> fields = FieldUtils.reflectAllFieldsOn(component.getClass());
         bl_Fields:
         for (Field field : fields) {
            for (ComponentFactory factory : factories) {
               if (factory.canProvide(field.getType())) {
                  if (_get.field(field) == null) {
                     _set.field(field).withValue(factory.provide(field.getType()));
                     for (PostInstantiatorProcessor processor : postInstantiatonProcessors) {
                        if (processor.accepts(_get.field(field))) {
                           processor.process(_get.field(field));
                        }
                     }
                     continue bl_Fields;
                  }
               }
            }
         }
      }
      return component;
   }

   protected <C> void invokeInitMethods(C component) {
      ComponentDefinition parent = definitions.get(component);
      List<Method> methods = null;
      InvocationHandler<Object> invoke;
      if (parent.hasChildren()) {
         for (ComponentDefinition child : parent) {
            methods = child.getInitMethods();
            invoke = new Mirror().on(child.getTarget()).invoke();
            for (Method method : methods) {
               invoke.method(method).withoutArgs();
            }
         }
      }
      methods = parent.getInitMethods();
      invoke = new Mirror().on(component).invoke();
      for (Method method : methods) {
         invoke.method(method).withoutArgs();
      }
   }

   private <C> boolean isFromBindAPI(C component) {
      return MirrorPropertyAccessor.class.isInstance(component) || BindManager.class.isInstance(component) || Binder.class.isInstance(component);
   }

   private <C> boolean isManaged(C component) {
      if (component == null || isFromBindAPI(component)) {
         return false;
      }
      return isManaged(component.getClass()) || AnnotationUtils.hasManagedAnnotation(component.getClass());
   }

   private <C> boolean isManaged(Class<C> type) {
      return JComponent.class.isAssignableFrom(type);
   }

   private <T, P> void processEargerAnnotations(ComponentDefinition definition) {
      if (definition.hasChildren()) {
         for (ComponentDefinition childDefinition : definition) {
            processEargerAnnotations(childDefinition);
         }
      }
      bl_Annotations:
      for (Annotation annotation : definition.getEargerAnnotations()) {
         for (AnnotationProcessor processor : processors) {
            if (processor.accepts(annotation, definition)) {
               processor.process(annotation, definition);
               continue bl_Annotations;
            }
         }
      }
   }

   private <T, P> void processLazyAnnotations(ComponentDefinition definition) {
      if (definition.hasChildren()) {
         for (ComponentDefinition childDefinition : definition) {
            processLazyAnnotations(childDefinition);
         }
      }
      bl_Annotations:
      for (Annotation annotation : definition.getEargerAnnotations()) {
         for (AnnotationProcessor processor : processors) {
            if (processor.accepts(annotation, definition)) {
               processor.process(annotation, definition);
               continue bl_Annotations;
            }
         }
      }
   }

}
