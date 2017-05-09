import dao.PlainContactDAO;
import dao.entities.Contact;
import dao.interfaces.ContactDAO;

import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        PlainContactDAO contactDAO = new PlainContactDAO();

        print(contactDAO);

        Contact contact = new Contact();
        contact.setFirst_name("Andrew");
        contact.setSecond_name("Buchkov");
        contact.setBirth_date(new Date(
                new GregorianCalendar(2001,2,12).getTimeInMillis()
        ));

        contactDAO.insert(contact);
        print(contactDAO);

        contactDAO.delete(7l);
        print(contactDAO);


    }

    static void print(ContactDAO contactDAO){
        List<Contact> list = contactDAO.findAll();
        list.forEach(System.out::println);
    }
}
