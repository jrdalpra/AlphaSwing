package javax.swing.validation.impl;

import java.awt.Component;

import javax.swing.stereotype.Resource;
import javax.swing.stereotype.scope.PrototypeScoped;
import javax.swing.validation.Validator;

@Resource
@PrototypeScoped
public class BeanValidator implements Validator {

   @Override
   public void empty() {
      // TODO Auto-generated method stub

   }

   @Override
   public boolean hasErrors() {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public void showErrorsOn(Component component) {
      // TODO Auto-generated method stub

   }

   @Override
   public void validate(Object target) {
      // TODO Auto-generated method stub

   }

}
