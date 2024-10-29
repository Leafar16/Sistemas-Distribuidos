package Teste4Janeiro2024;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Manager implements ManagerI {
    int min=0;
    int minMax=0;
    private static ReentrantLock lock=new ReentrantLock();
    Condition c=lock.newCondition();        
    Raid r=new Raid();


    public Raid join(String name, int minPlayers) throws InterruptedException{
        if(minPlayers<=0) throw new InterruptedException();
        lock.lock();
        try{

            minMax=Math.max(minMax,minPlayers);
            r.players().add(name);
            Raid myraid=r;          
            if(minMax == r.players().size()){
                c.signalAll();
                minMax=0;
                r=new Raid();
            }else{
                while (minMax!=r.players().size()){
                    c.await();
                }
            }
        }finally{
            lock.unlock();
        }
        
        return r;
    }
    
}
