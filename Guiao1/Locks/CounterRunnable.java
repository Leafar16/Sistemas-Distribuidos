package Guiao1.Locks;
import java.util.concurrent.locks.*;

public class CounterRunnable implements Runnable {
    private static int i=100;
     private static Lock lock=new ReentrantLock();//tem que ser static
    public void run(){
        while(true){lock.lock();
            try{
         if(i>=200) break;
            i++; //area critica,as threads acessam a variavel global i sem nenhum tipo de controlo(caso n haja locks)
            System.out.println(i);
            System.out.println(Thread.currentThread().getName());
        
    }finally{ //este finally garante que uma thread desbloqueia sempre a area critica,aconteca o que acontecer
        lock.unlock();
       }
    }
        
        
    }
    }
 
