package Guiao6;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClientNew {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 19876);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            while ((userInput = systemIn.readLine()) != null) {
                try{ 
                    int userInputN=Integer.parseInt(userInput);
                    out.println(userInput);
                    out.flush();
                }catch(NumberFormatException e) {
                    System.out.println("The input is not a valid integer.\n Input a valid integer:");
                    continue;
                }
                

                String response = in.readLine();
                System.out.println("Server response: " + response);
            }

            
            socket.shutdownOutput();//avisa o servidor que o cliente nao vai enviar mais dados,é aqui que o cliente avisa que a mensagem de cima deu null

            String response = in.readLine();
            System.out.println("Media: " + response);

            socket.shutdownInput();
            
          
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
