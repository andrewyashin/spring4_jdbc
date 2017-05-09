package dao.queries;

import dao.entities.Contact;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class SelectContactsByFirstName extends MappingSqlQuery<Contact> {

    public SelectContactsByFirstName(DataSource ds) {
        super(ds, ContactQuery.SELECT_BY_FIRST_NAME);
        super.declareParameter(new SqlParameter("name", Types.VARCHAR));
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
