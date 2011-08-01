package javax.swing.model.table;

import java.lang.reflect.Field;
import java.util.List;

import net.vidageek.mirror.dsl.AccessorsController;
import net.vidageek.mirror.dsl.Mirror;

public class ListDataSet implements DataSet {

   private List<?> data;

   public ListDataSet(List<?> data) {
      this.data = data;
   }

   public List<?> getData() {
      return data;
   }

   @Override
   public Integer size() {
      return data.size();
   }

   @Override
   public Object getValueAt(int row, Field field) {
      if (data.size() > row) {
         AccessorsController _row = new Mirror().on(data.get(row));
         try {
            return _row.invoke().getterFor(field);
         } catch (Exception e) {
            return _row.get().field(field);
         }
      }
      return null;
   }

   @Override
   public void setValueAt(Object newValue, int row, Field field) {
      // TODO retirar daqui
      if (data.size() > row) {
         AccessorsController _row = new Mirror().on(data.get(row));
         try {
            _row.invoke().setterFor(field).withValue(newValue);
         } catch (Exception e) {
            _row.set().field(field).withValue(newValue);
         }
      }
   }

   @Override
   public Object get(int row) {
      return data.size() > row ? data.get(row) : null;
   }

}
