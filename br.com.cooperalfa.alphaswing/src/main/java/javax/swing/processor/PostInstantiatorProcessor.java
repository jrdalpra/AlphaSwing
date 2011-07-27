package javax.swing.processor;

public interface PostInstantiatorProcessor {

   <C> boolean accepts(C component);

   <C> void process(C component);

}
