package Guiao4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {
    private int total;
    private int max;
    private ReentrantLock lock=new ReentrantLock();
    private Condition c=lock.newCondition();
    
    Barrier (int N) {
        this.total=0;
        this.max=N;
    }
    void await() throws InterruptedException { 
        lock.lock();//como vamos alterar uma variavel global,adquirimos o lock
        try{
            total++;
            while(total<this.max){
                c.await(); //a thread fica suspensa até que um sinal seja enviado
            }
         c.signalAll(); //acorda as threads suspensas
        }finally{
            lock.unlock();
        }
        
     }
    }