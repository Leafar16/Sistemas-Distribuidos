package Guiao2;
import java.util.concurrent.locks.ReentrantLock;

public class BancoMultiplasContas {

    private static class Account {
        private int balance;
        private static ReentrantLock lock=new ReentrantLock(); //ao usarmos um lock para cada conta permitimos 
        //que elas acessem diferentes objetos,em vez de bloquearmos o banco sempre que efetuamos uma operacao 
       
        Account (int balance) { this.balance = balance; }
        int balance () { return balance; }
        boolean deposit (int value) {
            balance += value;
            return true;
        }
        boolean withdraw (int value) {
            if (value > balance)
                return false;
            balance -= value;
            return true;
        }
    }

    // Bank slots and vector of accounts
    private final int slots;
    private Account[] av;

    public BancoMultiplasContas (int n) {
        slots=n;
        av=new Account[slots];
        for (int i=0; i<slots; i++)
            av[i]=new Account(0);
    }


    // Account balance
    public int balance (int id) {
        if (id < 0 || id >= slots)
            return 0;
        return av[id].balance();
    }

    // Deposit
    public boolean deposit (int id, int value) {
        if (id < 0 || id >= slots)
            return false;
        return av[id].deposit(value);
    }

    // Withdraw; fails if no such account or insufficient balance
    public boolean withdraw (int id, int value) {
        if (id < 0 || id >= slots)
            return false;

            av[id].lock.lock();
            try{
             return av[id].withdraw(value);
            }finally{
                av[id].lock.unlock();
            }
    }

    // Transfer
    public boolean transfer (int from, int to, int value) {
            if(from<0 || to<0 || from>slots || to>slots){
                return false;
        }
    
        av[Math.min(from,to)].lock.lock(); //faz com que iniciamos na mesma ordem,impedindo deadlocks
        av[Math.max(from,to)].lock.lock(); 
        try{
            if(!withdraw(from,value)){
                return false;
            }
            return deposit(to,value);
        }finally{
            av[from].lock.unlock();
            av[to].lock.unlock();
        }
            
     }    

    // TotalBalance
        public int totalBalance () { 

            for(int i=0;i<slots;i++){
                av[i].lock.lock();
            }
    
            try{
                int total=0;
                for(int i=0;i<slots;i++){
                    total+=balance(i);
               }
               return total;
            }finally{
                 for(int i=0;i<slots;i++){
                av[i].lock.unlock();
            }
            }
     }
    }

