package javax.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingUtilities;

public class RelativeLayout implements LayoutManager2 {

   private static class Pair {
      private Component  component;
      private Constraint constraint;

      public Pair(Component component, Constraint constraint) {
         super();
         this.component = component;
         this.constraint = constraint;
      }
   }

   private interface IsAConstraint<E extends Enum<E>> {
      void process(Constraint constraint, Component parent, Component target);

      boolean accepts(String constraint);
   }

   public static enum X implements IsAConstraint<X> {
      LEFT, RIGHT, CENTER, SAME, AFTER, BEFORE;

      @Override
      public void process(Constraint constraint, Component parent, Component target) {
         switch (this) {
            case LEFT: {
               constraint.location.setLocation(0 + constraint.gapX + constraint.plusX, constraint.location.y);
               break;
            }
            case RIGHT: {
               constraint.location.setLocation(parent.getSize().width - constraint.size.width - constraint.gapX + constraint.plusX, constraint.location.y);
               break;
            }
            case CENTER: {
               switch (constraint.width) {
                  case FILL: {
                     constraint.location.setLocation((parent.getSize().width / 2) - constraint.gapX + constraint.plusX, constraint.location.y);
                     break;
                  }
                  default: {
                     constraint.location.setLocation((parent.getSize().width / 2) - (constraint.size.width / 2) + constraint.gapX + constraint.plusX, constraint.location.y);
                     break;
                  }
               }
               break;
            }
            case SAME: {
               constraint.location.setLocation(constraint.relative.getLocation().x, constraint.location.y);
               break;
            }
            case AFTER: {
               constraint.location.setLocation(constraint.relative.getLocation().x + constraint.relative.getSize().width + constraint.gapX + constraint.plusX, constraint.location.y);
               break;
            }
            case BEFORE: {
               constraint.location.setLocation(constraint.relative.getLocation().x - constraint.size.width - constraint.gapX + constraint.plusX, constraint.location.y);
               break;
            }
         }
      }

      @Override
      public boolean accepts(String constraint) {
         return constraint.toUpperCase().contains(name());
      }

   }

   public static enum Y implements IsAConstraint<Y> {
      TOP, BOTTOM, CENTER, SAME, AFTER, BEFORE;

      @Override
      public void process(Constraint constraint, Component parent, Component target) {
         switch (this) {
            case TOP: {
               constraint.location.setLocation(constraint.location.x, 0 + constraint.gapY + constraint.plusY);
               break;
            }
            case BOTTOM: {
               constraint.location.setLocation(constraint.location.x, parent.getSize().height - constraint.size.height + (constraint.gapY * -1) + constraint.plusY);
               break;
            }
            case CENTER: {
               constraint.location.setLocation(constraint.location.x, (parent.getSize().height / 2) - (constraint.size.height / 2) + constraint.gapY + constraint.plusY);
               break;
            }
            case SAME: {
               constraint.location.setLocation(constraint.location.x, constraint.relative.getLocation().y);
               break;
            }
            case AFTER: {
               constraint.location.setLocation(constraint.location.x, constraint.relative.getLocation().y + constraint.relative.getSize().height + constraint.gapY
                        + constraint.plusY);
               break;
            }
            case BEFORE: {
               constraint.location.setLocation(constraint.location.x, constraint.relative.getLocation().y - constraint.size.height - constraint.gapY + constraint.plusY);
               break;
            }
         }
      }

      @Override
      public boolean accepts(String constraint) {
         return constraint.toUpperCase().contains(name());
      }

   }

   public static enum Width implements IsAConstraint<Width> {
      PREFFERED, FILL, FIT, SAME;

      @Override
      public void process(Constraint constraint, Component parent, Component target) {
         constraint.size = constraint.size == null ? target.getPreferredSize() : constraint.size;
         switch (this) {
            case FILL: {
               switch (constraint.x) {
                  case LEFT: {
                     constraint.size.width = (int) (parent.getSize().width - constraint.gapX + constraint.plusWidth);
                     break;
                  }
                  case CENTER: {
                     constraint.size.width = (int) ((parent.getSize().width / 2) - constraint.gapX + constraint.plusWidth);
                     break;
                  }
                  default: {
                     constraint.size.width += constraint.plusWidth - constraint.gapX;
                     break;
                  }
               }
               break;
            }
            case SAME: {
               constraint.size.setSize(constraint.relative.getSize().width + constraint.plusWidth, constraint.size.getHeight());
               break;
            }
         }
      }

      @Override
      public boolean accepts(String constraint) {
         return constraint.toUpperCase().contains(name());
      }

   }

