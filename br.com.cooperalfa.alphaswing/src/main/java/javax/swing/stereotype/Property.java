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
         ElementType.FIELD
})
@Documented
@IsManaged
@Qualifier
public @interface Property {

   int constant() default 0;

   boolean lazy() default false;

   String name();

   String value() default "null";

}
