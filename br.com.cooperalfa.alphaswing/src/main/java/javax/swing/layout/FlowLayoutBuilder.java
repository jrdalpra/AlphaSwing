package javax.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;

public class FlowLayoutBuilder implements LayoutBuilder<FlowLayout, FlowLayoutBuilder> {

   private Container  target;
   private FlowLayout layout;

   public FlowLayoutBuilder() {
      this.layout = new FlowLayout();
   }

   public FlowLayoutBuilder center() {
      this.layout.setAlignment(FlowLayout.CENTER);
      return this;
   }

   private void checkTarget() {
      if (!hasTarget()) {
         throw new UnsupportedOperationException("There's no target on this builder!");
      }
   }

   @Override
   public FlowLayout getLayout() {
      return this.layout;
   }

   @Override
   public Container getTarget() {
      checkTarget();
      return this.target;
   }

   private boolean hasTarget() {
      return this.target != null;
   }

   public FlowLayoutBuilder left() {
      this.layout.setAlignment(FlowLayout.LEFT);
      return this;
   }

   @Override
   public FlowLayoutBuilder on(Container target) {
      this.target = target;
      this.target.setLayout(this.layout);
      return this;
   }

   @Override
   public FlowLayoutBuilder with(Component... children) {
      checkTarget();
      for (Component child : children) {
         target.add(child);
      }
      return this;
   }

}
