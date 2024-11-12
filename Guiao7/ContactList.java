package Guiao7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

class ContactList extends ArrayList<Contact> { //é uma ArrayList<Contactos>


    // @TODO
    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(this.size());
        for (Contact contact : this) {
            contact.serialize(out);
        }
    }

    // @TODO
    public static ContactList deserialize(DataInputStream in) throws IOException {
        ContactList contactList = new ContactList();
        int length= in.readInt();
        for (int i = 0; i < length; i++) {
            contactList.add(Contact.deserialize(in));
        }
        return contactList;
    }

}