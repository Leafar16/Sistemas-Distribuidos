package Guiao6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client_handler implements Runnable {
    private Socket socket;
    private static int total_sum;
    private static int total_count;
    private static Lock lock=new ReentrantLock();

    public Client_handler(Socket socket){
        this.socket=socket;
    }
    
    private double getAverage(){
        lock.lock();
        try{
            if(total_count==0){
                return 0;
            }
            return (double) total_sum/total_count;
        }finally{
            lock.unlock();
        }
    }

    private void atualiza_total(int value){
        lock.lock();
        try{
            total_sum +=value;
            total_count++;
        }finally{
            lock.unlock();
        }
    }


    public void run(){

        try{
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                String line;

                int total=0;
                int vezes=0;
                while ((line = in.readLine()) != null) {
                    int nr=Integer.parseInt(line);
                    vezes++;
                    total=total+nr;
                    atualiza_total(nr);
                    out.println(total);
                    out.flush();
                }
                
                out.println((double)total/vezes);
                out.flush();

                socket.shutdownOutput();//avisa o cliente que o servidor nao vai mandar mais dados
                socket.shutdownInput();
                socket.close();
                total=0;
            }catch (IOException e) {
            e.printStackTrace();
        }
    }
 }
    


