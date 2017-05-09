package dao.interfaces;

import dao.entities.Contact;

import java.util.List;

/**
 * Created by andrew_yashin on 5/6/17.
 */
public interface ContactDAO {
    List<Contact> findAll();
    List<Contact> findAllWithDetails();
    List<Contact> findByFirstName(String firstName);
    String findFirstNameById(Long id);
    String findLastNameById(Long id);

    void insert(Contact contact);
    void insertWithDetails(Contact contact);
    void update(Contact contact);
    void delete(Long id);
}
