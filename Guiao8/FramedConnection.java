package Guiao8;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class FramedConnection implements AutoCloseable {
    private Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;

    private static ReentrantLock sendLock=new ReentrantLock(); // utilizando dois locks diferentes,podemos ler enquanto escrevemos
    private static ReentrantLock receiveLock=new ReentrantLock();


    public FramedConnection(Socket socket) throws IOException {
        this.socket=socket;
        this.input=new DataInputStream(socket.getInputStream());
        this.output=new DataOutputStream(socket.getOutputStream());
     }

     public void send(byte[] data) throws IOException {
        sendLock.lock();
        try{
            output.writeInt(data.length);
            output.write(data);
            output.flush();
        }finally{
            sendLock.unlock();
        }
     }

     public byte[] receive() throws IOException {
        receiveLock.lock();
        try{
            int lenght=input.readInt();
            var data=new byte[lenght];
            input.readFully(data);
            return data;
        }finally{
            receiveLock.unlock();
        }
      }

        public void close() throws IOException {
        try{
            input.close();
        }finally{
            try{
                output.close();
            }finally{
                socket.close();
            }
        }
            
     }
    
}