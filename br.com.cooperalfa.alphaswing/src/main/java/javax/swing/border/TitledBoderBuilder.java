package javax.swing.border;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class TitledBoderBuilder implements BorderBuilder {

   private JComponent target;
   private String     title;

   public TitledBoderBuilder build() {
      this.target.setBorder(BorderFactory.createTitledBorder(this.title));
      return this;
   }

   public JComponent getTarget() {
      return target;
   }

   public TitledBoderBuilder on(JComponent target) {
      this.target = target;
      return this;
   }

   public TitledBoderBuilder title(String title) {
      this.title = title;
      return this;
   }

}
