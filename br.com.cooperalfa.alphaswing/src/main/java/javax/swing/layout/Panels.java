package javax.swing.layout;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Panels {

   public static JScrollPane scroll(Component view) {
      JScrollPane result = new JScrollPane(view);
      result.setBorder(BorderFactory.createEmptyBorder());
      return result;
   }

   public static JPanel simple() {
      return new JPanel();
   }

   public static JPanel titled(String title) {
      JPanel result = new JPanel();
      result.setBorder(BorderFactory.createTitledBorder(" " + title.trim() + " "));
      return result;
   }

}
