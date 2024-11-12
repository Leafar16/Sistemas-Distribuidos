package Guiao7;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Arrays.asList;

class ContactManager {
    private HashMap<String, Contact> contacts = new HashMap<>();
    private ReentrantLock lock=new ReentrantLock();
    // @TODO
    public void update(Contact c) {
    lock.lock();
    try{
        contacts.put(c.name(),c);
    }finally {
        lock.unlock();
    }
    }

    // @TODO
    public ContactList getContacts() {
        lock.lock();
        var contactos = new ContactList();
        try {
            for (Contact value : contacts.values()) {
                contactos.add(value);
            }
            return contactos;
        }finally{
            lock.unlock();
        }
    }
}

class ServerWorker implements Runnable {
    private Socket socket;
    private ContactManager manager;

    public ServerWorker(Socket socket, ContactManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    // @TODO
    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream output=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            ContactList list=manager.getContacts();
            list.serialize(output);
            output.flush();

            boolean done = false;
            while (!done) {
                try {
                    Contact c = Contact.deserialize(in);// se não conseguir ler->EndOfFileExcepction
                    manager.update(c);
                } catch (EOFException e) {//
                    done = true;
                }
            }

            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



public class Server {

    public static void main (String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        ContactManager manager = new ContactManager();
        // example pre-population
        manager.update(new Contact("John", 20, 253123321, null, asList("john@mail.com")));
        manager.update(new Contact("Alice", 30, 253987654, "CompanyInc.", asList("alice.personal@mail.com", "alice.business@mail.com")));
        manager.update(new Contact("Bob", 40, 253123456, "Comp.Ld", asList("bob@mail.com", "bob.work@mail.com")));

        while (true) {
            Socket socket = serverSocket.accept();
            Thread worker = new Thread(new ServerWorker(socket, manager));
            worker.start();
        }
    }

}