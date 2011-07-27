package javax.swing.processor.defaults.post.instantiation;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.processor.PostInstantiatorProcessor;
import javax.swing.register.PostInstantiatonProcessorRegistra;
import javax.swing.stereotype.Resource;
import javax.swing.text.JTextComponent;

@Resource
public class JTextFieldFocusPostInstantiationProcessor implements PostInstantiatorProcessor, FocusListener {

   @Inject
   private PostInstantiatonProcessorRegistra registra;

   @Override
   public <C> boolean accepts(C component) {
      return JTextComponent.class.isInstance(component);
   }

   @Override
   public void focusGained(FocusEvent e) {
      // TODO parameter or component
      JTextComponent.class.cast(e.getSource()).setBackground(Color.GREEN.brighter().brighter());
   }

   @Override
   public void focusLost(FocusEvent e) {
      // TODO parameter or component
      JTextComponent.class.cast(e.getSource()).setBackground(Color.WHITE);
   }

   @PostConstruct
   public void init() {
      this.registra.register(this);
   }

   @Override
   public <C> void process(C component) {
      // TODO parameter or component
      JTextComponent.class.cast(component).setBackground(Color.WHITE);
      JTextComponent.class.cast(component).addFocusListener(this);
   }

}
