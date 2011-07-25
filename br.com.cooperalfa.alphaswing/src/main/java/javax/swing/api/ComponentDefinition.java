package javax.swing.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public interface ComponentDefinition extends Iterable<ComponentDefinition> {

   List<ComponentDefinition> getChildrenInfo();

   List<Annotation> getEargerAnnotations();

   List<Method> getInitMethods();

   List<Annotation> getLazyAnnotations();

   Object getParent();

   Object getTarget();

   boolean hasBindableChildren();

   boolean hasChildren();

   boolean hasParent();

   boolean isBindable();

}
