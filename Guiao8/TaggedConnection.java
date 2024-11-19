package Guiao8;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class TaggedConnection implements AutoCloseable {
    
    public static class Frame {
    
        public final int tag;
    public final byte[] data;
    
    public Frame(int tag, byte[] data) { 
        this.tag = tag; this.data = data; 
    }

    }
    private static ReentrantLock sendLock=new ReentrantLock();
    private static ReentrantLock receiveLock=new ReentrantLock();
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public TaggedConnection(Socket socket) throws IOException {
        this.socket=socket;
        this.input=new DataInputStream(socket.getInputStream());
        this.output=new DataOutputStream(socket.getOutputStream());
     }

     public void send(Frame frame) throws IOException {
            send(frame.tag,frame.data);
     }
     
     public void send(int tag, byte[] data) throws IOException {
        sendLock.lock();
        try{
            output.writeInt(tag);
            output.writeInt(data.length);
            output.write(data);
            output.flush();
        }finally{
            sendLock.unlock();
        }
     }    
     
     public Frame receive() throws IOException {
        receiveLock.lock();
        try{
            int tag=input.readInt();
            int length=input.readInt();
            var data=new byte [length];
            input.readFully(data);

            Frame frame=new Frame(tag,data);
            return frame;
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