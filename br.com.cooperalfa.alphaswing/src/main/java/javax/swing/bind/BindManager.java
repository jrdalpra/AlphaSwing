package javax.swing.bind;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.swing.api.ComponentDefinition;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.script.MirrorPropertyAccessor;
import javax.swing.stereotype.BindGroup;
import javax.swing.stereotype.Bindable;
import javax.swing.stereotype.Resource;
import javax.swing.stereotype.scope.PrototypeScoped;
import javax.swing.text.JTextComponent;
import javax.swing.thread.ThreadPool;

import net.vidageek.mirror.dsl.Matcher;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.get.dsl.GetterHandler;
import net.vidageek.mirror.invoke.dsl.InvocationHandler;

import org.springframework.context.annotation.Lazy;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Resource
@PrototypeScoped
@Lazy(false)
public class BindManager implements PropertyChangeListener, DocumentListener, ActionListener, FocusListener, ChangeListener, MouseListener {

   private ThreadPool                pool               = new ThreadPool(5);

   @Inject
   private MirrorPropertyAccessor    acessor;

   private Object                    target;
   private List<Binder>              binders            = new ArrayList<Binder>();
   private Object                    root;
   private StandardEvaluationContext context;
   private Object                    lock               = new Object();

   private List<String>              addListenerMethods = new ArrayList<String>();

   public BindManager() {
      addListenerMethods.add("addActionListener");
      addListenerMethods.add("addFocusListener");
      addListenerMethods.add("addPropertyChangeListener");
      addListenerMethods.add("addChangeListener");
      addListenerMethods.add("addMouseListener");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      process(e);
   }

   public boolean add(Binder e) {
      return binders.add(e.context(context));
   }

   public BindManager apply(ComponentDefinition definition) {
      this.target = definition.getTarget();
      this.root = definition.getParent();
      GetterHandler _get = new Mirror().on(this.target).get();
      if (context == null) {
         context = new StandardEvaluationContext(this.root);
         context.addPropertyAccessor(this.acessor);
      }
      for (Field field : new Mirror().on(this.target.getClass()).reflectAll().fields()) {
         applyListenersOn(_get.field(field));
         if (_get.field(field) != null) {
            add(new Binder().target(_get.field(field)).bindables(bindablesOf(field)).context(this.context).process());
         }
      }
      for (Method method : new Mirror().on(this.target.getClass()).reflectAll().methods()) {
         if (method.isAnnotationPresent(Bindable.class) || method.isAnnotationPresent(BindGroup.class)) {
            add(new Binder().target(this.target).method(method).bindables(bindablesOf(method)).context(context).process());
         }
      }
      applyListenersOn(this.target);
      process(null);
      return this;
   }

   private <C> void applyListenersOn(C component) {
      if (component == null) {
         return;
      }
      InvocationHandler<Object> _invoke = new Mirror().on(component).invoke();
      for (String addListener : addListenerMethods) {
         try {
            _invoke.method(addListener).withArgs(this);
         } catch (Exception e) {
         }
      }
      if (JTextComponent.class.isInstance(component)) {
         Object _contador = JTextComponent.class.cast(component).getDocument().getProperty("contador");
         if (_contador == null) {
            _contador = 0;
         }
         Integer contador = ((Integer) _contador) + 1;
         JTextComponent.class.cast(component).getDocument().putProperty("contador", contador);
         JTextComponent.class.cast(component).getDocument().putProperty("source" + contador.intValue(), component);
         JTextComponent.class.cast(component).getDocument().addDocumentListener(this);
      }
   }

   private List<Bindable> bindablesOf(Field field) {
      List<Annotation> annotations = new Mirror().on(field).reflectAll().annotationsMatching(new Matcher<Annotation>() {
         @Override
         public boolean accepts(Annotation element) {
            return element.annotationType().equals(BindGroup.class)
                                                                                                            || element.annotationType().equals(Bindable.class);
         }
      });
      List<Bindable> result = new ArrayList<Bindable>(annotations.size());
      for (Annotation annotation : annotations) {
         if (annotation.annotationType().equals(Bindable.class)) {
            result.add(Bindable.class.cast(annotation));
         } else {
            result.addAll(Arrays.asList(BindGroup.class.cast(annotation).value()));
         }
      }
      return result;
   }

   private List<Bindable> bindablesOf(Method method) {
      List<Annotation> annotations = new Mirror().on(method).reflectAll().annotationsMatching(new Matcher<Annotation>() {
         @Override
         public boolean accepts(Annotation element) {
            return element.annotationType().equals(BindGroup.class)
                                                                                                             || element.annotationType().equals(Bindable.class);
         }
      });
      List<Bindable> result = new ArrayList<Bindable>(annotations.size());
      for (Annotation annotation : annotations) {
         if (annotation.annotationType().equals(Bindable.class)) {
            result.add(Bindable.class.cast(annotation));
         } else {
            result.addAll(Arrays.asList(BindGroup.class.cast(annotation).value()));
         }
      }
      return result;
   }

   @Override
   public void changedUpdate(DocumentEvent e) {
      process(e);
   }

   @Override
   @PreDestroy
   protected void finalize() throws Throwable {
      pool.shutdown();
      super.finalize();
   }

   @Override
   public void focusGained(FocusEvent e) {
      process(e);
   }

   @Override
   public void focusLost(FocusEvent e) {
      process(e);
   }

   protected synchronized List<Binder> getBinders() {
      return new ArrayList<Binder>(binders);
   }

   @Override
   public void insertUpdate(DocumentEvent e) {
      process(e);
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      process(e);
   }

   @Override
   public void mouseEntered(MouseEvent e) {
      process(e);
   }

   @Override
   public void mouseExited(MouseEvent e) {
      process(e);
   }

   @Override
   public void mousePressed(MouseEvent e) {
      process(e);
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      process(e);
   }

   public BindManager process(final Object e) {
      pool.execute(new Runnable() {
         @Override
         public void run() {
            for (Binder binder : getBinders()) {
               binder.apply(root, e);
            }
         }
      });
      return this;
   }

   @Override
   public void propertyChange(PropertyChangeEvent e) {
      process(e);
   }

   @Override
   public void removeUpdate(DocumentEvent e) {
      process(e);
   }

   @Override
   public void stateChanged(ChangeEvent e) {
      process(e);
   }
}
