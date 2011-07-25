package javax.swing.bind;

import java.beans.PropertyChangeListener;

public interface HasBindableSupport {

   void addPropertyChangeListener(PropertyChangeListener listener);

   void addPropertyChangeListener(String property,
                                  PropertyChangeListener listener);

   <T> T getBean();

   void removePropertyChangeListener(PropertyChangeListener listener);

   void removePropertyChangeListener(String property,
                                     PropertyChangeListener listener);

}