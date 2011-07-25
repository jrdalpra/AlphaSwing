package javax.swing.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.stereotype.InitUI;

import net.vidageek.mirror.dsl.Matcher;
import net.vidageek.mirror.dsl.Mirror;

import org.springframework.core.annotation.AnnotationUtils;

public class MethodUtils {

   public static class InitMethodMatcher implements Matcher<Method> {
      @Override
      public boolean accepts(Method method) {
         return INIT_METHODS.contains(method.getName()) || AnnotationUtils.findAnnotation(method, InitUI.class) != null;
      }

   }

   public static final List<String> INIT_METHODS = new ArrayList<String>();

   static {
      INIT_METHODS.add("initUI");
   }

   private static Matcher<Method> matcher() {
      return new InitMethodMatcher();
   }

   public static List<Method> reflectAllInitMethodsOn(Class<?> type) {
      if (type == null) {
         return new ArrayList<Method>();
      }
      List<Method> result = reflectAllInitMethodsOn(type.getSuperclass());
      result.addAll(new Mirror().on(type).reflectAll().methodsMatching(matcher()));
      return result;
   }

   public static List<Method> sort(List<Method> methods) {
      Collections.sort(methods, new Comparator<Method>() {
         @Override
         public int compare(Method one,
                            Method other) {
            if (!one.isAnnotationPresent(InitUI.class) && !other.isAnnotationPresent(InitUI.class)) {
               return one.getName().compareTo(other.getName());
            }
            InitUI initUIForOne = one.getAnnotation(InitUI.class);
            InitUI initUIForOther = other.getAnnotation(InitUI.class);
            if (initUIForOne == null) {
               return 1;
            }
            if (initUIForOther == null) {
               return -1;
            }
            return new Integer(initUIForOne.order()).compareTo(new Integer(initUIForOther.order()));
         }
      });
      return methods;
   }

}
