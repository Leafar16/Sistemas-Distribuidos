package Guiao6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServerNew {
    public static void main(String[] args) {
        try {
            int total=0;
            int vezes=0;
            ServerSocket ss = new ServerSocket(19876);

            while (true) {
                Socket socket = ss.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                String line;
                while ((line = in.readLine()) != null) {
                    int nr=Integer.parseInt(line);
                    vezes++;
                    total=total+nr;
                    out.println(total);
                    out.flush();
                }
                
                out.println((double)total/vezes);
                out.flush();

                socket.shutdownOutput();//avisa o cliente que o servidor nao vai mandar mais dados
                socket.shutdownInput();
                socket.close();
                total=0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
