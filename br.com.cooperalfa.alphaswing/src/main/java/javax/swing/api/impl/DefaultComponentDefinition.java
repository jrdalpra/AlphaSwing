package javax.swing.api.impl;

import static javax.swing.util.AnnotationUtils.*;
import static javax.swing.util.FieldUtils.*;
import static javax.swing.util.MethodUtils.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.api.ComponentDefinition;
import javax.swing.util.AnnotationUtils;
import javax.swing.util.MethodUtils;

import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.get.dsl.GetterHandler;

public class DefaultComponentDefinition implements ComponentDefinition {

   private Object                    component;
   private Object                    parent;
   private List<ComponentDefinition> children;
   private List<Annotation>          eargerAnnotations;
   private List<Annotation>          lazyAnnotations;
   private List<Method>              initMethods;
   private Boolean                   bindable = false;

   public DefaultComponentDefinition(Object component, Object parent) {
      this.component = component;
      this.parent = parent;
      this.children = new ArrayList<ComponentDefinition>();
      this.eargerAnnotations = new ArrayList<Annotation>();
      this.lazyAnnotations = new ArrayList<Annotation>();
      process();
   }

   @Override
   public List<ComponentDefinition> getChildrenInfo() {
      return this.children;
   }

   @Override
   public List<Annotation> getEargerAnnotations() {
      return this.eargerAnnotations;
   }

   @Override
   public List<Method> getInitMethods() {
      return this.initMethods;
   }

   @Override
   public List<Annotation> getLazyAnnotations() {
      return this.lazyAnnotations;
   }

   @Override
   public Object getParent() {
      return this.parent;
   }

   @Override
   public Object getTarget() {
      return this.component;
   }

   @Override
   public boolean hasBindableChildren() {
      for (ComponentDefinition child : this) {
         if (child.isBindable()) {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasChildren() {
      return children != null && !children.isEmpty();
   }

   @Override
   public boolean hasParent() {
      return this.parent != null;
   }

   @Override
   public boolean isBindable() {
      return this.bindable;
   }

   @Override
   public Iterator<ComponentDefinition> iterator() {
      return this.children.iterator();
   }

   protected DefaultComponentDefinition plus(List<Annotation> annotations) {
      for (Annotation annotation : annotations) {
         if (isLazy(annotation)) {
            this.lazyAnnotations.add(annotation);
         } else {
            this.eargerAnnotations.add(annotation);
         }
         if (AnnotationUtils.isBindable(annotation)) {
            bindable = true;
         }
      }
      return this;
   }

   protected void process() {
      processAnnotations();
      processMethods();
      processChildren();
   }

   protected void processAnnotations() {
      plus(reflectAllAnnotationsOn(component.getClass()));
   }

   protected void processChildren() {
      GetterHandler _get = new Mirror().on(component).get();
      Object child = null;
      for (Field field : reflectAllFieldsOn(component.getClass())) {
         child = _get.field(field);
         if (child != null) {
            this.children.add(new DefaultComponentDefinition(child, component).plus(reflectAllAnnotationsOn(field)));
         }
      }
   }

   protected void processMethods() {
      this.initMethods = reflectAllInitMethodsOn(component.getClass());
      MethodUtils.sort(this.initMethods);
   }

}
