package Guiao7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

class Contact {
    private String name;
    private int age;
    private long phoneNumber;
    private String company;     // Pode ser null
    private ArrayList<String> emails;

    public Contact(String name, int age, long phoneNumber, String company, List<String> emails) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.company = company;
        this.emails = new ArrayList<>(emails);
    }

    public String name() { return name; }
    public int age() { return age; }
    public long phoneNumber() { return phoneNumber; }
    public String company() { return company; }
    public List<String> emails() { return new ArrayList(emails); }

    // @TODO
    public void serialize(DataOutputStream out) throws IOException { //Serialização é o processo de transformar um objeto ou estrutura de
                                                                     // dados em um formato que pode ser facilmente armazenado ou transmitido e,
                                                                     //posteriormente, reconstruído no seu estado original.
     out.writeUTF(name);
     out.writeInt(age);
     out.writeLong(phoneNumber);

     if (company!=null) {// company pode ser null
         out.writeBoolean(true);
         out.writeUTF(company);
     }else{
         out.writeBoolean(false);
     }

     out.writeInt(emails.size());//escreve o tamanho do array,para depois podermos lê-lo e saber qts emails existem
     for( String x : emails){
            out.writeUTF(x);
        }
    }

    // @TODO
    public static Contact deserialize(DataInputStream in) throws IOException {
        String name=in.readUTF();
        int age= in.readInt();
        long phoneNumber=in.readLong();

        String company=null;
        if (in.readBoolean()){
            company=in.readUTF();
        }

        int length_emails=in.readInt();
        List<String> emails=new ArrayList<>();
        for (int i = 0; i < length_emails; i++) {
            String email= in.readUTF();
            emails.add(email);
        }

        return new Contact(name,age,phoneNumber,company,emails); //montar o objeto
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name).append(";");
        builder.append(this.age).append(";");
        builder.append(this.phoneNumber).append(";");
        builder.append(this.company).append(";");
        builder.append(this.emails.toString());
        builder.append("}");
        return builder.toString();
    }

}