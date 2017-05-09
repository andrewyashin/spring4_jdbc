package dao.queries;

import dao.entities.Contact;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectAllContacts extends MappingSqlQuery<Contact> {

    public SelectAllContacts(DataSource ds) {
        super(ds, ContactQuery.SELECT_ALL);
    }

    @Override
    protected Contact mapRow(ResultSet resultSet, int i) throws SQLException {
        Contact contact = new Contact();
        contact.setId(resultSet.getLong("id"));
        contact.setFirst_name(resultSet.getString("first_name"));
        contact.setSecond_name(resultSet.getString("second_name"));
        contact.setBirth_date(resultSet.getDate("birth_date"));

        return contact;
    }
}
