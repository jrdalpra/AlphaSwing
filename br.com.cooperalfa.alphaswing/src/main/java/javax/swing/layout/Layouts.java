package javax.swing.layout;

import java.awt.Container;

import javax.swing.JComponent;

import pagelayout.CellGrid;

public class Layouts {

   public static BorderLayoutBuilder border() {
      return new BorderLayoutBuilder();
   }

   public static FlowLayoutBuilder flow() {
      return new FlowLayoutBuilder();
   }

   public static Container form(JComponent[][] rows,
                                Container target) {
      CellGrid grid = CellGrid.createCellGrid(rows);
      grid.createLayout(target);
      return target;
   }

   public static PageLayoutBuilder page() {
      return new PageLayoutBuilder();
   }

   public static JComponent[] row(JComponent... child) {
      return child;
   }

   public static JComponent[][] rows(JComponent[]... components) {
      return components;
   }
}
