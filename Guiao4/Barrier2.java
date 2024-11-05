package Guiao4;
//Implementacao de uma barreira reutilizavel
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier2 {
    private int total;
    private int ciclo;//ao utilizar ciclos tornamos esta barreira reutilizavel
    private int max;
    private ReentrantLock lock=new ReentrantLock();
    private Condition c=lock.newCondition();
    
    Barrier2 (int N) {
        this.total=0;
        this.ciclo=0;
        this.max=N;
    }

    void await() throws InterruptedException { 
        lock.lock();//como vamos alterar uma variavel global,adquirimos o lock
        try{
            int current_ciclo=ciclo;
            total++;
            if(total<this.max){
                while(current_ciclo==ciclo){
                    c.await(); //a thread fica suspensa até que um sinal seja enviado
                }
            } else{
                c.signalAll(); //acorda as threads suspensas
                total=0;
                ciclo++;
            }          
        }finally{
            lock.unlock();
        }
     }

    }
