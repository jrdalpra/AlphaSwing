package javax.swing.processor.defaults.property;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.api.ComponentDefinition;
import javax.swing.stereotype.Resource;

import net.vidageek.mirror.dsl.Matcher;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.get.dsl.GetterHandler;

@Resource
public class DefaultConstantPropertyAnnotationProcessor extends AbstractPropertyAnnotationProcessor implements WindowConstants {

   private class StaticField implements Matcher<Field> {
      @Override
      public boolean accepts(Field element) {
         return Modifier.isStatic(element.getModifiers());
      }
   }

   public Map<String, Object> properties = new HashMap<String, Object>();

   public DefaultConstantPropertyAnnotationProcessor() {
      getConstants();
      properties.put("SINGLE_SELECTION", ListSelectionModel.SINGLE_SELECTION);
      properties.put("SINGLE", ListSelectionModel.SINGLE_SELECTION);
      properties.put("MULTIPLE_INTERVAL_SELECTION", ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      properties.put("MULTIPLE", ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      properties.put("SINGLE_INTERVAL_SELECTION", ListSelectionModel.SINGLE_INTERVAL_SELECTION);
      properties.put("SINGLE_INTERVAL", ListSelectionModel.SINGLE_INTERVAL_SELECTION);
   }

   @Override
   public <A extends Annotation> boolean accepts(A annotation,
                                                 ComponentDefinition definition) {
      return super.accepts(annotation, definition) && properties.containsKey(cast(annotation).value());
   }

   private void getConstants() {
      List<Field> statics = new Mirror().on(getClass()).reflectAll().fieldsMatching(new StaticField());
      GetterHandler _this = new Mirror().on(this).get();
      for (Field field : statics) {
         properties.put(field.getName(), _this.field(field));
      }
   }

   @Override
   public <A extends Annotation> void process(A annotation,
                                              ComponentDefinition definition) {
      process(cast(annotation), definition.getTarget(), properties.get(cast(annotation).value()));
   }

}
