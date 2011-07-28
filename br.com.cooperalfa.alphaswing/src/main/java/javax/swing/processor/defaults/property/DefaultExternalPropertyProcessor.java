package javax.swing.processor.defaults.property;

import java.lang.annotation.Annotation;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.api.ComponentDefinition;
import javax.swing.stereotype.Property;
import javax.swing.stereotype.Resource;

import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

@Resource
public class DefaultExternalPropertyProcessor extends AbstractPropertyAnnotationProcessor {

   @Inject
   private ApplicationContext             spring;

   @Inject
   @Named("swingBeanExpressionResolver")
   private StandardBeanExpressionResolver resolver;

   private BeanExpressionContext          context;

   @Override
   public <A extends Annotation> boolean accepts(A annotation,
                                                 ComponentDefinition definiton) {
      return super.isProperty(annotation) && ((cast(annotation).value().startsWith("${") || cast(annotation).value().startsWith("#{")) && cast(annotation).value().endsWith("}"));
   }

   @PostConstruct
   public void init() {
      context = new BeanExpressionContext(((AbstractRefreshableConfigApplicationContext) spring).getBeanFactory(), null);
   }

   @Override
   public <A extends Annotation> void process(A annotation,
                                              ComponentDefinition definiton) {
      process(cast(annotation), definiton.getTarget(), value(cast(annotation)));
   }

   private Object value(Property property) {
      return resolver.evaluate(property.value(), context);
   }

}