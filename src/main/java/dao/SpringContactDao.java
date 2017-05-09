package dao;

import static dao.queries.ContactQuery.*;

import dao.entities.Contact;
import dao.entities.ContactTelDetail;
import dao.interfaces.ContactDAO;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringContactDao implements ContactDAO, InitializingBean {
    private DataSource dataSource;
    private NamedParameterJdbcTemplate template;

    @Resource(name = "dataSource")
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(dataSource == null){
            throw new BeanCreationException("DataSource is null");
        }
    }

    @Override
    public List<Contact> findAll() {
        return template.query(SELECT_ALL, new ContactMapper());
    }

    @Override
    public List<Contact> findByFirstName(String firstName) {
        Map<String, Object> parametrs = new HashMap<>();
        parametrs.put("firstName", firstName);

        return template.query(SELECT_BY_FIRST_NAME, (resultSet, i) -> {
            Contact contact = new Contact();
            contact.setId(resultSet.getLong("id"));
            contact.setFirst_name(resultSet.getString("first_name"));
            contact.setSecond_name(resultSet.getString("second_name"));
            contact.setBirth_date(resultSet.getDate("birth_date"));
            return contact;
        });
    }

    @Override
    public List<Contact> findAllWithDetails() {
        return template.query(SELECT_ALL_WITH_DETAILS,
                resultSet -> {
                    Map<Long, Contact> map = new HashMap<>();
                    Contact contact = null;

                    while (resultSet.next()) {
                        Long id = resultSet.getLong("id");
                        contact = map.get(id);

                        if(contact == null){
                            contact = new Contact();
                            contact.setId(id);
                            contact.setFirst_name(resultSet.getString("first_name"));
                            contact.setSecond_name(resultSet.getString("second_name"));
                            contact.setBirth_date(resultSet.getDate("birth_date"));
                            contact.setDetails(new ArrayList<>());
                            map.put(id, contact);
                        }

                        Long contact_tel_detail = resultSet.getLong("tel_id");

                        if (contact_tel_detail > 0){
                            ContactTelDetail detail = new ContactTelDetail();
                            detail.setId(resultSet.getLong("tel_id"));
                            detail.setContact_id(resultSet.getLong("contact_id"));
                            detail.setTel_number(resultSet.getString("tel_number"));
                            detail.setTel_type(resultSet.getString("tel_type"));
                            contact.getDetails().add(detail);
                        }

                    }

                    return new ArrayList<>(map.values());
                });
    }

    @Override
    public String findFirstNameById(Long id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);

        return template.queryForObject(SELECT_FIRST_NAME_BY_ID, parameters, String.class);
    }

    @Override
    public String findLastNameById(Long id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);

        return template.queryForObject(SELECT_LAST_NAME_BY_ID, parameters, String.class);
    }

    @Override
    public void insert(Contact contact) {

    }

    @Override
    public void insertWithDetails(Contact contact) {

    }

    @Override
    public void update(Contact contact) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("first_name", contact.getFirst_name());
        parameters.put("second_name", contact.getSecond_name());
        parameters.put("birth_date", contact.getBirth_date());
        parameters.put("id", contact.getId());

        template.update(UPDATE, parameters);
    }

    @Override
    public void delete(Long id) {

    }

    private static class ContactMapper implements RowMapper<Contact>{
        @Override
        public Contact mapRow(ResultSet resultSet, int i) throws SQLException {
            Contact contact = new Contact();
            contact.setId(resultSet.getLong("id"));
            contact.setFirst_name(resultSet.getString("first_name"));
            contact.setSecond_name(resultSet.getString("second_name"));
            contact.setBirth_date(resultSet.getDate("birth_date"));
            return contact;
        }
    }

    private static class ContactWithDetailsMapper implements ResultSetExtractor<List<Contact>>{
        @Override
        public List<Contact> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Map<Long, Contact> map = new HashMap<>();
            Contact contact = null;

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                contact = map.get(id);

                if(contact == null){
                    contact = new Contact();
                    contact.setId(id);
                    contact.setFirst_name(resultSet.getString("first_name"));
                    contact.setSecond_name(resultSet.getString("second_name"));
                    contact.setBirth_date(resultSet.getDate("birth_date"));
                    contact.setDetails(new ArrayList<>());
                    map.put(id, contact);
                }

                Long contact_tel_detail = resultSet.getLong("tel_id");

                if (contact_tel_detail > 0){
                    ContactTelDetail detail = new ContactTelDetail();
                    detail.setId(resultSet.getLong("tel_id"));
                    detail.setContact_id(resultSet.getLong("contact_id"));
                    detail.setTel_number(resultSet.getString("tel_number"));
                    detail.setTel_type(resultSet.getString("tel_type"));
                    contact.getDetails().add(detail);
                }

            }

            return new ArrayList<>(map.values());
        }
    }
}
