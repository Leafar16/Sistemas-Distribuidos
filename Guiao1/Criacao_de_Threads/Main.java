package Guiao1.Criacao_de_Threads;

import java.util.ArrayList;

public class Main {
    
    public static void main(String[] args) {
        int N=10;
        var threads=new ArrayList<Thread>();
        for (int i = 0; i < N; i++) {
            threads.add(new Thread(new HelloRunnable()));
        }
        for (Thread thread : threads) {
            thread.start();
        }

        for(Thread  thread : threads){
         try{
            thread.join();

         }catch (InterruptedException e){
            throw new RuntimeException(e);
         }
        }
    }
}
