package javax.swing.processor;

public interface ComponentProcessor {

   <C> C apply(C component);

   <C> C instanciateOn(C component);

}
