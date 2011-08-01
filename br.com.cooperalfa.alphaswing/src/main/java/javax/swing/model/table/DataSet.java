package javax.swing.model.table;

import java.lang.reflect.Field;

public interface DataSet {

   Integer size();

   Object getValueAt(int row, Field field);

   void setValueAt(Object newValue, int row, Field field);

   Object get(int row);

}
