package Guiao5;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Warehouse {
    private Map<String, Product> map =  new HashMap<String, Product>(); 
    private static ReentrantLock lockWareHouse=new ReentrantLock();
    
    private class Product { 
        private Condition c=lockWareHouse.newCondition();
        int quantity = 0; }

    private Product get(String item) {
        lockWareHouse.lock();
        try{
            Product p = map.get(item);
            if (p != null) return p;
            p = new Product();
            map.put(item, p);
            return p;
        }finally{
            lockWareHouse.unlock();
        }
        
    }

    public void supply(String item, int quantity) throws InterruptedException{    
        lockWareHouse.lock();
        try{
            Product p = get(item);
            p.quantity += quantity;
            p.c.signalAll();
        }finally{
            lockWareHouse.unlock();
        }
        
    }

    // Errado se faltar algum produto...
    public void consume(Set<String> items) throws InterruptedException{
        lockWareHouse.lock();
        try{
            for (String s : items){
            Product p=get(s);
            while(p.quantity==0){
                p.c.await();
            }
            p.quantity--;
            } 
        }finally{
            lockWareHouse.unlock();
        }
    }

}