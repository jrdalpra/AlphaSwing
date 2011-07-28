package javax.swing.model.combobox;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class ListComboBoxModel<T> implements ComboBoxModel {

   private List<T>                dados;

   private List<ListDataListener> listeners = new ArrayList<ListDataListener>();

   private T                      selecionado;

   public ListComboBoxModel() {
   }

   @Override
   public void addListDataListener(ListDataListener l) {
      listeners.add(l);
   }

   public ListComboBoxModel<T> com(List<T> dados) {
      this.dados = dados;
      return this;
   }

   @Override
   public T getElementAt(int index) {
      return this.dados.get(index);
   }

   @Override
   public T getSelectedItem() {
      return this.selecionado;
   }

   @Override
   public int getSize() {
      return this.dados.size();
   }

   @Override
   public void removeListDataListener(ListDataListener l) {
      listeners.remove(l);
   }

   public ListComboBoxModel<T> selecionado(T selecionado) {
      this.selecionado = selecionado;
      return this;
   }

   @SuppressWarnings("unchecked")
   @Override
   public void setSelectedItem(Object anItem) {
      this.selecionado = (T) anItem;
   }

}
