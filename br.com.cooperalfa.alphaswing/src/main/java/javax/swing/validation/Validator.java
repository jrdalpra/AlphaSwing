package javax.swing.validation;

import java.awt.Component;

public interface Validator {

   void empty();

   boolean hasErrors();

   void showErrorsOn(Component component);

   void validate(Object target);

}
