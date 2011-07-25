package javax.swing.model.table;

import java.lang.reflect.Field;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import net.vidageek.mirror.dsl.Matcher;
import net.vidageek.mirror.dsl.Mirror;

public class EntityTableModel implements TableModel {

   private List<Field> fields;

   @Override
   public void addTableModelListener(TableModelListener l) {
   }

   public <E> EntityTableModel forEntity(Class<E> clazz) {
      fields = new Mirror().on(clazz).reflectAll().fieldsMatching(new Matcher<Field>() {
         @Override
         public boolean accepts(Field field) {
            if (field.getName().equals("serialVersionUID")) {
               return false;
            }
            return true;
         }
      });
      return this;
   }

   @Override
   public Class<?> getColumnClass(int columnIndex) {
      return fields.get(columnIndex).getType();
   }

   @Override
   public int getColumnCount() {
      return fields.size();
   }

   @Override
   public String getColumnName(int columnIndex) {
      return fields.get(columnIndex).getName();
   }

   @Override
   public int getRowCount() {
      return 1000;
   }

   @Override
   public Object getValueAt(int rowIndex,
                            int columnIndex) {
      return columnIndex == 2 ? false : "teste";
   }

   @Override
   public boolean isCellEditable(int rowIndex,
                                 int columnIndex) {
      return true;
   }

   @Override
   public void removeTableModelListener(TableModelListener l) {
   }

   @Override
   public void setValueAt(Object aValue,
                          int rowIndex,
                          int columnIndex) {
      System.out.println(aValue);
   }

}
