package javax.swing.stereotype.scope;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Scope;

import org.springframework.beans.factory.config.BeanDefinition;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Scope
@org.springframework.context.annotation.Scope(BeanDefinition.SCOPE_SINGLETON)
public @interface ApplicationScoped {
}
