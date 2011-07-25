package javax.swing.spring;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.processor.ComponentProcessor;
import javax.swing.stereotype.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Resource
public class SwingBeanPostProcessor implements BeanPostProcessor {

   @Inject
   @Named("defaultComponentProcessor")
   private ComponentProcessor processor;

   @Override
   public Object postProcessAfterInitialization(Object bean,
                                                String beanName) throws BeansException {
      return processor.apply(bean);
   }

   @Override
   public Object postProcessBeforeInitialization(Object bean,
                                                 String beanName) throws BeansException {
      return processor.instanciateOn(bean);
   }

}
