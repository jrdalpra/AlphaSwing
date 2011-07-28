package javax.swing.script;

import javax.swing.stereotype.Resource;
import javax.swing.stereotype.scope.ApplicationScoped;

import net.vidageek.mirror.dsl.AccessorsController;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.exception.MirrorException;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

@Resource
@ApplicationScoped
public class MirrorPropertyAccessor implements PropertyAccessor {

   @Override
   public boolean canRead(EvaluationContext context,
                          Object target,
                          String name) throws AccessException {
      return true;
   }

   @Override
   public boolean canWrite(EvaluationContext context,
                           Object target,
                           String name) throws AccessException {
      return true;
   }

   @SuppressWarnings("rawtypes")
   @Override
   public Class[] getSpecificTargetClasses() {
      return null; // can work with all classes
   }

   @Override
   public TypedValue read(EvaluationContext context,
                          Object target,
                          String name) throws AccessException {
      AccessorsController _target;
      try {
         _target = new Mirror().on(target);
      } catch (Exception e) {
         return new TypedValue(null);
      }
      try {
         return new TypedValue(_target.invoke().getterFor(name));
      } catch (MirrorException e1) {
         try {
            return new TypedValue(_target.get().field(name));
         } catch (MirrorException e2) {
            try {
               return new TypedValue(_target.invoke().method(name).withoutArgs());
            } catch (MirrorException e3) {
               throw new AccessException("Error", e3);
            }
         }
      }
   }

   @Override
   public void write(EvaluationContext context,
                     Object target,
                     String name,
                     Object newValue) throws AccessException {
      AccessorsController _target = new Mirror().on(target);
      try {
         _target.invoke().setterFor(name).withValue(newValue);
      } catch (MirrorException e1) {
         try {
            _target.invoke().method(name).withArgs(newValue);
         } catch (MirrorException e2) {
            try {
               _target.set().field(name).withValue(newValue);
            } catch (MirrorException e3) {
               throw new AccessException("Error", e3);
            }
         }
      }
   }
}