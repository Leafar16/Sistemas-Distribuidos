package Guiao1.Locks;
import java.util.concurrent.locks.*;;

public class CounterRunnable implements Runnable {
    private static int i=100;
     private static Lock lock=new ReentrantLock();
    public void run(){
       lock.lock();
       try{
        while(i<200){
            i++; //area critica,as threads acessam a variavel global i sem nenhum tipo de controlo(caso n haja locks)
            System.out.println(i);
        }
       }finally{ //este finally garante que uma thread desbloqueia sempre a area critica,aconteca o que acontecer
        lock.unlock();
       }
        
    }
}
