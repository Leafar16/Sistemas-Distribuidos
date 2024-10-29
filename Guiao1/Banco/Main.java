package Guiao1.Banco;
import java.util.ArrayList;

public class Main {
    
 public static class BankRunner implements Runnable{
  int L=1000;
  Bank bank;
  BankRunner(Bank bank){
   this.bank=bank;
  }

  public void run(){
  for(int i=0;i<L;i++){
   bank.deposit(100);
   System.out.println(bank.balance());
   
   }
  }
 }
    public static void main(String[] args) {
        int N=10;
        var threads=new ArrayList<Thread>();
        var bank=new Bank();

        for(int i=0;i<N;i++){
         threads.add(new Thread(new BankRunner(bank)));
        }

        for(Thread t: threads){
            t.start();
        }

        for(Thread t: threads){
        try {
            t.join();
        } catch (Exception e) {
            System.out.println("error");
        }
            
        }
    }
}
