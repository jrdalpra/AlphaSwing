package javax.swing.stereotype;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Qualifier;

@Retention(RetentionPolicy.RUNTIME)
@Target({
         ElementType.TYPE,
         ElementType.FIELD,
         ElementType.METHOD
})
@Documented
@IsManaged
@Qualifier
public @interface Bindable {
   String property() default "?";

   String source() default "";
}
