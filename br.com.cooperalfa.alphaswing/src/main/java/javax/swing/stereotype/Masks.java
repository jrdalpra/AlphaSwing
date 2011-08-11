package javax.swing.stereotype;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
         ElementType.TYPE,
         ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@IsManaged
public @interface Masks {
   Mask[] value();
}
