package javax.swing.stereotype;

import java.awt.event.ActionListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;

import org.springframework.beans.factory.annotation.Qualifier;

@Retention(RetentionPolicy.RUNTIME)
@Target({
         ElementType.TYPE,
         ElementType.FIELD,
         ElementType.METHOD
})
@IsManaged
@Qualifier
public @interface Action {
   Class<? extends EventListener> listener() default ActionListener.class;

   String method() default "?";

   String qualifier() default "actionPerformed";
}