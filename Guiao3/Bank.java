package Guiao3;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

class Bank {
    //criacao de um lock
 private static ReentrantLock lockBanco=new ReentrantLock();

    private static class Account {
        private static ReentrantLock lockConta=new ReentrantLock();

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

    // create account and return account id
    public int createAccount(int balance) { //assim protegemos alteracoes no HashMap das contas
        lockBanco.lock();
        try{
            Account c = new Account(balance);
            int id = nextId;
            nextId += 1;
            map.put(id, c);
            return id;
        }finally{
            lockBanco.unlock();
        }
        
    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id) {
        lockBanco.lock();
        try{
            Account c = map.remove(id);
            if (c == null)
                return 0;
            return c.balance();
        }finally{
            lockBanco.unlock();
        }
        
    }

    // account balance; 0 if no such account
    public int balance(int id) {
        map.get(id).lockConta.lock();
        try{
            Account c = map.get(id);
            if (c == null)
                return 0;
            return c.balance();
        }finally{
            map.get(id).lockConta.unlock();
        }
    }

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        map.get(id).lockConta.lock();
        try{
        Account c = map.get(id);
        if (c == null)
            return false;
        return c.deposit(value);
        }finally{
            map.get(id).lockConta.unlock();
        }
    }

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        map.get(id).lockConta.lock();
        try{
        Account c = map.get(id);
        if (c == null)
            return false;
        return c.withdraw(value);
        }finally{
            map.get(id).lockConta.unlock();
        }
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
        map.get(Math.min(to, from)).lockConta.lock(); //to prevent deadlocks
        map.get(Math.max(to, from)).lockConta.lock();
        try{    
            Account cfrom, cto;
            cfrom = map.get(from);
            cto = map.get(to);
            if (cfrom == null || cto ==  null)
                return false;
            return cfrom.withdraw(value) && cto.deposit(value);
        }finally{
            map.get(Math.min(to, from)).lockConta.unlock();
            map.get(Math.max(to, from)).lockConta.unlock();
        }
    }

    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance(int[] ids) {
        for(int y : ids){
            map.get(y).lockConta.lock();
        }
        try{
            int total = 0;
            for (int i : ids) {
                Account c = map.get(i);
                if (c == null)
                    return 0;
                total += c.balance();
            }
            return total;
        }finally{
            for(int y : ids){
                map.get(y).lockConta.unlock();
            }
        }
        
    }

}