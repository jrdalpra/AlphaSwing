package javax.swing.layout;

import static java.util.Arrays.*;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import pagelayout.Column;
import pagelayout.PageLayout;

public class PageLayoutBuilder implements LayoutBuilder<PageLayout, PageLayoutBuilder> {

   private Container       target;
   private List<Component> children;
   private boolean         hasDone;
   private PageLayout      layout;

   public PageLayoutBuilder() {
      this.children = new ArrayList<Component>();
   }

   @Override
   public PageLayoutBuilder build() {
      // TODO Auto-generated method stub
      return null;
   }

   private void checkTarget() {
      if (!hasTarget()) {
         throw new UnsupportedOperationException("There's no target on this builder!");
      }
   }

   private PageLayout doLayout() {
      if (!hasDone) {
         this.layout = new Column(this.children.toArray(new Component[this.children.size()])).createLayout(target);
      }
     return layout;
   }

   @Override
  public PageLayout getLayout() {
      checkTarget();
      return doLayout();
   }

   @Override
   public Container getTarget() {
     checkTarget();
      doLayout();
      return this.target;
   }

   private boolean hasTarget() {
      return this.target != null;
   }

   @Override
   public PageLayoutBuilder on(Container target) {
      this.target = target;
      return this;
   }

   @Override
   public PageLayoutBuilder with(Component... children) {
      checkTarget();
      this.children.addAll(asList(children));
      return this;
   }

}
