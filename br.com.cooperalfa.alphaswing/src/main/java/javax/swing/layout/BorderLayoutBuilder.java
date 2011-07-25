package javax.swing.layout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

public class BorderLayoutBuilder implements LayoutBuilder<BorderLayout, BorderLayoutBuilder> {

   private BorderLayout layout;
   private Container    target;

   public BorderLayoutBuilder() {
      this.layout = new BorderLayout();
   }

   @Override
   public BorderLayout getLayout() {
      return layout;
   }

   @Override
   public Container getTarget() {
      return this.target;
   }

   public BorderLayoutBuilder hgap(int gap) {
      this.layout.setHgap(gap);
      return this;
   }

   @Override
   public BorderLayoutBuilder on(Container target) {
      this.target = target;
      return this;
   }

   public BorderLayoutBuilder vgap(int gap) {
      this.layout.setVgap(gap);
      return this;
   }

   @Override
   public BorderLayoutBuilder with(Component... children) {
      // TODO Auto-generated method stub
      return null;
   }

}
