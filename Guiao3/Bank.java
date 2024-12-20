package Guiao3;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Bank {
    //criacao de um lock
 private static ReentrantLock lockBanco=new ReentrantLock();

    private static class Account {
        private  ReentrantLock lockConta=new ReentrantLock();

        private int balance;
        Account(int balance) { this.balance = balance; }
        int balance() { return balance; }
        boolean deposit(int value) {
            balance += value;
            return true;
        }
        boolean withdraw(int value) {
            if (value > balance)
                return false;
            balance -= value;
            return true;
        }
    }

    private Map<Integer, Account> map = new HashMap<Integer, Account>();
    private int nextId = 0;
    private static ReentrantReadWriteLock lockRW=new ReentrantReadWriteLock();//O ReentrantReadWriteLock permite que múltiplas threads façam leituras simultâneas (obtendo o lock de leitura), mas só permite uma única thread para escrita (obtendo o lock de escrita)

    // create account and return account id
    public int createAccount(int balance) { //assim protegemos alteracoes no HashMap das contas
        lockRW.writeLock().lock();
        try{
            Account c = new Account(balance);
            int id = nextId;
            nextId += 1;
            map.put(id, c);
            return id;
        }finally{
            lockRW.writeLock().unlock();
        }
        
    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id) {
        lockRW.writeLock().lock();
        try{
            Account c = map.remove(id);
            if (c == null)
                return 0;
            return c.balance();
        }finally{
            lockRW.writeLock().unlock();
        }
        
    }

    // account balance; 0 if no such account
    public int balance(int id) {
        Account c;
        lockRW.readLock().lock();
        try{
            c=map.get(id);
            if (c==null) return 0;
            c.lockConta.lock();
        }finally{
            lockRW.readLock().unlock();        }
        try{
            return c.balance();
        }finally{
            c.lockConta.unlock();
        }
    }

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        Account c;
        lockRW.readLock().lock();
        try{
        c = map.get(id);
        if (c == null)
            return false;
            c.lockConta.lock();
        }finally{
            lockRW.readLock().unlock();
        }
        try{
            return c.deposit(value);
        }finally{
            c.lockConta.unlock();
        }
    }

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        Account c;
        lockRW.readLock().lock();
        try{
        c = map.get(id);
        if (c == null)
            return false;
            c.lockConta.lock();
        }finally{
            lockRW.readLock().unlock();
        }
        try{
        return c.withdraw(value);
        }finally{
            c.lockConta.unlock();
        }
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
        Account cfrom, cto;
        lockRW.readLock().lock();
        try{ 
            cfrom = map.get(from);
            cto = map.get(to);
            if (cfrom == null || cto ==  null)
                return false;
            if(from>to){
                cfrom.lockConta.lock();
                cto.lockConta.lock();
            }else{
                cto.lockConta.lock();
                cfrom.lockConta.lock();
            }
            }finally{
                lockRW.readLock().unlock();
            }      
                try {
            return cfrom.withdraw(value) && cto.deposit(value);
        }finally{
            cfrom.lockConta.unlock();
            cto.lockConta.unlock();
        }
    }
  

    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance(int[] ids) {
        int total = 0;
        Account[] contas=new Account[ids.length];
        lockRW.readLock().lock();
        try{
            for(int i=0;i<ids.length;i++){
                Account c=map.get(ids[i]);
                if (c==null) return 0;
                contas[i]=c;
                c.lockConta.lock();
            }
            }finally{
                lockRW.readLock().unlock();
            }
            try{
                for(Account c:contas){
                    total += c.balance();

                }
            }finally{
                for(Account c : contas){
                   c.lockConta.unlock();
            }     
            }
            return total;

        }
        
    }
