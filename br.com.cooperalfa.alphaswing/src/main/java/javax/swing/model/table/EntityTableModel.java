package javax.swing.model.table;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelListener;
import javax.swing.stereotype.Ignore;
import javax.swing.table.AbstractTableModel;

import net.vidageek.mirror.dsl.Matcher;
import net.vidageek.mirror.dsl.Mirror;

@SuppressWarnings("serial")
public class EntityTableModel extends AbstractTableModel {

   public static interface IgnoreOnModel {
   }

   private static Map<Class<?>, Class<?>> wrappers = new HashMap<Class<?>, Class<?>>();

   static {
      wrappers.put(int.class, Integer.class);
      wrappers.put(float.class, Float.class);
      // TODO remover daqui e add outros
   }

   private List<Field>                    fields;
   private DataSet                        dataset;

   @Override
   public void addTableModelListener(TableModelListener l) {
      super.addTableModelListener(l);
   }

   public EntityTableModel dataset(DataSet dataset) {
      this.dataset = dataset;
      return refresh();
   }

   public EntityTableModel except(String... except) {
      ArrayList<Field> toRemove = new ArrayList<Field>();
      List<String> exceptList = Arrays.asList(except);
      for (Field field : fields) {
         if (exceptList.contains(field.getName())) {
            toRemove.add(field);
         }
      }
      fields.removeAll(toRemove);
      return this;
   }

   public <E> EntityTableModel forEntity(Class<E> clazz) {
      fields = new Mirror().on(clazz).reflectAll().fieldsMatching(new Matcher<Field>() {
         @Override
         public boolean accepts(Field field) {
            if (Modifier.isStatic(field.getModifiers()) || field.isAnnotationPresent(Ignore.class)) {
               return false;
            }
            return true;
         }
      });
      return this;
   }

   @Override
   public Class<?> getColumnClass(int columnIndex) {
      Class<?> clazz = fields.get(columnIndex).getType();
      if (clazz.isPrimitive()) {
         return wrapperFor(clazz);
      }
      return clazz;
   }

   private Class<?> wrapperFor(Class<?> clazz) {
      return wrappers.get(clazz);
   }

   @Override
   public int getColumnCount() {
      return fields.size();
   }

   @Override
   public String getColumnName(int columnIndex) {
      // TODO anotações ou uma classe que retorne os nomes
      return fields.get(columnIndex).getName();
   }

   @Override
   public int getRowCount() {
      return dataset.size();
   }

   @Override
   public Object getValueAt(int row,
                            int column) {
      return dataset.getValueAt(row, fields.get(column));
   }

   @Override
   public boolean isCellEditable(int rowIndex,
                                 int columnIndex) {
      // TODO anotacoes ou classe que diga sobre edicao ou não da columna
      return false;
   }

   @Override
   public void removeTableModelListener(TableModelListener l) {
      super.removeTableModelListener(l);
   }

   @Override
   public void setValueAt(Object newValue,
                          int row,
                          int column) {
      try {
         dataset.setValueAt(newValue, row, fields.get(column));
         fireTableCellUpdated(row, column);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public DataSet getDataset() {
      return dataset;
   }

   public EntityTableModel refresh() {
      fireTableDataChanged();
      return this;
   }

}
