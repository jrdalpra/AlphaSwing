package javax.swing.util;

import java.lang.reflect.Field;
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
         return accepts(field.getType());
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

}