   public static enum Height implements IsAConstraint<Height> {
      PREFFERED, FILL, FIT, SAME;

      @Override
      public void process(Constraint constraint, Component parent, Component target) {
         constraint.size = constraint.size == null ? target.getPreferredSize() : constraint.size;
         switch (this) {
            case FILL: {
               switch (constraint.y) {
                  case TOP: {
                     constraint.size.setSize(constraint.size.getWidth(), parent.getSize().height - constraint.gapY + constraint.plusHeight);
                     break;
                  }
                  case CENTER: {
                     constraint.size.setSize(constraint.size.getWidth(), (parent.getSize().height / 2) - constraint.gapY + constraint.plusHeight);
                     break;
                  }
               }
               break;
            }
            case FIT: {
               double height = constraint.last.getY() - target.getY() - constraint.last.getHeight() - constraint.gapY + constraint.plusHeight;
               constraint.size.setSize(constraint.size.getWidth(), height);
               break;
            }
            case SAME: {
               constraint.size.setSize(constraint.size.getWidth(), constraint.relative.getSize().height + constraint.plusWidth);
               break;
            }
         }
      }

      @Override
      public boolean accepts(String constraint) {
         return constraint.toUpperCase().contains(name());
      }

   }

   public static final class Constraint {

      private static final List<IsAConstraint<?>> allConstraints = new ArrayList<RelativeLayout.IsAConstraint<?>>();

      static {
         allConstraints.addAll(Arrays.asList(X.values()));
         allConstraints.addAll(Arrays.asList(Y.values()));
         allConstraints.addAll(Arrays.asList(Width.values()));
         allConstraints.addAll(Arrays.asList(Height.values()));
      }

      private Component                           relative;
      private X                                   x              = X.CENTER;
      private Double                              plusX          = 0d;
      private Y                                   y              = Y.CENTER;
      private Double                              plusY          = 0d;
      private Dimension                           size           = null;
      private Point                               location       = new Point(0, 0);
      private Double                              gapX           = null;
      private Double                              gapY           = null;
      private Width                               width          = Width.PREFFERED;
      private Height                              height         = Height.PREFFERED;
      private Double                              plusWidth      = 0d;
      private Double                              plusHeight     = 0d;
      private Component                           last           = null;

      public boolean hasGapX() {
         return this.gapX != null;
      }

      public boolean hasGapY() {
         return this.gapY != null;
      }

      public Constraint gapX(Double gap) {
         this.gapX = gap;
         return this;
      }

      public Constraint gapY(Double gap) {
         this.gapY = gap;
         return this;
      }

      public Constraint size(Width width, Height height) {
         this.width = width;
         this.height = height;
         return this;
      }

      public Constraint width(Width width) {
         return size(width, Height.PREFFERED);
      }

      public Constraint align(X x, Y y) {
         this.x = x;
         this.y = y;
         return this;
      }

      public Constraint relative(Component to) {
         this.relative = to;
         return this;
      }

      public boolean hasRelative() {
         return this.relative != null;
      }

      public Constraint plusX(Double plus) {
         this.plusX = plus;
         return this;
      }

      public Constraint plusX(Integer plus) {
         plusX(plus.doubleValue());
         return this;
      }

      public Constraint plusWidth(Double plus) {
         this.plusWidth = plus;
         return this;
      }

      public Constraint plusWidth(Integer plus) {
         plusWidth(plus.doubleValue());
         return this;
      }

      public Constraint plusHeight(Double plus) {
         this.plusWidth = plus;
         return this;
      }

      public Constraint plusHeight(Integer plus) {
         plusHeight(plus.doubleValue());
         return this;
      }

      public Constraint plusY(Double plus) {
         this.plusY = plus;
         return this;
      }

      public Constraint plusY(Integer plus) {
         plusY(plus.doubleValue());
         return this;
      }

      private Constraint parse(Object constraints) {
         if (constraints instanceof String) {
            bl_Tokens:
            for (String token : ((String) constraints).split(",")) {
               for (IsAConstraint<?> constraint : allConstraints) {
                  if (constraint.accepts(token)) {
                     if (constraint instanceof X) {
                        this.x = (X) constraint;
                     } else if (constraint instanceof Y) {
                        this.y = (Y) constraint;
                     } else if (constraint instanceof X) {
                        this.width = (Width) constraint;
                     } else if (constraint instanceof X) {
                        this.height = (Height) constraint;
                     }
                     continue bl_Tokens;
                  }
               }

               if (token.contains("gapX")) {
                  this.gapX = new Integer(token.split(":")[1]).doubleValue();
               } else if (token.contains("gapY")) {
                  this.gapY = new Integer(token.split(":")[1]).doubleValue();
               } else if (token.contains("plusX")) {
                  this.plusX = new Integer(token.split(":")[1]).doubleValue();
               } else if (token.contains("plusY")) {
                  this.plusY = new Integer(token.split(":")[1]).doubleValue();
               } else if (token.contains("plusWidth")) {
                  this.plusWidth = new Integer(token.split(":")[1]).doubleValue();
               } else if (token.contains("plusHeight")) {
                  this.plusHeight = new Integer(token.split(":")[1]).doubleValue();
               }

            }
            return this;
         }
         return null;
      }

