package javax.swing.processor.defaults.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.api.ComponentDefinition;
import javax.swing.processor.ActionProcessor;
import javax.swing.register.ActionProcessorRegistra;
import javax.swing.stereotype.Action;
import javax.swing.stereotype.Resource;
import javax.swing.stereotype.scope.ApplicationScoped;

import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.exception.MirrorException;

@Resource
@ApplicationScoped
public class ActionPerformedProcessor implements ActionProcessor {

   @Inject
   private ActionProcessorRegistra registra;

   @Override
   public boolean accepts(Action action,
                          ComponentDefinition definition) {
      return action.listener().equals(ActionListener.class);
   }

   @PostConstruct
   public void init() {
      registra.register(this);
   }

   @Override
   public void process(final Action action,
                       ComponentDefinition definition) {
      final Object component = definition.getTarget();
      final Object parent = definition.getParent();
      try {
         new Mirror().on(component).invoke().method("addActionListener").withArgs(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new Mirror().on(parent).invoke().method(action.method()).withArgs(e);
            }
         });
      } catch (MirrorException error) {
         error.printStackTrace();
      }
   }

}
