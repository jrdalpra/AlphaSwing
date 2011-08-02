package javax.swing.bind;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.DocumentEvent;
import javax.swing.stereotype.Bind;

import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.invoke.dsl.InvocationHandler;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class Binder {

   private List<Bind>              bindables;
   private static SpelExpressionParser parser            = new SpelExpressionParser();

   private Map<Bind, Expression>   expressions       = new HashMap<Bind, Expression>();
   private Map<Bind, Expression>   targetExpressions = new HashMap<Bind, Expression>();
   private InvocationHandler<Object>   _target;
   private EvaluationContext           context;
   private Object                      target;
   private Method                      method;

   public Binder() {
   }

   public void apply(Object root,
                     Object event) {
      Object _old = null, _new = null;
      if (isSource(event)) {
         return;
      }
      for (Bind bindable : expressions.keySet()) {
         try {
            _old = targetExpressions.get(bindable).getValue(this.method == null ? target : _target.method(method).withoutArgs());
            _new = expressions.get(bindable).getValue(context);
            if (_new != null && (_old == null || !_old.equals(_new))) {
               targetExpressions.get(bindable).setValue(this.method == null ? target : _target.method(method).withoutArgs(), _new);
            }
         } catch (Exception e) {
         }
      }
   }

   public Binder bindables(List<Bind> bindables) {
      this.bindables = bindables;
      return this;
   }

   public Binder context(EvaluationContext context) {
      this.context = context;
      return this;
   }

   private Boolean isSource(Object event) {
      if (event == null) {
         return false;
      }
      if (DocumentEvent.class.isInstance(event)) {
         Integer contador = (Integer) DocumentEvent.class.cast(event).getDocument().getProperty("contador");
         for (int i = 0; i < contador; i++) {
            if (DocumentEvent.class.cast(event).getDocument().getProperty("source" + (i + 1)) == target) {
               return true;
            }
         }
      }
      return false;
   }

   public Binder method(Method method) {
      this.method = method;
      return this;
   }

   public Binder process() {
      for (Bind bindable : bindables) {
         expressions.put(bindable, parser.parseExpression(bindable.value()));
         targetExpressions.put(bindable, parser.parseExpression(bindable.property()));
      }
      return this;
   }

   public Binder target(Object target) {
      this.target = target;
      _target = new Mirror().on(target).invoke();
      return this;
   }
}