      private void process(Component parent, Component target) {
         width.process(this, parent, target);
         height.process(this, parent, target);
         x.process(this, parent, target);
         y.process(this, parent, target);
         target.setLocation(this.location);
         target.setSize(this.size);
      }

      public Dimension getSize() {
         return this.size;
      }

      public Point getLocation() {
         return this.location;
      }

   }

   private Double     gapX  = 1d;
   private Double     gapY  = 1d;

   private List<Pair> pairs = new ArrayList<RelativeLayout.Pair>();

   public RelativeLayout gapX(Double gap) {
      this.gapX = gap;
      return this;
   }

   public RelativeLayout gapY(Double gap) {
      this.gapY = gap;
      return this;
   }

   @Override
   public void addLayoutComponent(String name, Component comp) {
      throw new RuntimeException("not supported");
   }

   @Override
   public void removeLayoutComponent(Component comp) {
      ArrayList<Pair> toRemove = new ArrayList<Pair>();
      for (Pair pair : pairs) {
         if (pair.component.equals(comp)) {
            toRemove.add(pair);
         }
      }
      pairs.removeAll(toRemove);
   }

   @Override
   public Dimension preferredLayoutSize(Container parent) {
      Dimension preffered = new Dimension();
      Dimension screen = SwingUtilities.getRoot(parent).getSize();
      List<Integer> heights = new ArrayList<Integer>();
      Insets insets = parent.getInsets();
      preProcess(parent);
      int childCount = parent.getComponentCount();
      Dimension size = null;
      for (int child = 0; child < childCount; child++) {
         size = parent.getComponent(child).getPreferredSize();
         preffered.width += size.width;
         heights.add(size.height);
      }
      Collections.sort(heights);
      preffered.height = childCount > 0 ? heights.get(heights.size() - 1) : 0;
      preffered.width -= insets.left + insets.right + gapX - 2;
      preffered.height += insets.bottom + insets.top + gapY - 2;
      screen.width -= insets.left + insets.right + gapX - 2;
      screen.height += insets.bottom + insets.top + gapY - 2;
      return new Dimension(Math.min(preffered.width, screen.width), Math.min(preffered.height, screen.height));
   }

   @Override
   public Dimension minimumLayoutSize(Container parent) {
      return parent.getMinimumSize();
   }

   @Override
   public void layoutContainer(Container parent) {
      synchronized (parent.getTreeLock()) {
         preProcess(parent);
         for (Pair pair : pairs) {
            pair.constraint.process(parent, pair.component);
         }
      }
   }

   private void preProcess(Component parent) {
      Component last = parent;
      for (Pair pair : pairs) {
         pair.constraint.last = last;
         if (!pair.constraint.hasGapX()) {
            pair.constraint.gapX(gapX);
         }
         if (!pair.constraint.hasGapY()) {
            pair.constraint.gapY(gapY);
         }
         if (!pair.constraint.hasRelative()) {
            pair.constraint.relative = last;
         }
         last = pair.component;
      }

   }

   @Override
   public void addLayoutComponent(Component comp, Object constraints) {
      pairs.add(new Pair(comp, cast(constraints)));
   }

   private Constraint cast(Object constraints) {
      if (constraints instanceof Constraint) {
         return (Constraint) constraints;
      }
      return new Constraint().parse(constraints);
   }

   @Override
   public Dimension maximumLayoutSize(Container target) {
      return target.getMaximumSize();
   }

   @Override
   public float getLayoutAlignmentX(Container target) {
      return 0;
   }

   @Override
   public float getLayoutAlignmentY(Container target) {
      return 0;
   }

   @Override
   public void invalidateLayout(Container target) {
   }

   public RelativeLayout gapX(Integer gap) {
      return gapX(gap.doubleValue());
   }

   public RelativeLayout gapY(Integer gap) {
      return gapY(gap.doubleValue());
   }

}
