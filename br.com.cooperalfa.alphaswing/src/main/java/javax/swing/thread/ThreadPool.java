package javax.swing.thread;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ThreadPool {

   /**
    * Classe worker, a thread que efetivamente executa os jobs no pool.
    * 
    * @author Ronald Tetsuo Miura - Mirante Inform�tica
    */
   class Worker extends Thread {
      public Worker() {
         setDaemon(true);
         start();
      }

      public void run() {
         // repete at� que o pool seja marcado para shutdown
         while (!_shutdown) {
            Runnable job;
            synchronized (LOCK) {
               // espera at� algum job estar dispon�vel na fila
               while (_jobs.isEmpty()) {
                  try {
                     // se n�o tem, libera o lock e espera
                     LOCK.wait();
                  } catch (InterruptedException e) {
                     e.printStackTrace();
                  }
               }
               // pega o primeiro job da fila (note que ainda estou dentro do
               // bloco synchronized)
               job = _jobs.removeFirst();
            }

            // fora do bloco synchronized, sen�o trava todas as threads at� o
            // fim do job
            // executa job
            job.run();
         }
      }
   }

   /** Flag de shutdown. */
   boolean                             _shutdown = false;

   /** Fila de jobs. */
   LinkedList<Runnable>                _jobs     = new LinkedList<Runnable>();

   /** Lista de threads no pool. */
   private List<WeakReference<Worker>> _threads  = new ArrayList<WeakReference<Worker>>();

   /**
    * Lock artificial. Evita que outras threads externas fiquem em wait no lock.
    * Eu poderia ter usado _jobs para isso, mas acho que assim fica mais
    * organizado.
    */
   final Object                        LOCK      = new Object();

   public ThreadPool(int size) {
      for (int i = 0; i < size; i++) {
         this._threads.add(new WeakReference<Worker>(new Worker()));
      }
   }

   /**
    * Adiciona o job � fila e avisa uma thread (em wait) de que h� jobs na fila.
    * Se n�o houver nenhuma thread em wait, a pr�xima que terminar o servi�o
    * pegar� automaticamente o pr�ximo job.
    * 
    * @param job
    */
   public void execute(Runnable job) {
      if (_shutdown) {
         throw new IllegalStateException("Pool marcado para shutdown");
      }
      synchronized (LOCK) {
         _jobs.addLast(job);
         LOCK.notify(); // notifica UMA thread em wait
      }
   }

   /**
    * Diz pro pool para parar de aceitar jobs e encerrar todas as suas threads.
    */
   public void shutdown() {
      // Seta a flag de shutdown e avisa todas as threads dormentes para
      // acordarem.
      // Assim, elas ver�o a flag true e sair�o do loop
      synchronized (LOCK) {
         _shutdown = true;
         _jobs.clear(); // descarta jobs n�o executados
         LOCK.notifyAll(); // notifica TODAS as thread em wait
      }

      // Espera por cada thread morrer
      Iterator<WeakReference<Worker>> it = _threads.iterator();
      while (it.hasNext()) {
         Thread t = it.next().get();
         try {
            t.join();
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }
}
