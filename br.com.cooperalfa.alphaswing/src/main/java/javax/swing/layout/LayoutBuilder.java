package javax.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;

public interface LayoutBuilder<L extends LayoutManager, T extends LayoutBuilder<L, T>> {

   T build();

   L getLayout();

   Container getTarget();

   T on(Container target);

   T with(Component... children);

}
