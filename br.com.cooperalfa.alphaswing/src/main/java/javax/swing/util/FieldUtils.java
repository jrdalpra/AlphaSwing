package javax.swing.util;

import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.swing.JComponent;

import net.vidageek.mirror.dsl.Matcher;
import net.vidageek.mirror.dsl.Mirror;

public class FieldUtils {

   private static class ManagedFieldMatcher implements Matcher<Field> {
      private boolean accepts(Class<?> type) {
         if (type == null) {
            return false;
         }
         return isManaged(type) || AnnotationUtils.hasManagedAnnotation(type) || accepts(type.getSuperclass());
      }

      @Override
      public boolean accepts(Field field) {
         return !Modifier.isStatic(field.getModifiers()) && accepts(field.getType()) && !isDefinedOnSwingAPI(field);
      }

      private boolean isDefinedOnSwingAPI(Field field) {
         return Component.class.isAssignableFrom(field.getType()) && isOnAPIPackage(field);
      }

      private boolean isOnAPIPackage(Field field) {
         String pkg = field.getDeclaringClass().getPackage().getName();
         return pkg.contains("swing") || pkg.contains("awt");
      }
   }

   public static <C> boolean isManaged(Class<C> type) {
      return JComponent.class.isAssignableFrom(type);
   }

   public static final Matcher<Field> matcher() {
      return new ManagedFieldMatcher();
   }

   public static List<Field> reflectAllFieldsOn(Class<?> type) {
      return new Mirror().on(type).reflectAll().fieldsMatching(matcher());
   }

   public static List<Field> reflectAllFields(Class<?> type) {
      return new Mirror().on(type).reflectAll().fields();
   }

}
