package Guiao6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServerNewMult {


    public static void main(String[] args) {
        int total_threads=0;
        try {

            
            ServerSocket ss = new ServerSocket(22222);

            while (true) {
                Socket socket = ss.accept();

                Thread t=new Thread(new Client_handler(socket));
                t.start();
            }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }    
    }

