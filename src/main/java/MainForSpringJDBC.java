import dao.entities.Contact;
import dao.entities.ContactTelDetail;
import dao.interfaces.ContactDAO;
import dao.queries.ContactQuery;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class MainForSpringJDBC {
    public static void main(String[] args) {
        ApplicationContext context =
                new GenericXmlApplicationContext("config_datasource.xml");

//        ContactDAO contactDAO = (ContactDAO) context.getBean("contact");
//        System.out.println(contactDAO.findFirstNameById(1L));
//        System.out.println(contactDAO.findAll());
//        System.out.println(contactDAO.findAllWithDetails());
        ContactDAO contactDAO = (ContactDAO) context.getBean("contactDAO");
        System.out.println(contactDAO.findAll());

        List<Contact> contacts = contactDAO.findByFirstName("Andrew");
        for(Contact contact: contacts){
            contact.setFirst_name("Andrii");
            contactDAO.update(contact);
        }

        System.out.println(contactDAO.findByFirstName("Andrii"));

        Contact contact =  new Contact();
        contact.setFirst_name("Sasha");
        contact.setSecond_name("Rudyka");
        contact.setBirth_date(
                new Date(new GregorianCalendar(1996,6,6).getTimeInMillis()));

        ContactTelDetail detail1 = new ContactTelDetail();
        detail1.setTel_type("home");
        detail1.setTel_number("123123");

        List<ContactTelDetail> contactTelDetails = contact.getDetails();
        if(contactTelDetails == null){
            contact.setDetails(new ArrayList<>());
            contactTelDetails = contact.getDetails();
        }

        contactTelDetails.add(detail1);

        contactDAO.insertWithDetails(contact);


    }


}
