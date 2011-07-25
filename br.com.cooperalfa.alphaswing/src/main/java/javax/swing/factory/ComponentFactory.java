package javax.swing.factory;

public interface ComponentFactory<T> {

   boolean canProvide(Class<T> type);

   T provide(Class<T> type);

}
