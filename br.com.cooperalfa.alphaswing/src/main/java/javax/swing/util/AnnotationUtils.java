package javax.swing.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.stereotype.BindGroup;
import javax.swing.stereotype.Bindable;
import javax.swing.stereotype.IsLazy;
import javax.swing.stereotype.IsManaged;
import javax.swing.stereotype.Property;

import net.vidageek.mirror.dsl.Matcher;
import net.vidageek.mirror.dsl.Mirror;

public class AnnotationUtils {

   public static final List<String> lazyProperties = new ArrayList<String>();

   static {
      lazyProperties.add("visible");
   }

   private static class ManagedAnnotationMatcher implements Matcher<Annotation> {
      @Override
      public boolean accepts(Annotation annotation) {
         return hasManagedAnnotation(annotation);
      }

   }

   public static <A extends Annotation> boolean hasManagedAnnotation(A annotation) {
      if (IsManaged.class.isAssignableFrom(annotation.annotationType())) {
         return true;
      }
      List<Annotation> annotations = new Mirror().on(annotation.annotationType()).reflectAll().annotations().atClass();
      for (Annotation current : annotations) {
         if (IsManaged.class.isAssignableFrom(current.annotationType())) {
            return true;
         }
      }
      return false;
   }

   public static <C> boolean hasManagedAnnotation(Class<C> type) {
      List<Annotation> annotations = new Mirror().on(type).reflectAll().annotations().atClass();
      for (Annotation annotation : annotations) {
         if (hasManagedAnnotation(annotation)) {
            return true;
         }
      }
      return false;
   }

   public static Boolean isBindable(Annotation annotation) {
      return Bindable.class.isAssignableFrom(annotation.annotationType()) || BindGroup.class.isAssignableFrom(annotation.annotationType());
   }

   public static boolean isLazy(Annotation annotation) {
      return isLazy(annotation.annotationType()) || isLazyPropertyAnnotation(annotation);
   }

   private static boolean isLazyPropertyAnnotation(Annotation annotation) {
      if (!Property.class.isAssignableFrom(annotation.annotationType())) {
         return false;
      }
      Property property = Property.class.cast(annotation);
      return property.lazy() || lazyProperties.contains(property.name());
   }

   public static boolean isLazy(Class<?> type) {
      return type == null ? false : (type.isAnnotationPresent(IsLazy.class) || isLazy(type.getSuperclass()));
   }

   public static Matcher<Annotation> matcher() {
      return new ManagedAnnotationMatcher();
   }

   public static List<Annotation> reflectAllAnnotationsOn(Class<?> type) {
      if (type == null) {
         return new ArrayList<Annotation>();
      }
      ArrayList<Annotation> result = new ArrayList<Annotation>();
      result.addAll(reflectAllAnnotationsOn(type.getSuperclass()));
      result.addAll(new Mirror().on(type).reflectAll().annotationsMatching(matcher()));
      return result;
   }

   public static List<Annotation> reflectAllAnnotationsOn(Field field) {
      return new Mirror().on(field).reflectAll().annotationsMatching(matcher());
   }

}
